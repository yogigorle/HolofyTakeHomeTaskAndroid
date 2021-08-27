package com.example.holofytask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.holofytask.R
import com.example.holofytask.data.models.VideoModel
import com.example.holofytask.databinding.FragmentVideosListBinding
import com.example.holofytask.ui.viewmodel.VideoViewModel
import com.example.holofytask.utils.RecyclerViewScrollListener
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_videos_list.*

@AndroidEntryPoint
class VideosListFragment : Fragment() {

    lateinit var fragmentVideoListBinding: FragmentVideosListBinding

    private val videoViewModel: VideoViewModel by activityViewModels()

    private val videosAdapter = VideosAdapter(::onVideoItemClicked)

    var currentWindowIndex = 0
    var playbackPosition = 0L

    var position = 0

    var firstTime = false


    private lateinit var scrollListener: RecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentVideoListBinding = FragmentVideosListBinding.inflate(layoutInflater)

        return fragmentVideoListBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        videoViewModel.getVideoData().observe(viewLifecycleOwner, Observer {
            videosAdapter.addItemsToTheList(it.toMutableList())
        })

        videoViewModel.mediaModel.observe(viewLifecycleOwner, Observer {
            currentWindowIndex = it.currentWindowIndex
            playbackPosition = it.currentPosition
            firstTime = it.bool
        })

        setAdapter()


    }

    private fun onVideoItemClicked(videoItem: VideoModel, position: Int, cardView: View) {

        val videoCardTransitionName = getString(R.string.video_viewer_transition_name)
        val extras = FragmentNavigatorExtras(cardView to videoCardTransitionName)

        val action =
            VideosListFragmentDirections.actionVideosListFragmentToVideoViewerFragment(
                PlayerViewAdapter.getCurrentWindowIndex(),
                PlayerViewAdapter.getCurrentPlaybackPos(),
                position,
                videoItem
            )

        Navigation.findNavController(fragmentVideoListBinding.root)
            .navigate(action, extras)

        PlayerViewAdapter.pauseCurrentPlayingVideo()

        //exit and return transition
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.anim_duration).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.anim_duration).toLong()
        }
    }


    private fun setAdapter() {
        rv_videos.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = videosAdapter

        }


        scrollListener = object : RecyclerViewScrollListener() {
            override fun onItemVisible(index: Int) {
                position = index
                if (index != -1) {

                    if (!firstTime) {
                        PlayerViewAdapter.playCurrentIndexAndPausePreviousPlayer(index)
                    } else {
                        PlayerViewAdapter.playIndexThenPausePreviousPlayerFromPos(
                            index,
                            currentWindowIndex,
                            playbackPosition
                        )
                    }
                }
            }
        }
        rv_videos.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }

}