package com.ikuz.ikuzmusicapp.android.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Build
import android.os.ConditionVariable
import android.util.Log
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.*
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.ikuz.ikuzmusicapp.android.data.repository.LocalSongRepository
import com.ikuz.ikuzmusicapp.android.media.media3.BrowseTree
import com.ikuz.ikuzmusicapp.android.media.media3.IMA_BROWSABLE_ROOT
import com.ikuz.ikuzmusicapp.android.media.media3.LocalMusicSource
import com.ikuz.ikuzmusicapp.android.media.media3.MusicSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
open class MusicService @Inject constructor(private val repository: LocalSongRepository): MediaLibraryService() {

    @Inject
    lateinit var player: ExoPlayer

    private lateinit var musicSource: MusicSource

    private lateinit var mediaSession: MediaLibrarySession

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val browseTree: BrowseTree by lazy {
        BrowseTree(applicationContext, musicSource)
    }


    private val executorService by lazy {
        MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor())
    }

    open fun getCallback(): MediaLibrarySession.Callback {
        return MusicServiceCallBack()
    }

    override fun onCreate() {
        super.onCreate()

        mediaSession = with(MediaLibrarySession.Builder(
            this, player, getCallback())) {
            setId(packageName)
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(
                    this@MusicService,
                    0,
                    sessionIntent,
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )
            }
            build()
        }

        musicSource = LocalMusicSource(repository)
        serviceScope.launch {
            musicSource.load()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        removeMediaSession()
    }

    private fun removeMediaSession() {
        mediaSession.run {
            release()
        }
        serviceJob.cancel()
    }


    override fun onGetSession(controllerInfo: ControllerInfo): MediaLibrarySession {
        return mediaSession
    }

    private fun openWhenReady(conditionVariable: ConditionVariable): (Boolean) -> Unit = {
        conditionVariable.open()
    }

    private fun <T> callWhenMusicSourceReady(action: () -> T): ListenableFuture<T> {
        val conditionVariable = ConditionVariable()
        return if (musicSource.whenReady(openWhenReady(conditionVariable))) {
            Futures.immediateFuture(action())
        } else {
            executorService.submit<T> {
                conditionVariable.block();
                action()
            }
        }
    }

    open inner class MusicServiceCallBack: MediaLibrarySession.Callback{
        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            return callWhenMusicSourceReady {
                LibraryResult.ofItemList(
                    browseTree[parentId] ?: ImmutableList.of(),
                    LibraryParams.Builder().build()
                )
            }
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ): ListenableFuture<LibraryResult<MediaItem>> {
            return callWhenMusicSourceReady {
                LibraryResult.ofItem(
                    browseTree.getMediaItemByMediaId(mediaId) ?: MediaItem.EMPTY,
                    LibraryParams.Builder().build())
            }
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            return callWhenMusicSourceReady {
                mediaItems.map { browseTree.getMediaItemByMediaId(it.mediaId)!! }.toMutableList()
            }
        }
    }

}

const val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"
