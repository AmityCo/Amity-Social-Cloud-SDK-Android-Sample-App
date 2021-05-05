package com.amity.sample.ascsdk.channellist

enum class ChannelMembershipOption(val value: String) {

    ADD_ROLE("add role"),
    REMOVE_ROLE("remove role");

    companion object {
        fun enumOf(value: String): ChannelMembershipOption? = values().find { it.value == value }
    }
}