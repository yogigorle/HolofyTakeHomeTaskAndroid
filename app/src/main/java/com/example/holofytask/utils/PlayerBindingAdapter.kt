package com.example.holofytask.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.example.holofytask.HolofyApplication
import com.example.holofytask.utils.LocalCacheDataSourceFactory
import com.example.holofytask.utils.PlayerStateCallBack
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache

class PlayerViewAdapter() {


    companion object {


        private lateinit var httpDataSourceFactory: HttpDataSource.Factory

        @SuppressLint("StaticFieldLeak")
        private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
        private lateinit var cacheDataSourceFactory: DataSource.Factory
        private val simpleCache: SimpleCache = HolofyApplication.simpleCache

        //players map to hold all the generated players
        private var playersMap: MutableMap<Int, SimpleExoPlayer> = mutableMapOf()

        //to get the current playing video
        private var currentPlayingVideo: Pair<Int, SimpleExoPlayer>? = null

        //to release all the attatched plyers
        fun releaseAllPlayers() {
            playersMap.map {
                it.value.release()
            }
        }

        //method to release player at recylced item.
        fun releaseRecycledPlayer(index: Int) {
            playersMap[index]?.release()
            LocalCacheDataSourceFactory.simpleCache?.release()
            LocalCacheDataSourceFactory.simpleCache = null
        }

        //method to pause any ongoing players when scrolled
        fun pauseCurrentPlayingVideo() {
            if (currentPlayingVideo != null) {
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }


        //method to play the video at the current index and pause previous index in recycler view
        fun playCurrentIndexAndPausePreviousPlayer(index: Int) {
            if (playersMap[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }
        }

        fun playIndexThenPausePreviousPlayerFromPos(
            index: Int,
            currentWindowIndex: Int,
            currentPosition: Long
        ) {
            if (playersMap[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap.get(index)?.playWhenReady = true
                playersMap[index]?.seekTo(currentWindowIndex, currentPosition)
                currentPlayingVideo = Pair(index, playersMap.get(index)!!)
            }

        }

        fun getCurrentPlaybackPos(): Long {
            return currentPlayingVideo?.second?.currentPosition!!
        }

        fun getCurrentWindowIndex(): Int {
            return currentPlayingVideo?.second?.currentWindowIndex!!
        }


        //actual binding adapter

        @JvmStatic
        @BindingAdapter(
            value = ["video_url", "on_state_change", "thumbnail", "progressbar", "item_index", "autoPlay"],
            requireAll = false
        )
        fun PlayerView.loadVideo(
            url: String,
            callBack: PlayerStateCallBack? = null,
            thumbnail: ImageView? = null,
            progressBar: ProgressBar? = null,
            index: Int? = null,
            autoPlay: Boolean = true
        ) {

            if (url == null) return


            httpDataSourceFactory = DefaultHttpDataSourceFactory("sample")


            defaultDataSourceFactory = DefaultDataSourceFactory(
                context, httpDataSourceFactory
            )

            defaultDataSourceFactory = DefaultDataSourceFactory(context, httpDataSourceFactory)
            // CacheDataSourceFactory class is deprecated
            cacheDataSourceFactory = CacheDataSourceFactory(
                simpleCache, defaultDataSourceFactory,
                CacheDataSource.FLAG_BLOCK_ON_CACHE or
                        CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
            )
            val mediaSourceFactory = ProgressiveMediaSource.Factory(cacheDataSourceFactory)

            val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse(url))

            val player = SimpleExoPlayer.Builder(context).build()

            player.prepare(mediaSource, true, true)

            player.apply {
                playWhenReady = autoPlay
                repeatMode = Player.REPEAT_MODE_ALL
                setKeepContentOnPlayerReset(true)
                volume = 0f
            }

            // Provide url to load the video from here


            //to disable video controller(which contains play and pause)
            this.useController = false


            this.player = player


            //add player to the players map with its index
            if (playersMap.containsKey(index)) {
                playersMap.remove(index)
            }
            if (index != null) {
                playersMap[index] = player
            }
            this.player!!.addListener(object : Player.EventListener {

                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
//                    context.showToast("player error $error")
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)

                    when (true) {
                        playbackState == Player.STATE_BUFFERING -> {
                            callBack?.onVideoBuffering(player)
                            thumbnail?.visibility = VISIBLE
                            progressBar?.visibility = VISIBLE
                        }

                        playbackState == Player.STATE_READY -> {
                            thumbnail?.visibility = GONE
                            progressBar?.visibility = GONE
                        }

                        playbackState == Player.STATE_READY && player.playWhenReady -> {
//                            thumbnail.visibility = GONE
                            callBack?.onStartPlaying(player)
                        }
                    }
                }
            })
        }

    }


}