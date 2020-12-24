package com.ekoapp.sdk.community.update

import android.os.Bundle
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.community.CommunityFormActivity
import com.ekoapp.sdk.intent.OpenUpdateCommunityIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_community.*

open class UpdateCommunityByAdminActivity : CommunityFormActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textview_community_id.text = getCommunityId()
        button_submit.text = "Update Community"
        button_submit.setOnClickListener {
            updateCommunity()
        }
    }

    open fun getCommunityId(): String {
        return OpenUpdateCommunityIntent.getCommunityId(intent)
    }

    open fun updateCommunity() {
        val update = communityRepository
                .updateCommunityByAdmin(getCommunityId())
                .isPublic(isPublic())
                .isOfficial(isOfficial())
                .onlyAdminCanPost(onlyAdminCanPost())

        if (getDisplayName().isNotEmpty()) {
            update.displayName(getDisplayName())
        }
        if (getDescription().isNotEmpty()) {
            update.description(getDescription())
        }
        if (getMetaData().size() > 0) {
            update.metadata(getMetaData())
        }
        if (getCategoryIds().isNotEmpty()) {
            update.categoryIds(getCategoryIds())
        }

        val disposable = update.build()
                .update()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    finish()
                }, {
                    showToast(message = "Update by admin failed !")
                })
        compositeDisposable.add(disposable)
    }

}