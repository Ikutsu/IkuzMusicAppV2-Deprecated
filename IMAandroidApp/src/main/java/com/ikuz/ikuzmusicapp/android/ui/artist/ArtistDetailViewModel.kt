package com.ikuz.ikuzmusicapp.android.ui.artist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel
import com.ikuz.ikuzmusicapp.android.data.repository.LocalSongRepository
import com.ikuz.ikuzmusicapp.android.ui.destinations.artistDetailDestination
import com.ikuz.ikuzmusicapp.android.ui.local.LocalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val localSongRepository: LocalSongRepository,
): ViewModel() {


    val state = MutableStateFlow(ArtistDetailUiState())
    var loading = mutableStateOf(false)
    private val artistNavArgs = artistDetailDestination.argsFrom(savedStateHandle)

    init {
        viewModelScope.launch {
            artistDetail(artistNavArgs.artist)
        }
    }

    private fun artistDetail(artist: String?){
        if (artist != null){
            viewModelScope.launch{
                loading.value = true
                delay(200)
                state.value = state.value.copy(
                    artistName = artist,
                    artistSongList = onArtistSongDetails(artist),
                    artistAlbumList = onArtistAlbumDetails(artist)
                )
                loading.value = false
            }
        }
    }

    private suspend fun onArtistSongDetails(artist: String) : List<SongListModel>{
        return localSongRepository.getSongData().filter { it.artist == artist }
    }

    private suspend fun onArtistAlbumDetails(artist: String) : List<AlbumListModel>{
        return localSongRepository.getAlbumData().filter { it.artist == artist }
    }
}