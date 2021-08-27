package com.example.holofytask.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoModel(

    val videoTitle: String = "",
    val videoUrl: String = "",
    val videoThumbnail: String = "",
    val videoSubTitle: String = "",
    val content: String = "",
    var currentPosition: Long = 0L,
    var currentWindowIndex: Int = 0,
    var bool: Boolean = false,

):Parcelable{

}