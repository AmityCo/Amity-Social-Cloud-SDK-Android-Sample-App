package com.amity.sample.ascsdk.post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenSinglePostActivityIntent
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import kotlinx.android.synthetic.main.item_post.*

class SinglePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_post)

        val post = OpenSinglePostActivityIntent.getPost(intent)
        val item = AmitySocialClient.newFeedRepository().getPost(postId = post.getPostId()).blockingFirst()

        item.let {
            val postData = it.getData()
            if (postData !is AmityPost.Data.TEXT) {
                post_textview.text = "incorrect type"
                return
            }

            post_textview.text = String.format("postId: %s\npostDataType: %s", item.getPostId(), it.getDataType().apiKey)
            sender_textview.text = String.format("userId: %s", item.getPostedUser()?.getUserId())
            data_textview.text = String.format("text: %s, children count: %s", postData.getText(), it.getChildren().size)
            deleted_textview.text = String.format("deleted: %s", it.isDeleted())
            var childrenData = ""
            for (child in it.getChildren()) {
                childrenData += "====================\n"
                childrenData += String.format("postId: %s\npostDataType: %s\n", child.getPostId(), child.getDataType().apiKey)
                childrenData += String.format("userId: %s\n", child.getPostedUser()?.getUserId())
            }
            childrenData += "====================\n"
            data_children.text = childrenData
            reaction_textview.text = String.format("reactionCount: %s\nmyReactions: %s", it.getReactionCount(), it.getMyReactions())
            comment_count_textview.text = String.format("commentCount: %s", it.getCommentCount())
            time_textview.text = String.format("created: %s", it.getCreatedAt())
        }

    }
}