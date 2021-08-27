package com.example.holofytask.utils

import com.google.android.exoplayer2.Player

interface PlayerStateCallBack {

    fun onVideoBuffering(player: Player)
    fun onStartPlaying(player: Player)
    fun onFinishedPlaying(player: Player)
}