package com.amity.sample.ascsdk.community.create

import android.os.Bundle
import android.view.View
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.community.CommunityFormActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_community.*

class CreateCommunityActivity : CommunityFormActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switch_community_official.visibility = View.GONE
        switch_community_only_admin.visibility = View.GONE
        textview_community_id.visibility = View.GONE
        button_submit.setOnClickListener {
            createCommunity()
        }
    }

    private fun createCommunity() {
        val creator = communityRepository
                .createCommunity(getDisplayName())
                .isPublic(isPublic())

        if (getDescription().isNotEmpty()) {
            creator.description(getDescription())
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
                    showToast(message = "Create failed !")
                })
        compositeDisposable.add(disposable)
    }

}