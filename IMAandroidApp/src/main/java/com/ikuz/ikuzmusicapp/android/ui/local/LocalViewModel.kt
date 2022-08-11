package com.ikuz.ikuzmusicapp.android.ui.local

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel
import com.ikuz.ikuzmusicapp.android.data.repository.LocalSongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val localSongRepository: LocalSongRepository
): ViewModel(){

    val state = MutableStateFlow(LocalUiState())
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    private val _showNotFound = MutableStateFlow(false)
    val showNotFound: StateFlow<Boolean> = _showNotFound.asStateFlow()
    var loading = mutableStateOf(false)

    init {
        songQuery()
    }

    private fun songQuery(){
        viewModelScope.launch{
            if (localSongRepository.getSongData().isEmpty()){
                _showNotFound.value = true
            }else{
                loading.value = true
                delay(200)
                state.value = state.value.copy(
                    songList = localSongRepository.getSongData(),
                    artistList = localSongRepository.getArtistData(),
                    albumList = localSongRepository.getAlbumData()
                )
                loading.value = false
            }
        }
    }

    fun searchSong(query: String){
        viewModelScope.launch{
            if (query.isEmpty()){
                state.value = state.value.copy(
                    songList = onSongQuery(query),
                    artistList = onArtistQuery(query),
                    albumList = onAlbumQuery(query)
                )
            }else if (query.isNotEmpty()){
                loading.value = true
                delay(200)
                state.value = state.value.copy(
                    songList = onSongQuery(query),
                    artistList = onArtistQuery(query),
                    albumList = onAlbumQuery(query)
                )
                loading.value = false
            }
        }
    }

    private suspend fun onSongQuery(title: String): List<SongListModel> {
        return localSongRepository.getSongData().filter { it.title.contains(title, true) }
    }
    private suspend fun onArtistQuery(title: String): List<ArtistListModel>{
        return localSongRepository.getArtistData().filter { it.artist.contains(title, true) }
    }
    private suspend fun onAlbumQuery(title: String): List<AlbumListModel>{
        return localSongRepository.getAlbumData().filter { it.album.contains(title,true) }
    }

    fun openDialog(){
        _showDialog.value = true
    }

    fun disableDialog() {
        _showDialog.value = false
    }
}