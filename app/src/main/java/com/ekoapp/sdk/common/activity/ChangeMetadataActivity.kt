package com.ekoapp.sdk.common.activity

import android.os.Bundle
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.user.EkoUser
import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    private fun createRequest(): Single<EkoUser> {
        val data = JsonObject()
        data.addProperty(key_edittext.text.toString().trim(), value_edittext.text.toString().trim());
        return EkoClient.updateUser()
                .metadata(metadata = data)
                .build()
                .update()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}