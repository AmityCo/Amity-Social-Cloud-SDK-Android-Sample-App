package com.ekoapp.sample.chatfeature.messages.seals

import com.ekoapp.ekosdk.EkoMessage

sealed class MessageSealed {
    class Text(val item: EkoMessage) : MessageSealed()
    class Image(val item: EkoMessage) : MessageSealed()
    class File(val item: EkoMessage) : MessageSealed()
    class Custom(val item: EkoMessage) : MessageSealed()
}