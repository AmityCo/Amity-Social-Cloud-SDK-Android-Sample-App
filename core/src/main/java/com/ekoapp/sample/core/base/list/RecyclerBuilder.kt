package com.ekoapp.sample.core.base.list

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class RecyclerBuilder(
        val context: Context,
        val recyclerView: RecyclerView,
        val spaceCount: Int) {

    fun builder(): RecyclerBuilder {
        recyclerView.apply { layoutManager = GridLayoutManager(context, spaceCount) }
        return this
    }

    fun build(setupAdapter: RecyclerView.Adapter<ViewHolder>): RecyclerBuilder {
        recyclerView.adapter = setupAdapter
        return this
    }

    fun buildMultiView(setupAdapter: RecyclerView.Adapter<BaseViewHolder<*>>): RecyclerBuilder {
        recyclerView.adapter = setupAdapter
        return this
    }

    fun smoothScrollToPosition(position: Int) {
        val delay = 100L
        Handler().postDelayed({
            recyclerView.smoothScrollToPosition(position)
        }, delay)
    }
}
