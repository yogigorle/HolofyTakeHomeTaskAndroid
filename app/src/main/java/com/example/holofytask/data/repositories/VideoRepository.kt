package com.example.holofytask.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.holofytask.HolofyApplication
import com.example.holofytask.R
import com.example.holofytask.data.models.VideoModel

class VideoRepository {

    fun loadData(): LiveData<List<VideoModel>> {
        val response: MutableLiveData<List<VideoModel>> = MutableLiveData()

        val videosList = listOf<VideoModel>(
            VideoModel(
                "Big Buck Bunny",
                "https://static.klliq.com/videos/Nca_afaqR15IQIrEDyhH9zEtoPSycv_z_hd.mp4",
                "https://static.klliq.com/thumbnails/ZnFAHzGD9RQrRsBjJt2Pv3Y1vIAo11FX.png",
                "Big Buck Bunny tells the story of a giant",
                content = HolofyApplication.context.getString(R.string.video_content)
            ),
            VideoModel(
                "Elephant Dream",
                "https://static.klliq.com/videos/QMWR5PxqxnnAILvO8iGB5ygvV47wxoDK_hd.mp4",
                "https://static.klliq.com/thumbnails/vGRpl-Xw45xfOCborXr3bwAsl0uu_qMA.png",
                "The first Blender Open Movie from 2006",
                content = HolofyApplication.context.getString(R.string.video_content)
            ),
            VideoModel(
                "For Bigger Blazes",
                "https://static.klliq.com/videos/EJUhFO-_YQkH_Ll6tPppf2EkR794aTQQ_hd.mp4",
                "https://static.klliq.com/thumbnails/5a7Byj0r5ZIKC0gV9QWCneZQZEmKCP-B.png",
                "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV.",
                content = HolofyApplication.context.getString(R.string.video_content)
            ),
            VideoModel(
                "For Bigger Escape",
                "https://static.klliq.com/videos/QMWR5PxqxnnAILvO8iGB5ygvV47wxoDK_hd.mp4",
                "https://static.klliq.com/thumbnails/vGRpl-Xw45xfOCborXr3bwAsl0uu_qMA.png",
                "Introducing Chromecast. The easiest way to enjoy online video and music on your",
                content = HolofyApplication.context.getString(R.string.video_content)
            ),
            VideoModel(
                "For Bigger Fun",
                "https://static.klliq.com/videos/Nca_afaqR15IQIrEDyhH9zEtoPSycv_z_hd.mp4",
                "https://static.klliq.com/thumbnails/ZnFAHzGD9RQrRsBjJt2Pv3Y1vIAo11FX.png",
                "Introducing Chromecast. The easiest way to enjoy online video and music on your TV.",
                content = HolofyApplication.context.getString(R.string.video_content)
            )
        )

        response.value = videosList

        return response
    }

}