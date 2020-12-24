package com.ekoapp.sdk.post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.feed.EkoPost
import com.ekoapp.sdk.R
import com.ekoapp.sdk.intent.OpenSinglePostActivityIntent
import kotlinx.android.synthetic.main.item_post.*

class SinglePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_post)

        val post = OpenSinglePostActivityIntent.getPost(intent)
        val item = EkoClient.newFeedRepository().getPost(postId = post.getPostId()).blockingFirst()

        item.let {
            val postData = it.getData()
            if (postData !is EkoPost.Data.TEXT) {
                post_textview.text = "incorrect type"
                return
            }
            post_textview.text = String.format("postId: %s\npostDataType: %s", item.getPostId(), it.getDataType().apiKey)
            sender_textview.text = String.format("userId: %s", item.getPostedUserId())
            data_textview.text = String.format("text: %s, children count: %s", postData.getText(), it.getChildren().size)
            deleted_textview.text = String.format("deleted: %s", it.isDeleted())
            var childrenData = ""
            for (child in it.getChildren()) {
                childrenData += "====================\n"
                childrenData += String.format("postId: %s\npostDataType: %s\n", child.getPostId(), child.getDataType().apiKey)
                childrenData += String.format("userId: %s\n", child.getPostedUserId())
            }
            childrenData += "====================\n"
            data_children.text = childrenData
            reaction_textview.text = String.format("reactionCount: %s\nmyReactions: %s", it.getReactionCount(), it.getMyReactions())
            comment_count_textview.text = String.format("commentCount: %s", it.getCommentCount())
            time_textview.text = String.format("created: %s", it.getCreatedAt())
        }

    }
}