package com.ikuz.ikuzmusicapp.android.data

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.ikuz.ikuzmusicapp.android.data.model.AlbumModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlbumResolver
@Inject constructor(@ApplicationContext val context: Context) {

    private var mCursor: Cursor? = null

    private val projection = arrayOf(
        MediaStore.Audio.AlbumColumns.ALBUM,
        MediaStore.Audio.AlbumColumns.ALBUM_ID,
        MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
        MediaStore.Audio.AlbumColumns.ARTIST,
    )

    @WorkerThread
    fun getAlbumData(): List<AlbumModel> {
        return getCursorData()
    }

    // TODO: fetch albumArt from _id [DONE]
    fun getCursorData(): MutableList<AlbumModel> {
        val albumlist = mutableListOf<AlbumModel>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        mCursor?.use { cursor ->
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM_ID)
            val songCountColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST)

            cursor.apply {
                if (count == 0) {
                    Log.e("AlbumResolverHelper", "No albums found")
                } else {
                    while (moveToNext()) {
                        val album = getString(albumColumn)
                        val albumId = getLong(albumIdColumn)
                        val songCount = getInt(songCountColumn)
                        val artist = getString(artistColumn)
//                        val albumArt = "content://media/external/audio/albumart/$albumId"
//                        println(albumArt)
                        albumlist += AlbumModel(album, albumId, songCount, artist)
                    }
                }
            }
        }
        return albumlist
    }
}