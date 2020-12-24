package com.ekoapp.sdk.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showToast
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_community.*
import org.json.JSONException

open class CommunityFormActivity : AppCompatActivity() {

    val compositeDisposable = CompositeDisposable()
    val communityRepository = EkoClient.newCommunityRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_community)
    }

    fun getDisplayName(): String {
        return edittext_display_name.text.toString()
    }

    fun getDescription(): String {
        return edittext_description.text.toString()
    }

    fun isPublic(): Boolean {
        return switch_community_type.isChecked
    }

    fun isOfficial(): Boolean {
        return switch_community_type.isChecked
    }

    fun onlyAdminCanPost(): Boolean {
        return switch_community_type.isChecked
    }

    fun getCategoryIds(): List<String> {
        return edittext_categoryIds.text.toString().split(",")
                .filter { it.isNotEmpty() }
    }

    fun getMetaData(): JsonObject {
        return try {
            val jsonString = edittext_meta_data.text.toString()
            val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)
            jsonObject?.let { jsonObjectNonNull ->
                jsonObjectNonNull
            } ?: kotlin.run {
                JsonObject()
            }
        } catch (exception: JSONException) {
            showToast("Meta Data is incorrect !")
            JsonObject()
        }
    }

    fun getUserIds(): List<String> {
        return edittext_user_ids.text.toString().split(",")
                .filter { it.isNotEmpty() }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}