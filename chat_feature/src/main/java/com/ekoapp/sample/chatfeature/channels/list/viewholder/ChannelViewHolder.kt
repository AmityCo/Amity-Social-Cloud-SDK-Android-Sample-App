package com.ekoapp.sample.chatfeature.channels.list.viewholder

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.sample.chatfeature.channels.list.renders.EkoChannelsRenderData
import com.ekoapp.sample.chatfeature.channels.list.renders.channelRender
import com.ekoapp.sample.chatfeature.dialogs.AboutChannelBottomSheetFragment
import com.ekoapp.sample.chatfeature.dialogs.ChannelsMoreHorizBottomSheetFragment
import com.ekoapp.sample.core.base.list.BaseViewHolder
import kotlinx.android.synthetic.main.item_channel.view.*

class ChannelViewHolder(itemView: View) : BaseViewHolder<EkoChannel>(itemView) {
    private var channelsHorizBottomSheet: ChannelsMoreHorizBottomSheetFragment = ChannelsMoreHorizBottomSheetFragment()

    override fun bind(item: EkoChannel) {
        val context = itemView.context
        EkoChannelsRenderData(context, item).channelRender(
                itemView.avatar_chat_room_with_total,
                itemView.image_chat_room_type,
                itemView.text_chat_room_name,
                itemView.text_chat_room_member
        )

        itemView.image_more_horiz.setOnClickListener {
            context.renderBottomSheet()
        }
    }

    private fun Context.renderBottomSheet() {
        channelsHorizBottomSheet.show((this as AppCompatActivity).supportFragmentManager, channelsHorizBottomSheet.tag)
    }

    fun joinChannel(join: (Boolean) -> Unit) {
        channelsHorizBottomSheet.renderJoin {
            join.invoke(it)
            channelsHorizBottomSheet.dialog?.cancel()
        }
    }

    fun aboutChannel(context: Context, items: ArrayList<String>) {
        channelsHorizBottomSheet.renderAbout {
            val aboutChannelBottomSheet = AboutChannelBottomSheetFragment(items)
            aboutChannelBottomSheet.show((context as AppCompatActivity).supportFragmentManager, aboutChannelBottomSheet.tag)
            channelsHorizBottomSheet.dialog?.cancel()
        }
    }
}