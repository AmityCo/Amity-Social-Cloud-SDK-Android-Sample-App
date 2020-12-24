package com.ekoapp.sdk.community.create

import android.os.Bundle
import android.view.View
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.community.CommunityFormActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_community.*

open class CreateCommunityByAdminActivity : CommunityFormActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textview_community_id.visibility = View.GONE
        button_submit.setOnClickListener {
            createCommunity()
        }
    }

    open fun createCommunity() {
        val creator = communityRepository
                .createCommunityByAdmin(getDisplayName())
                .isPublic(isPublic())
                .isOfficial(isOfficial())
                .onlyAdminCanPost(onlyAdminCanPost())

        if (getDescription().isNotEmpty()) {
            creator.description(getDescription())
        }
        if (getMetaData().size() > 0) {
            creator.metadata(getMetaData())
        }
        if (getCategoryIds().isNotEmpty()) {
            creator.categoryIds(getCategoryIds())
        }
        if (getUserIds().isNotEmpty()) {
            creator.userIds(getUserIds())
        }

        val disposable = creator.build()
                .create()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    finish()
                }, {
                    showToast(message = "Create by admin failed !")
                })
        compositeDisposable.add(disposable)
    }

}