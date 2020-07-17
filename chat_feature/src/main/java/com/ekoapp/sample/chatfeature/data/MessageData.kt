package com.ekoapp.sample.chatfeature.data

import android.net.Uri
import com.ekoapp.sample.core.data.Metadata

data class MessageData(val text: String? = null,
                       val image: Uri? = null,
                       val file: Uri? = null,
                       val custom: List<Metadata>? = null)