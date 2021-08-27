package com.example.holofytask.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.holofytask.R
import com.example.holofytask.data.models.VideoModel
import com.example.holofytask.databinding.CardviewVideoItemsBinding
import com.example.holofytask.utils.PlayerStateCallBack
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.cardview_video_items.view.*

class VideosAdapter(
    val onItemClicked: (model: VideoModel, position: Int, cardView: View) -> Unit
) :
    RecyclerView.Adapter<VideosAdapter.VideosItemViewHolder>(), PlayerStateCallBack {


    private var videosList: MutableList<VideoModel> = mutableListOf()

    inner class VideosItemViewHolder(private var videoItemView: CardviewVideoItemsBinding) :
        RecyclerView.ViewHolder(videoItemView.root) {

        fun onBind(model: VideoModel) {

            videoItemView.root.setOnClickListener {
                onItemClicked(
                    model,
                    adapterPosition,
                    it
                )
            }

            videoItemView.apply {
                dataModel = model
                callback = this@VideosAdapter
                index = adapterPosition
                executePendingBindings()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosItemViewHolder {
        val videoViewBinding: CardviewVideoItemsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cardview_video_items,
                parent,
                false
            )

        return VideosItemViewHolder(videoViewBinding)

    }

    override fun onBindViewHolder(holder: VideosItemViewHolder, position: Int) {
        val model = getItem(position)
        holder.onBind(model)

    }

    override fun getItemCount(): Int {
        return videosList.size
    }

    fun addItemsToTheList(videoItems: MutableList<VideoModel>) {
        if (videoItems.isNotEmpty()) {
            videosList.clear()
            videosList.addAll(videoItems)
            notifyDataSetChanged()
        }

    }

    private fun getItem(position: Int): VideoModel {
        return videosList[position]
    }

    override fun onVideoBuffering(player: Player) {
//        TODO("Not yet implemented")
    }

    override fun onStartPlaying(player: Player) {
//        TODO("Not yet implemented")
    }

    override fun onFinishedPlaying(player: Player) {
//        TODO("Not yet implemented")
    }

    override fun onViewRecycled(holder: VideosItemViewHolder) {
        val position = holder.adapterPosition
        PlayerViewAdapter.releaseRecycledPlayer(position)
        super.onViewRecycled(holder)
    }

}