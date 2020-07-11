package com.ekoapp.sample.chatfeature.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ekoapp.sample.chatfeature.constants.EXTRA_CHANNEL_MESSAGES
import com.ekoapp.sample.chatfeature.constants.EXTRA_REPLY_MESSAGES
import com.ekoapp.sample.chatfeature.constants.REQUEST_CODE_CHANNEL_SETTINGS
import com.ekoapp.sample.chatfeature.constants.REQUEST_CODE_MESSAGES
import com.ekoapp.sample.chatfeature.data.ChannelData
import com.ekoapp.sample.chatfeature.data.MessageData
import com.ekoapp.sample.chatfeature.membership.view.MembershipActivity
import com.ekoapp.sample.chatfeature.messages.view.MessagesActivity
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsActivity

fun Fragment.openChannelSettingsPage() {
    val intent = Intent(requireContext(), ChannelSettingsActivity::class.java)
    startActivityForResult(intent, REQUEST_CODE_CHANNEL_SETTINGS)
}

fun Fragment.openMessagesPage(data: ChannelData) {
    val intent = Intent(requireContext(), MessagesActivity::class.java)
    intent.putExtra(EXTRA_CHANNEL_MESSAGES, data)
    startActivityForResult(intent, REQUEST_CODE_MESSAGES)
}

fun AppCompatActivity.openReplyMessagesPage(data: MessageData?) {
    val intent = Intent(this, MessagesActivity::class.java)
    intent.putExtra(EXTRA_REPLY_MESSAGES, data)
    startActivityForResult(intent, REQUEST_CODE_MESSAGES)
}

fun AppCompatActivity.openMembershipPage(data: ChannelData) {
    val intent = Intent(this, MembershipActivity::class.java)
    intent.putExtra(EXTRA_CHANNEL_MESSAGES, data)
    startActivityForResult(intent, REQUEST_CODE_MESSAGES)
}
