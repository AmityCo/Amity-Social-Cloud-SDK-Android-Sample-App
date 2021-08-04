package com.amity.sample.ascsdk.post.create

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.sample.ascsdk.intent.OpenCreateImagePostIntent
import com.amity.sample.ascsdk.intent.OpenCreateTextPostIntent
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_post_create.*

class CreateTextPostActivity : CreatePostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button_add.visibility = View.GONE
        text.addTextChangedListener {
            button_post.isEnabled = !it.isNullOrEmpty()
        }

    }

    override fun post() {
        createTextPost().subscribe()
        finish()
    }

    private fun createTextPost(): Single<AmityPost> {
        when {
            OpenCreateTextPostIntent.getTargetType(intent) == "community" -> {
                return AmitySocialClient.newPostRepository()
                        .createPost()
                        .targetCommunity(OpenCreateTextPostIntent.getTargetId(intent))
                        .text(text.text.toString())
                        .build()
                        .post()

            }
            OpenCreateImagePostIntent.getTargetType(intent) == "myUser" -> {
                return AmitySocialClient.newPostRepository()
                        .createPost()
                        .targetMe()
                        .text(text.text.toString())
                        .build()
                        .post()
            }
            else -> {
                return AmitySocialClient.newPostRepository()
                        .createPost()
                        .targetUser(OpenCreateTextPostIntent.getTargetId(intent))
                        .text(text.text.toString())
                        .build()
                        .post()
            }
        }
    }

}