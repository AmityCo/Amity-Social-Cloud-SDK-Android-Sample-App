package com.amity.sample.ascsdk.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_community.*

interface CommunityContractor {
    fun getCommunityId():String? = null
    fun getCommunity(communityId: String){}
}
open class CommunityFormActivity : AppCompatActivity(), CommunityContractor {
    val compositeDisposable = CompositeDisposable()
    val communityRepository = AmitySocialClient.newCommunityRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_community)
        getCommunityId()?.let(this::getCommunity)
    }
    override fun getCommunity(communityId: String){
        val observeCommunity = communityRepository.getCommunity(communityId)
        val observeMembership = communityRepository.membership(communityId)
            .getMembers()
            .build()
            .query()
    
        compositeDisposable.add(Flowable.zip(
            observeCommunity,
            observeMembership,
            BiFunction<AmityCommunity, PagedList<AmityCommunityMember>, Boolean>
            { community, membership ->
                setupView(community, membership)
                true
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }
    
    private fun setupView(community: AmityCommunity, communityMember: PagedList<AmityCommunityMember>) {
        edittext_display_name.setText(community.getDisplayName())
        edittext_description.setText(community.getDescription())
        val categories = community.getCategories().joinToString(separator = ",") { it.getCategoryId() }
        edittext_categoryIds.setText(categories)
        edittext_meta_data.setText(community.getMetadata().toString())
        val membership = communityMember.joinToString(separator = ",") { it.getUserId() }
        edittext_user_ids.setText(membership)
        switch_community_type.isChecked = community.isPublic()
        switch_community_post_review.isChecked = community.isPostReviewEnabled()
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
    
    fun isPostReviewEnabled():Boolean {
        return switch_community_post_review.isChecked
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