package com.ekoapp.sample.chatfeature.data

data class ReplyingStateData(val channelId: String,
                             val parentId: String? = null,
                             val isNotCancel: Boolean = false,
                             val isReplyPage: Boolean)