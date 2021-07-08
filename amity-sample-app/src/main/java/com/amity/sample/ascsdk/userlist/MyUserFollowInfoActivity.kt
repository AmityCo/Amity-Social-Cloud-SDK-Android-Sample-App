package com.amity.sample.ascsdk.userlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenMyUserFollowerListIntent
import com.amity.sample.ascsdk.intent.OpenMyUserFollowingListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowStatusFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_follow_info.*

class MyUserFollowInfoActivity : AppCompatActivity() {
    private var disposable: Disposable? = null

    private val userRepository = AmityCoreClient.newUserRepository()

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_my_follow_info)

        initListener()
        observeMyFollowInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    private fun observeMyFollowInfo() {
        disposable = userRepository.relationship()
                .me()
                .getFollowInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    following_button.text = "Followings: ${it.getFollowingCount()}"
                    follower_button.text = "Followers: ${it.getFollowerCount()}"
                    pending_request_button.text = "Pending request: ${it.getPendingRequestCount()}"
                    request_sent_button.text = "Request sent "

                    following_button.visibility = View.VISIBLE
                    follower_button.visibility = View.VISIBLE
                    pending_request_button.visibility = View.VISIBLE
                    request_sent_button.visibility = View.VISIBLE
                }, {
                    following_button.visibility = View.GONE
                    follower_button.visibility = View.GONE
                    pending_request_button.visibility = View.GONE
                    request_sent_button.visibility = View.GONE
                    showToast("Get Follow Information: Failed")
                })
    }

    private fun initListener() {
        following_button.setOnClickListener {
            startActivity(OpenMyUserFollowingListIntent(this, AmityFollowStatusFilter.ACCEPTED.apiKey))
        }

        follower_button.setOnClickListener {
            startActivity(OpenMyUserFollowerListIntent(this, AmityFollowStatusFilter.ACCEPTED.apiKey))
        }

        request_sent_button.setOnClickListener {
            startActivity(OpenMyUserFollowingListIntent(this, AmityFollowStatusFilter.PENDING.apiKey))
        }

        pending_request_button.setOnClickListener {
            startActivity(OpenMyUserFollowerListIntent(this, AmityFollowStatusFilter.PENDING.apiKey))
        }
    }
}


