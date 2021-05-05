package com.amity.sample.ascsdk.community.update

import android.os.Bundle
import android.view.View
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.community.CommunityFormActivity
import com.amity.sample.ascsdk.intent.OpenUpdateCommunityIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_community.*

class UpdateCommunityActivity : CommunityFormActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switch_community_official.visibility = View.GONE
        switch_community_only_admin.visibility = View.GONE
        textview_community_id.text = getCommunityId()
        button_submit.text = "Update Community"
        button_submit.setOnClickListener {
            updateCommunity()
        }
    }

    fun getCommunityId(): String {
        return OpenUpdateCommunityIntent.getCommunityId(intent)
    }

    private fun updateCommunity() {
        val update = communityRepository
                .updateCommunity(getCommunityId())
                .isPublic(isPublic())

        if (getDisplayName().isNotEmpty()) {
            update.displayName(getDisplayName())
        }

        update.description(getDescription())

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