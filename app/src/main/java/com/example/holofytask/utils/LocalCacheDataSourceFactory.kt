package com.example.holofytask.utils

import com.example.holofytask.HolofyApplication
import com.example.holofytask.HolofyApplication.Companion.context
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class LocalCacheDataSourceFactory() : DataSource.Factory {

    private val defaultDataSourceFactory: DefaultDataSourceFactory


    private val fileDataSource: FileDataSource = FileDataSource()

    lateinit var cacheDataSink: CacheDataSink

    init {

        if (simpleCache == null) {
            simpleCache = SimpleCache(
                File(context.cacheDir, "media"),
                LeastRecentlyUsedCacheEvictor(HolofyApplication.exoPlayerCacheSize)
            )
        }


        val userAgent = "Demo"
        val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()
        defaultDataSourceFactory = DefaultDataSourceFactory(
            HolofyApplication.context,
            bandwidthMeter,
            DefaultHttpDataSourceFactory(userAgent)
        )


    }

    companion object {
        var simpleCache: SimpleCache? = null


    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
            simpleCache, defaultDataSourceFactory.createDataSource(),
            fileDataSource, cacheDataSink,
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}