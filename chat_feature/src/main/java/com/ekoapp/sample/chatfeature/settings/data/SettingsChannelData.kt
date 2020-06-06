package com.ekoapp.sample.chatfeature.settings.data

import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.ekosdk.EkoTags
import com.google.common.collect.ImmutableSet

data class SettingsChannelData(val types: ImmutableSet<EkoChannel.Type>,
                               val filter: EkoChannelFilter,
                               val includingTags: EkoTags,
                               val excludingTags: EkoTags)