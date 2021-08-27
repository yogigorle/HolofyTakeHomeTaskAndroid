package com.example.holofytask.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.example.holofytask.R

class LoadImageBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["image_url"], requireAll = false)
        fun loadImage(imgView: ImageView, profileImage: String?) {
            if (!profileImage.isNullOrEmpty()) {
                imgView.load(profileImage) {
                    placeholder(R.drawable.video_placeholder)
                    diskCachePolicy(CachePolicy.READ_ONLY)
                }
            }
        }
    }
}