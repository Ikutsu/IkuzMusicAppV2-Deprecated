package com.ikuz.ikuzmusicapp.android.media.media3

import android.content.Context
import androidx.media3.common.MediaItem

class BrowseTree(
    val context: Context,
    val musicSource: MusicSource
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaItem>>()
    private val mediaIdToMediaItem = mutableMapOf<String, MediaItem>()

    fun getMediaItemByMediaId(mediaId: String) = mediaIdToMediaItem[mediaId]

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    init {
        val rootList = mediaIdToChildren[IMA_BROWSABLE_ROOT] ?: mutableListOf()
        mediaIdToChildren[IMA_BROWSABLE_ROOT] = rootList
    }
}

const val IMA_BROWSABLE_ROOT = "/"
