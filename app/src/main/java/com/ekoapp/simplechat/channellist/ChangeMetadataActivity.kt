package com.ekoapp.simplechat.channellist

import android.os.Bundle
import com.ekoapp.simplechat.KeyValueInputActivity

class ChangeMetadataActivity : KeyValueInputActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Change Metadata"
    }

    override fun onButtonClick() {
        // Coming soon in version 2.6
        finish()
    }

}