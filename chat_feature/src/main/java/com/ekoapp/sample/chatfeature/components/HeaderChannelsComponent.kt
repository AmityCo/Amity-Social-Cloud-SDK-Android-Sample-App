package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.dialogs.CreateChannelBottomSheetFragment
import kotlinx.android.synthetic.main.component_header_channels.view.*

class HeaderChannelsComponent : ConstraintLayout {
    private var createChannelBottomSheet: CreateChannelBottomSheetFragment = CreateChannelBottomSheetFragment()

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
        createChannelBottomSheet.show((this as AppCompatActivity).supportFragmentManager, createChannelBottomSheet.tag)
    }

    fun createStandardChannel(standard: () -> Unit) {
        createChannelBottomSheet.renderStandard {
            standard.invoke()
            createChannelBottomSheet.dialog?.cancel()
        }
    }

    fun createPrivateChannel(private: () -> Unit) {
        createChannelBottomSheet.renderPrivate {
            private.invoke()
            createChannelBottomSheet.dialog?.cancel()
        }
    }
}