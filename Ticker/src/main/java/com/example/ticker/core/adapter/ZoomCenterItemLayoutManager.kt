package com.example.ticker.core.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ZoomCenterItemLayoutManager(private val context: Context) : LinearLayoutManager(context) {

    private val shrinkAmount = 0.8f
    private val shrinkDistance = 0.9f

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scrollVerticallyBy(0, recycler, state)
    }
}