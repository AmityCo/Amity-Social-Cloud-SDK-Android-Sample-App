package com.ekoapp.sdk.post.create

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.feed.EkoPost
import com.ekoapp.sdk.intent.OpenCreateImagePostIntent
import com.ekoapp.sdk.intent.OpenCreateTextPostIntent
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

    private fun createTextPost(): Single<EkoPost> {
        when {
            OpenCreateTextPostIntent.getTargetType(intent) == "community" -> {
                return EkoClient.newFeedRepository()
                        .createPost()
                        .targetCommunity(OpenCreateTextPostIntent.getTargetId(intent))
                        .text(text.text.toString())
                        .build()
                        .post()

            }
            OpenCreateImagePostIntent.getTargetType(intent) == "myUser" -> {
                return EkoClient.newFeedRepository()
                        .createPost()
                        .targetMe()
                        .text(text.text.toString())
                        .build()
                        .post()
            }
            else -> {
                return EkoClient.newFeedRepository()
                        .createPost()
                        .targetUser(OpenCreateTextPostIntent.getTargetId(intent))
                        .text(text.text.toString())
                        .build()
                        .post()
            }
        }
    }

}