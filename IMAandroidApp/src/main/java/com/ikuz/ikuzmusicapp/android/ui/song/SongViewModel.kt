package com.ikuz.ikuzmusicapp.android.ui.song

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuz.ikuzmusicapp.android.data.model.AlbumModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistModel
import com.ikuz.ikuzmusicapp.android.data.model.SongModel
import com.ikuz.ikuzmusicapp.android.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val songRepository: SongRepository
): ViewModel(){

    val state = MutableStateFlow(SongUiState())
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    private val _showNotFound = MutableStateFlow(false)
    val showNotFound: StateFlow<Boolean> = _showNotFound.asStateFlow()
    var loading = mutableStateOf(false)

    init {
        songQuery()
    }

    fun songQuery(){
        viewModelScope.launch{
            if (songRepository.getSongData().isEmpty()){
                _showNotFound.value = true
            }else{
                loading.value = true
                delay(200)
                state.value = state.value.copy(
                    songList = songRepository.getSongData(),
                    artistList = songRepository.getArtistData(),
                    albumList = songRepository.getAlbumData()
                )
                loading.value = false
            }
        }
    }

    fun searchSong(query: String){
        viewModelScope.launch{
            if (query.isEmpty()){
                state.value = state.value.copy(
                    onSongQuery(query),
                    onArtistQuery(query),
                    onAlbumQuery(query)
                )
            }else if (query.isNotEmpty()){
                loading.value = true
                delay(200)
                state.value = state.value.copy(
                    onSongQuery(query),
                    onArtistQuery(query),
                    onAlbumQuery(query)
                )
                loading.value = false
            }
        }
    }
//    @Query ("SELECT * FROM SongModel WHERE title LIKE :title")

    suspend fun onSongQuery(title: String): List<SongModel> {
        return songRepository.getSongData().filter { it.title.contains(title, true) }
    }
    suspend fun onArtistQuery(title: String): List<ArtistModel>{
        return songRepository.getArtistData().filter { it.artist.contains(title, true) }
    }
    suspend fun onAlbumQuery(title: String): List<AlbumModel>{
        return songRepository.getAlbumData().filter { it.album.contains(title,true) }
    }

    fun openDialog(){
        _showDialog.value = true
    }

    fun disableDialog() {
        _showDialog.value = false
    }
}