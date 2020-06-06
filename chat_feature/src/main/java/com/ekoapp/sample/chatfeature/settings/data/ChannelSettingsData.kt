package com.ekoapp.sample.chatfeature.settings.data

data class ChannelSettingsData(val types: Set<String>,
                               val filter: String,
                               val includingTags: Set<String>,
                               val excludingTags: Set<String>)