package com.ikuz.ikuzmusicapp.android.data.model

import android.net.Uri

data class SongModel(
     var uri: Uri,
     var path: String,
     var title: String,
     var artist: String,
     var album: String,
     var id: Long,
     var duration: Int
)