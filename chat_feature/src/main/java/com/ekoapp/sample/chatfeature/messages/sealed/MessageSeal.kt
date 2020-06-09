package com.ekoapp.sample.chatfeature.messages.sealed

import com.ekoapp.ekosdk.EkoMessage

sealed class MessageSealed {
    class Text(val item: EkoMessage) : MessageSealed()
    class Image(val item: EkoMessage) : MessageSealed()
    class File(val item: EkoMessage) : MessageSealed()
    class Custom(val item: EkoMessage) : MessageSealed()
}