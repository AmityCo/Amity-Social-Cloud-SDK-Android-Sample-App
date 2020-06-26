package com.ekoapp.sample.core.seals

import com.ekoapp.ekosdk.EkoPost

sealed class ReportSealType {
    class FLAG(val item: EkoPost) : ReportSealType()
    class UNFLAG(val item: EkoPost) : ReportSealType()
}