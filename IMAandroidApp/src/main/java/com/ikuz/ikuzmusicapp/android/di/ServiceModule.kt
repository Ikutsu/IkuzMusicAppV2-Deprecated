package com.ikuz.ikuzmusicapp.android.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import java.io.File


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()


    @Provides
    @ServiceScoped
    fun providePlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer =
         ExoPlayer.Builder(context).build().apply {
             setAudioAttributes(audioAttributes, true)
             setHandleAudioBecomingNoisy(true)
             setWakeMode(C.WAKE_MODE_LOCAL)
         }

    @Provides
    @ServiceScoped
    fun provideDataSource(
        @ApplicationContext context: Context
    ) = DefaultDataSource.Factory(context)

    @Provides
    @ServiceScoped
    fun provideCachedDataSource(
        @ApplicationContext context: Context,
        dataSourceFactory: DefaultDataSource.Factory
    ): CacheDataSource.Factory {
        val cacheDir = File(context.cacheDir, "media")

        val dataSourceProvider = StandaloneDatabaseProvider(context)

        val cache = SimpleCache(cacheDir, NoOpCacheEvictor(), dataSourceProvider)
        return CacheDataSource.Factory().apply {
            setCache(cache)
            setUpstreamDataSourceFactory(dataSourceFactory)
        }
    }
}

//@Module
//@InstallIn(ServiceComponent::class)
//class MediaSessionModule {
//
//    @Provides
//    @ServiceScoped
//    fun provideMediaSession(@ApplicationContext context: Context, player: ExoPlayer): MediaSession {
//        return MediaSession.Builder(context, player).build()
//    }
//
//}