package com.ikuz.ikuzmusicapp.android.ui.local

import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel

data class LocalUiState(
    val songList: List<SongListModel> = listOf(),
    val artistList: List<ArtistListModel> = listOf(),
    val albumList: List<AlbumListModel> = listOf(),
)