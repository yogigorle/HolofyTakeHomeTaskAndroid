package com.example.holofytask.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

    var firstVisibleItem = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
        onItemVisible(firstVisibleItem)
    }

    abstract fun onItemVisible(index: Int)
}