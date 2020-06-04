package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.dialogs.CreateChannelBottomSheetFragment
import com.ekoapp.sample.chatfeature.dialogs.SelectChannelBottomSheetFragment
import com.ekoapp.sample.chatfeature.enums.ChannelTypes
import kotlinx.android.synthetic.main.component_header_channels.view.*

data class CreateChannelData(val id: String, val type: String)
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

    fun createStandardChannel(fm: FragmentManager, action: (CreateChannelData) -> Unit) {
        selectChannelBottomSheet.renderStandard {
            createChannel(fm, ChannelTypes.STANDARD, action)
            selectChannelBottomSheet.dialog?.cancel()
        }
    }

    fun createPrivateChannel(fm: FragmentManager, action: (CreateChannelData) -> Unit) {
        selectChannelBottomSheet.renderPrivate {
            createChannel(fm, ChannelTypes.PRIVATE, action)
            selectChannelBottomSheet.dialog?.cancel()
        }
    }

    private fun createChannel(fm: FragmentManager, type: ChannelTypes, action: (CreateChannelData) -> Unit) {
        val createChannelBottomSheet = CreateChannelBottomSheetFragment(String.format(context.getString(R.string.temporarily_create_channel_display), type.text))
        createChannelBottomSheet.show(fm, createChannelBottomSheet.tag)
        createChannelBottomSheet.renderOk {
            action.invoke(CreateChannelData(id = it, type = type.text))
            createChannelBottomSheet.dialog?.cancel()
        }
    }
}