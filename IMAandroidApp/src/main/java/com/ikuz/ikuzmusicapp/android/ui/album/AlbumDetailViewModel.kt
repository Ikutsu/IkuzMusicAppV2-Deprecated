package com.ikuz.ikuzmusicapp.android.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel
import com.ikuz.ikuzmusicapp.android.data.repository.LocalSongRepository
import com.ikuz.ikuzmusicapp.android.ui.destinations.albumDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val localSongRepository: LocalSongRepository,
) : ViewModel() {

    val state = MutableStateFlow(AlbumDetailUiState())
    private val albumNavArgs = albumDetailDestination.argsFrom(savedStateHandle)

    init {
        albumDetails()
    }

    private fun albumDetails(){
        viewModelScope.launch {
            state.value = state.value.copy(
                albumName = onAlbum(albumNavArgs.albumId).first().album,
                albumId = albumNavArgs.albumId,
                albumArtist = onAlbum(albumNavArgs.albumId).first().artist,
                albumNumSong = onAlbum(albumNavArgs.albumId).first().numsongs,
                albumSongList = onAlbumSongList(albumNavArgs.albumId),
            )
        }
    }

    private suspend fun onAlbumSongList(albumId: Long): List<SongListModel>{
        return localSongRepository.getSongData().filter { it.albumId == albumId }
    }
    private suspend fun onAlbum(albumId: Long): List<AlbumListModel>{
        return localSongRepository.getAlbumData().filter {it.albumId == albumId }
    }

}