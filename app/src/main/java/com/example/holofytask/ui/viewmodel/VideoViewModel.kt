package com.example.holofytask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.holofytask.data.models.VideoModel
import com.example.holofytask.data.repositories.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    var videoRepo: VideoRepository
) : ViewModel() {

    private val _mediaModel: MutableLiveData<VideoModel> = MutableLiveData()
    val mediaModel: LiveData<VideoModel> = _mediaModel

    fun getVideoData(): LiveData<List<VideoModel>> {
        return videoRepo.loadData()
    }


    fun putMediaData(windowIndex: Int, currentPos: Long, bool: Boolean = false) {

        _mediaModel.value =
            VideoModel(currentWindowIndex = windowIndex, currentPosition = currentPos, bool = bool)

    }
}