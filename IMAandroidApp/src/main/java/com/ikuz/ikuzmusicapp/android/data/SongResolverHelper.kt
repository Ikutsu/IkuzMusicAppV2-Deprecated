package com.ikuz.ikuzmusicapp.android.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.ikuz.ikuzmusicapp.android.data.model.SongModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


/**
 * TODO: Add request for permissions
 * **/
class SongResolverHelper
@Inject constructor(@ApplicationContext val context: Context) {

    var mCursor: Cursor? = null

    val projection : Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.ALBUM,
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.DURATION
    )

    val selectionClause : String? = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ? "

    var selectionArg = arrayOf("1")

    val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    @WorkerThread
    fun getAudioData(): List<SongModel>{
        return getCursorData()
    }

    fun getCursorData(): MutableList<SongModel> {
        val audioList = mutableListOf<SongModel>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArg,
            sortOrder
        )

        mCursor?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)


            cursor.apply {
                if (count == 0){
                    Log.e("Cursor", "Cursor empty")
                }
                else {
                    while (cursor.moveToNext()) {
                        val data = getString(displayNameColumn)
                        val title = getString(titleColumn)
                        val artist = getString(artistColumn)
                        val album = getString(albumColumn)
                        val displayName = getString(displayNameColumn)
                        val id = getLong(idColumn)
                        val duration = getInt(durationColumn)
                            val uri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                        println(title)
                        audioList += SongModel(
                            uri, data, title, artist, album, displayName, id, duration
                        )
                    }
                }
            }
        }

        return audioList
    }


}