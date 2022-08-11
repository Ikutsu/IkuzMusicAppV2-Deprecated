package com.ikuz.ikuzmusicapp.android.data.resolver

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.ikuz.ikuzmusicapp.android.data.model.ArtistListModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalArtistResolver
@Inject constructor(@ApplicationContext val context: Context) {

    private var mCursor: Cursor? = null

    private val projection = arrayOf(
        MediaStore.Audio.ArtistColumns.ARTIST,
        MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
    )

    @WorkerThread
    fun getArtistData(): List<ArtistListModel>{
        return getCursorData()
    }

    private fun getCursorData(): MutableList<ArtistListModel>{
        val artistList = mutableListOf<ArtistListModel>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        mCursor?.use { cursor ->
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)
            val numberOfTracksColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

            cursor.apply {
                if (count == 0){
                    Log.e("ArtistResolverHelper", "No artists found")
                }
                else{
                    while (moveToNext()){
                        val artist = getString(artistColumn)
                        val numberOfTracks = getInt(numberOfTracksColumn)

                        artistList += ArtistListModel(artist, numberOfTracks)
                    }
                }
            }
        }
        return artistList
    }
}