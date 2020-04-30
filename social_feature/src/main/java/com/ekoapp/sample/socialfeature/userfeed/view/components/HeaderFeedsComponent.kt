package com.ekoapp.sample.socialfeature.userfeed.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.dialogs.MoreHorizBottomSheetFragment
import kotlinx.android.synthetic.main.component_header_feeds.view.*


class HeaderFeedsComponent : ConstraintLayout {

    private var moreHorizBottomSheet: MoreHorizBottomSheetFragment

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_feeds, this, true)
        moreHorizBottomSheet = MoreHorizBottomSheetFragment()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(data: SampleFeedsResponse) {
        text_full_name.text = data.creator
        image_more_horiz.setOnClickListener { context.renderBottomSheet() }
    }

    private fun Context.renderBottomSheet() {
        moreHorizBottomSheet.show((this as AppCompatActivity).supportFragmentManager, moreHorizBottomSheet.tag)
    }

    fun editFeeds(edit: (Boolean) -> Unit) {
        moreHorizBottomSheet.renderEdit(edit::invoke)
    }

    fun deleteFeeds(delete: (Boolean) -> Unit) {
        moreHorizBottomSheet.renderDelete {
            delete.invoke(it)
            moreHorizBottomSheet.dialog?.cancel()
        }
    }

}