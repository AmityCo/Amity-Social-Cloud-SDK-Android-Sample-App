package com.ekoapp.sample.chatfeature.intents

import android.content.Intent
import androidx.fragment.app.Fragment
import com.ekoapp.sample.chatfeature.constants.REQUEST_CODE_CHANNEL_SETTINGS
import com.ekoapp.sample.chatfeature.settings.ChannelSettingsActivity

fun Fragment.openChannelSettingsPage() {
    val intent = Intent(requireContext(), ChannelSettingsActivity::class.java)
    startActivityForResult(intent, REQUEST_CODE_CHANNEL_SETTINGS)
}