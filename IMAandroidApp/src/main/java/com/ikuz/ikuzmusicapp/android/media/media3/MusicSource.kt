package com.ikuz.ikuzmusicapp.android.media.media3

import androidx.annotation.IntDef
import androidx.media3.common.MediaItem

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State
const val STATE_CREATED = 1
const val STATE_INITIALIZING = 2
const val STATE_INITIALIZED = 3
const val STATE_ERROR = 4

interface MusicSource: Iterable<MediaItem> {

    suspend fun load()

    fun whenReady(performAction: (Boolean) -> Unit): Boolean
}

abstract class AbstractMusicSource: MusicSource {

    @State
    var state: Int = STATE_CREATED
        set(value) {
            if (
                value == STATE_INITIALIZED
                || value == STATE_ERROR
            ){
                synchronized(onReadyListener) {
                    field = value
                    onReadyListener.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else{
                field = value
            }
        }

    private val onReadyListener = mutableListOf<(Boolean) -> Unit>()

    override fun whenReady(performAction: (Boolean) -> Unit): Boolean =
        when (state) {
            STATE_CREATED, STATE_INITIALIZING -> {
                onReadyListener += performAction
                false
            }
            else -> {
                performAction(state != STATE_ERROR)
                true
            }
        }
}