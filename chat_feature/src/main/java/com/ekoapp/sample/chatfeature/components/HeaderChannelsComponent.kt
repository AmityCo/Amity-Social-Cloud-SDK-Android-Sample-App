package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.dialogs.SelectChannelBottomSheetFragment
import kotlinx.android.synthetic.main.component_header_channels.view.*

class HeaderChannelsComponent : ConstraintLayout {
    private var selectChannelBottomSheet: SelectChannelBottomSheetFragment = SelectChannelBottomSheetFragment()

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_channels, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(total: Int) {
        avatar_with_total.setupView(total)
        image_create.setOnClickListener { context.renderBottomSheet() }
    }

    private fun Context.renderBottomSheet() {
        selectChannelBottomSheet.show((this as AppCompatActivity).supportFragmentManager, selectChannelBottomSheet.tag)
    }

    fun createStandardChannel(standard: () -> Unit) {
        selectChannelBottomSheet.renderStandard {
            standard.invoke()
            selectChannelBottomSheet.dialog?.cancel()
        }
    }

    fun createPrivateChannel(private: () -> Unit) {
        selectChannelBottomSheet.renderPrivate {
            private.invoke()
            selectChannelBottomSheet.dialog?.cancel()
        }
    }
}