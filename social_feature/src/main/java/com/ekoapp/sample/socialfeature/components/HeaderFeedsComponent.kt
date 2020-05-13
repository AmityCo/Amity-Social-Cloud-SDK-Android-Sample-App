package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.utils.getTimeAgo
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.dialogs.FeedsMoreHorizBottomSheetFragment
import kotlinx.android.synthetic.main.component_header_feeds.view.*


class HeaderFeedsComponent : ConstraintLayout {

    private var feedsMoreHorizBottomSheet: FeedsMoreHorizBottomSheetFragment

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_feeds, this, true)
        feedsMoreHorizBottomSheet = FeedsMoreHorizBottomSheetFragment()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {
        text_full_name.text = item.postedUserId
        text_time.text = item.createdAt.toDate().getTimeAgo()
        image_more_horiz.setOnClickListener { context.renderBottomSheet() }
    }

    private fun Context.renderBottomSheet() {
        feedsMoreHorizBottomSheet.show((this as AppCompatActivity).supportFragmentManager, feedsMoreHorizBottomSheet.tag)
    }

    fun editFeeds(edit: (Boolean) -> Unit) {
        feedsMoreHorizBottomSheet.renderEdit {
            edit.invoke(it)
            feedsMoreHorizBottomSheet.dialog?.cancel()
        }
    }

    fun deleteFeeds(delete: (Boolean) -> Unit) {
        feedsMoreHorizBottomSheet.renderDelete {
            delete.invoke(it)
            feedsMoreHorizBottomSheet.dialog?.cancel()
        }
    }
}