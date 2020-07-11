package com.ekoapp.sample.core.seals

import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.ekosdk.EkoUser

sealed class ReportPostSealType {
    class FLAG(val item: EkoPost) : ReportPostSealType()
    class UNFLAG(val item: EkoPost) : ReportPostSealType()
}

sealed class ReportMessageSealType {
    class FLAG(val item: EkoMessage) : ReportMessageSealType()
    class UNFLAG(val item: EkoMessage) : ReportMessageSealType()
}

sealed class ReportSenderSealType {
    class FLAG(val item: EkoUser) : ReportSenderSealType()
    class UNFLAG(val item: EkoUser) : ReportSenderSealType()
}