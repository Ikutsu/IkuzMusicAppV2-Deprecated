package com.ikuz.ikuzmusicapp.android.data.model

import android.graphics.Bitmap

data class AlbumListModel(
    val album : String,
//    val _data : String,
//    val _id : Long,
    val albumId: Long,
//    val album_art : String?,
    val numsongs : Int,
    val artist : String,
//    val album_art : String
)