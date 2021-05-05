package com.amity.sample.ascsdk.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_community.*

open class CommunityFormActivity : AppCompatActivity() {

    val compositeDisposable = CompositeDisposable()
    val communityRepository = AmitySocialClient.newCommunityRepository()

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


    fun getCategoryIds(): List<String> {
        return edittext_categoryIds.text.toString().split(",")
                .filter { it.isNotEmpty() }
    }

    fun getMetaData(): JsonObject {
        return try {
            val jsonString = edittext_meta_data.text.toString()
            Gson().fromJson(jsonString, JsonObject::class.java)
        } catch (exception: Exception) {
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