package com.ikuz.ikuzmusicapp.android.ui.song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuz.ikuzmusicapp.android.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val songRepository: SongRepository
): ViewModel(){

    val state = MutableStateFlow(SongViewModelState())
    init {
        viewModelScope.launch{
            state.value = state.value.copy(songRepository.getSongData())
            println(state)
        }
    }

}