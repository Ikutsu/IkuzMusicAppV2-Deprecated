package com.ikuz.ikuzmusicapp.android.ui.song

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
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

    val state = MutableStateFlow(SongViewModelState())
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    private val _showNotFound = MutableStateFlow(false)
    val showNotFound: StateFlow<Boolean> = _showNotFound.asStateFlow()
    var query = mutableStateOf("")
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
                delay(300)
                state.value = state.value.copy(
                    songRepository.getSongData(),
                    songRepository.getArtistData(),
                    songRepository.getAlbumData()
                )
                loading.value = false
            }
        }
    }

    fun searchSong(query: String){
        viewModelScope.launch{
            state.value = state.value.copy(
                onSongQuery(query),
                onArtistQuery(query),
                onAlbumQuery(query)
            )
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

    fun onQueryChange(query: String){
        this.query.value = query
    }

    fun openDialog(){
        _showDialog.value = true
    }

    fun disableDialog() {
        _showDialog.value = false
    }
}