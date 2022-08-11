package com.ikuz.ikuzmusicapp.android.ui.artist

import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel

data class ArtistDetailUiState(
    val artistName: String = "",
    val artistSongList: List<SongListModel> = listOf(),
    val artistAlbumList: List<AlbumListModel> = listOf(),
)