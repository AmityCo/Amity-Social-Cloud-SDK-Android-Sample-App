package com.ekoapp.sdk.community.update

import android.os.Bundle
import android.view.View
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.intent.OpenUpdateCommunityIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_community.*

class UpdateCommunityActivity : UpdateCommunityByAdminActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switch_community_official.visibility = View.GONE
        switch_community_only_admin.visibility = View.GONE
    }

    override fun getCommunityId(): String {
        return OpenUpdateCommunityIntent.getCommunityId(intent)
    }

    override fun updateCommunity() {
        val update = communityRepository
                .updateCommunity(getCommunityId())
                .isPublic(isPublic())

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
                    showToast(message = "Update failed !")
                })
        compositeDisposable.add(disposable)
    }

}