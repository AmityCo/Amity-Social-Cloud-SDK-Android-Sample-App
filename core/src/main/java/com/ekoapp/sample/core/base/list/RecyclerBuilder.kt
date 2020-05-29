package com.ekoapp.sample.core.base.list

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val UPPERMOST = 0
const val SINGLE_SPACE = 1
const val TRIPLE_SPACE = 3

data class RecyclerBuilder(
        val context: Context,
        val recyclerView: RecyclerView,
        val spaceCount: Int = SINGLE_SPACE) {

    fun builder(): RecyclerBuilder {
        recyclerView.apply { layoutManager = GridLayoutManager(context, spaceCount) }
        return this
    }

    fun build(setupAdapter: RecyclerView.Adapter<BaseViewHolder<*>>): RecyclerBuilder {
        recyclerView.adapter = setupAdapter
        return this
    }

    fun smoothScrollToPosition(position: Int = UPPERMOST) {
        val delay = 100L
        Handler().postDelayed({
            recyclerView.smoothScrollToPosition(position)
        }, delay)
    }
}
