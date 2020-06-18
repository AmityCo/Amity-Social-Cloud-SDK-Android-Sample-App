package com.ekoapp.sample.core.base.list

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val UPPERMOST = 0
const val SINGLE_SPACE = 1
const val DOUBLE_SPACE = 2
const val TRIPLE_SPACE = 3

const val FLASH_SCROLL = 100L
const val IMMEDIATELY_SCROLL = 500L

data class RecyclerBuilder(
        val context: Context,
        val recyclerView: RecyclerView,
        val spaceCount: Int = SINGLE_SPACE) {

    fun builder(): RecyclerBuilder {
        recyclerView.apply { layoutManager = GridLayoutManager(context, spaceCount) }
        return this
    }

    fun stackFromEnd(stackFromEnd: Boolean): RecyclerBuilder {
        recyclerView.apply {
            setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            linearLayoutManager.stackFromEnd = stackFromEnd
            linearLayoutManager.reverseLayout = false
            layoutManager = linearLayoutManager
        }
        return this
    }

    fun build(setupAdapter: RecyclerView.Adapter<BaseViewHolder<*>>): RecyclerBuilder {
        recyclerView.adapter = setupAdapter
        return this
    }

    fun smoothScrollToPosition(delay: Long = FLASH_SCROLL, position: Int = UPPERMOST) {
        Handler().postDelayed({
            recyclerView.smoothScrollToPosition(position)
        }, delay)
    }
}
