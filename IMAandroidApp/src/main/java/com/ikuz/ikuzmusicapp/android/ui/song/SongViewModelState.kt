package com.ikuz.ikuzmusicapp.android.ui.song

import com.ikuz.ikuzmusicapp.android.data.model.SongModel

data class SongViewModelState(
    val songList: List<SongModel> = listOf()
)