package com.ikuz.ikuzmusicapp.android.media

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    serviceComponent: ComponentName
){
    val rootMediaItem = MutableLiveData<MediaItem>()
        .apply { postValue(MediaItem.EMPTY) }
    val playbackState = MutableLiveData<PlaybackState>()
        .apply { postValue(EMPTY_PLAYBACK_STATE) }
    val nowPlaying = MutableLiveData<MediaItem>()
        .apply { postValue(NOTHING_PLAYING) }
    val player: Player? get() = browser

    private var browser: MediaBrowser? = null
    private val playerListener: PlayerListener = PlayerListener()

    private val coroutineContext: CoroutineContext = Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext + SupervisorJob())

    init {
        scope.launch {
            val newBrowser =
                MediaBrowser.Builder(context, SessionToken(context, serviceComponent))
                    .setListener(BrowserListener())
                    .buildAsync()
                    .await()
            newBrowser.addListener(playerListener)
            browser = newBrowser
            rootMediaItem.postValue(
                newBrowser.getLibraryRoot(/* params= */ null).await().value
            )
            newBrowser.currentMediaItem?.let {
                nowPlaying.postValue(it)
            }
        }
    }

    suspend fun getChildren(parentId: String): ImmutableList<MediaItem> {
        return this.browser?.getChildren(parentId, 0, 100, null)?.await()?.value
            ?: ImmutableList.of()
    }

    suspend fun sendCommand(command: String, parameters: Bundle?):Boolean =
        sendCommand(command, parameters) { _, _ -> }

    suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ((Int, Bundle?) -> Unit)
    ):Boolean = if (browser?.isConnected == true) {
        val args = parameters ?: Bundle()
        browser?.sendCustomCommand(SessionCommand(command, args), args)?.await()?.let {
            resultCallback(it.resultCode, it.extras)
        }
        true
    } else {
        false
    }

    fun release() {
        rootMediaItem.postValue(MediaItem.EMPTY)
        nowPlaying.postValue(NOTHING_PLAYING)
        browser?.let {
            it.removeListener(playerListener)
            it.release()
        }
    }

    private fun updatePlaybackState(player: Player) {
        playbackState.postValue(
            PlaybackState(
                player.playbackState,
                player.playWhenReady,
                player.duration
            )
        )
    }

    private fun updateNowPlaying(player: Player) {
        val mediaItem = player.currentMediaItem ?: MediaItem.EMPTY
        if (mediaItem == MediaItem.EMPTY) {
            return
        }
        // The current media item from the CastPlayer may have lost some information.
        val mediaItemFuture = browser!!.getItem(mediaItem.mediaId)
        mediaItemFuture.addListener(
            Runnable {
                val fullMediaItem = mediaItemFuture.get().value ?: return@Runnable
                nowPlaying.postValue(
                    mediaItem.buildUpon().setMediaMetadata(fullMediaItem.mediaMetadata).build()
                )
            },
            MoreExecutors.directExecutor()
        )
    }

    private inner class BrowserListener : MediaBrowser.Listener {
        override fun onDisconnected(controller: MediaController) {
            release()
        }
    }

    private inner class PlayerListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
                || events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                updatePlaybackState(player)
            }
            if (events.contains(Player.EVENT_MEDIA_METADATA_CHANGED)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)) {
                updateNowPlaying(player)
            }
        }
    }
}

class PlaybackState(
    private val playbackState: Int = Player.STATE_IDLE,
    private val playWhenReady: Boolean = false,
    val duration: Long = C.TIME_UNSET) {
    val isPlaying: Boolean
        get() {
            return (playbackState == Player.STATE_BUFFERING
                    || playbackState == Player.STATE_READY)
                    && playWhenReady
        }
}

val EMPTY_PLAYBACK_STATE: PlaybackState = PlaybackState()
val NOTHING_PLAYING: MediaItem = MediaItem.EMPTY