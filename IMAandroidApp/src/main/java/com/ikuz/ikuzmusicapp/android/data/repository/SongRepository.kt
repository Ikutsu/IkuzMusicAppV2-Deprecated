package com.ikuz.ikuzmusicapp.android.data.repository

import com.ikuz.ikuzmusicapp.android.data.AlbumResolver
import com.ikuz.ikuzmusicapp.android.data.ArtistResolver
import com.ikuz.ikuzmusicapp.android.data.SongResolver
import com.ikuz.ikuzmusicapp.android.data.model.AlbumModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistModel
import com.ikuz.ikuzmusicapp.android.data.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepository
@Inject constructor(
    private val songResolver: SongResolver,
    private val artistResolver: ArtistResolver,
    private val albumResolver: AlbumResolver)
{
    suspend fun getSongData():List<SongModel> = withContext(Dispatchers.IO) {
        songResolver.getAudioData()
    }
    suspend fun getArtistData():List<ArtistModel> = withContext(Dispatchers.IO) {
        artistResolver.getArtistData()
    }
    suspend fun getAlbumData():List<AlbumModel> = withContext(Dispatchers.IO) {
        albumResolver.getAlbumData()
    }
}