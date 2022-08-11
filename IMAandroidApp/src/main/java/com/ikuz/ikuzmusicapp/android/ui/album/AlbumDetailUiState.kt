package com.ikuz.ikuzmusicapp.android.ui.album

import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel

data class AlbumDetailUiState(
    val albumName : String = "",
    val albumId : Long = 0,
    val albumArtist : String = "",
    val albumNumSong : Int = 0,
    val albumSongList : List<SongListModel> = listOf(),
)
