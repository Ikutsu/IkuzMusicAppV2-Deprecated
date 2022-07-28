package com.ikuz.ikuzmusicapp.android.ui.song

import com.ikuz.ikuzmusicapp.android.data.model.AlbumModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistModel
import com.ikuz.ikuzmusicapp.android.data.model.SongModel

data class SongViewModelState(
    val songList: List<SongModel> = listOf(),
    val artistList: List<ArtistModel> = listOf(),
    val albumList: List<AlbumModel> = listOf()

)