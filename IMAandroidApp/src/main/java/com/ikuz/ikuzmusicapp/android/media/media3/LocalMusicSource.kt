package com.ikuz.ikuzmusicapp.android.media.media3

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel
import com.ikuz.ikuzmusicapp.android.data.repository.LocalSongRepository
import javax.inject.Inject

internal class LocalMusicSource (private val repository: LocalSongRepository): AbstractMusicSource() {

    private var audioMetadata: List<MediaItem> = emptyList()

    override fun iterator(): Iterator<MediaItem> = audioMetadata.iterator()

    init {
        state = STATE_INITIALIZING
    }

    override suspend fun load() {
        updateMusicSource(repository)?.let { updatedCatalog ->
            audioMetadata = updatedCatalog
            state = STATE_INITIALIZED
        } ?: run {
            audioMetadata = emptyList()
            state = STATE_ERROR
        }
    }

    private suspend fun updateMusicSource (repository: LocalSongRepository): List<MediaItem>?{
        return repository.getSongData().map { audio ->
            val albumArtUrl = "content://media/external/audio/albumart/" + audio.albumId
            val imageUrl = Uri.parse(albumArtUrl)
            val mediaMetadata = MediaMetadata.Builder()
                .from(audio)
                .apply {
                    setArtworkUri(imageUrl)
                }
                .build()
            MediaItem.Builder().apply {
                setMediaId(audio.id.toString())
                setUri(audio.uri)
                setMediaMetadata(mediaMetadata)
            }.build()
        }.toList()

    }

    private fun MediaMetadata.Builder.from(audio: SongListModel): MediaMetadata.Builder {
        setArtist(audio.artist)
        setTitle(audio.title)
        setIsPlayable(true)
        val bundle = Bundle()
        bundle.putLong("durationMs", audio.duration)
        return this
    }

    fun asMediaSource(
        dataSource: CacheDataSource.Factory
    ): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        audioMetadata.forEach { mMediaItem ->
            val mediaSource = ProgressiveMediaSource
                .Factory(dataSource)
                .createMediaSource(mMediaItem)

            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }
}
