package com.ikuz.ikuzmusicapp.android.data.repository

import com.ikuz.ikuzmusicapp.android.data.SongResolverHelper
import com.ikuz.ikuzmusicapp.android.data.model.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepository
@Inject constructor(private val songResolverHelper: SongResolverHelper){
    suspend fun getSongData():List<SongModel> = withContext(Dispatchers.IO) {
        songResolverHelper.getAudioData()
    }
}