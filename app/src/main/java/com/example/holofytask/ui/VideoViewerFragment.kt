package com.example.holofytask.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.holofytask.HolofyApplication
import com.example.holofytask.R
import com.example.holofytask.data.models.VideoModel
import com.example.holofytask.databinding.FragmentVideoViewerBinding
import com.example.holofytask.ui.viewmodel.VideoViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_video_viewer.*


class VideoViewerFragment : Fragment() {

    private val videoViewModel: VideoViewModel by activityViewModels()

    lateinit var viewBinding: FragmentVideoViewerBinding

    private val args: VideoViewerFragmentArgs by navArgs()

    private lateinit var httpDataSourceFactory: HttpDataSource.Factory

    @SuppressLint("StaticFieldLeak")
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var cacheDataSourceFactory: DataSource.Factory
    private val simpleCache: SimpleCache = HolofyApplication.simpleCache


    var playBackPosition: Long = 0L
    var windowIndex: Int = 0
    var index: Int = 0
    lateinit var videoModel: VideoModel

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.anim_duration).toLong()
            scrimColor = Color.TRANSPARENT
        }

        args.let {

            index = it.position
            windowIndex = it.windowIndex
            playBackPosition = it.playbackPos
            videoModel = it.videoItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentVideoViewerBinding.inflate(layoutInflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            tvTitle.text = videoModel.videoTitle
            tvSubtitle.text = videoModel.videoSubTitle
            tv_content.text = videoModel.content
            ivClose.setOnClickListener { findNavController().popBackStack() }
        }


    }


    override fun onStart() {
        super.onStart()
        initializePlayer()

    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }


    private fun initializePlayer() {

        val trackSelector = DefaultTrackSelector(requireContext()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())

        }

        httpDataSourceFactory = DefaultHttpDataSourceFactory("sample")


        defaultDataSourceFactory = DefaultDataSourceFactory(
            context, httpDataSourceFactory
        )

        defaultDataSourceFactory = DefaultDataSourceFactory(context,
            httpDataSourceFactory
        )
        // CacheDataSourceFactory class is deprecated
        cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache, defaultDataSourceFactory,
            CacheDataSource.FLAG_BLOCK_ON_CACHE or
                    CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        val mediaSourceFactory = ProgressiveMediaSource.Factory(cacheDataSourceFactory)

        val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse(videoModel.videoUrl))

        player = SimpleExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->

                viewBinding.playerView.player = exoPlayer
                exoPlayer.apply {
                    playWhenReady = true
                    volume = 1f

                    prepare(
                        mediaSource
                    )

                    seekTo(windowIndex, playBackPosition)
                }

                viewBinding.playerView.setKeepContentOnPlayerReset(true)
            }
    }

    private fun releasePlayer() {

        player?.let {
            videoViewModel.putMediaData(it.currentWindowIndex, it.currentPosition, true)
            it.release()
        }

        player = null

    }


}