package com.ikuz.ikuzmusicapp.android.data.model

import android.net.Uri

data class SongListModel(
     val uri: Uri,
     val path: String,
     val title: String,
     val artist: String,
     val album: String,
     val id: Long,
     val duration: Int,
     val albumId: Long
)