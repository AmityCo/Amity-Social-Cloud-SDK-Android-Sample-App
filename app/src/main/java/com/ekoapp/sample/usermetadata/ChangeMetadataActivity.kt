package com.ekoapp.sample.usermetadata

import android.os.Bundle
import com.ekoapp.ekosdk.EkoClient
import com.google.gson.JsonObject
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_key_value_input.*

class ChangeMetadataActivity : KeyValueInputActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Change Metadata"
    }

    override fun onButtonClick() {
        send_button.isEnabled = false
        changeMetadata()
        finish()
    }

    private fun changeMetadata() {
        val request = createRequest()
        request.subscribe()
    }

    private fun createRequest(): Completable {
        val data = JsonObject();
        data.addProperty(key_edittext.text.toString().trim(), value_edittext.text.toString().trim());
        return EkoClient.setUserMetadata(data)
    }

}