package com.ikuz.ikuzmusicapp.android.data.repository

import com.ikuz.ikuzmusicapp.android.data.resolver.LocalAlbumResolver
import com.ikuz.ikuzmusicapp.android.data.resolver.LocalArtistResolver
import com.ikuz.ikuzmusicapp.android.data.resolver.LocalSongResolver
import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistListModel
import com.ikuz.ikuzmusicapp.android.data.model.SongListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalSongRepository
@Inject constructor(
    private val localSongResolver: LocalSongResolver,
    private val localArtistResolver: LocalArtistResolver,
    private val localAlbumResolver: LocalAlbumResolver
)
{
    suspend fun getSongData():List<SongListModel> = withContext(Dispatchers.IO) {
        localSongResolver.getAudioData()
    }
    suspend fun getArtistData():List<ArtistListModel> = withContext(Dispatchers.IO) {
        localArtistResolver.getArtistData()
    }
    suspend fun getAlbumData():List<AlbumListModel> = withContext(Dispatchers.IO) {
        localAlbumResolver.getAlbumData()
    }
}