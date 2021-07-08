package com.amity.sample.ascsdk.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenSinglePostActivityIntent
import com.amity.socialcloud.sdk.core.AmityVideo
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

            var rootPost = "postId: %s\n" +
                    "target: %s\n" +
                    "feedType: %s\n" +
                    "dataType: %s\n" +
                    "data: %s\n" +
                    "myReactions: %s\n" +
                    "reactionCount: %s\n" +
                    "commentCount: %s\n" +
                    "childrenCount: %s\n" +
                    "flagCount: %s\n" +
                    "isFlaggedByMe: %s\n" +
                    "deleted: %s\n" +
                    "updatedAt: %s\n" +
                    "createdAt: %s\n"

            val childPost = "\n----------------------------\n" +
                    "   postId: %s\n" +
                    "   parentId: %s\n" +
                    "   target: %s\n" +
                    "   feedType: %s\n" +
                    "   dataType: %s\n" +
                    "   data: %s\n" +
                    "   myReactions: %s\n" +
                    "   reactionCount: %s\n" +
                    "   commentCount: %s\n" +
                    "   flagCount: %s\n" +
                    "   isFlaggedByMe: %s\n" +
                    "   deleted: %s\n" +
                    "   updatedAt: %s\n" +
                    "   createdAt: %s\n"

            var displayPost = String.format(rootPost,
                    post.getPostId(),
                    if (post.getTarget() is AmityPost.Target.COMMUNITY) "community " + (post.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()?.getCommunityId() else "user " + (post.getTarget() as AmityPost.Target.USER).getUser()?.getUserId(),
                    post.getFeedType().apiKey,
                    post.getDataType().apiKey,
                    (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: (post.getData() as? AmityPost.Data.IMAGE)?.getImage()?.getUrl() ?: (post.getData() as? AmityPost.Data.FILE)?.getFile()?.getUrl() ?: (post.getData() as? AmityPost.Data.CUSTOM)?.getRawData()?.toString() ?: (post.getData() as? AmityPost.Data.VIDEO)?.getThumbnailImage()?.getUrl() ?: "unknown",
                    post.getMyReactions(),
                    post.getReactionCount(),
                    post.getCommentCount(),
                    post.getChildren().size,
                    post.getFlagCount(),
                    post.isFlaggedByMe,
                    post.isDeleted(),
                    post.getUpdatedAt(),
                    post.getCreatedAt())

            post.getChildren().forEach {
                displayPost += String.format(childPost,
                        it.getPostId(),
                        it.getParentPostId(),
                        if (it.getTarget() is AmityPost.Target.COMMUNITY) "community " + (it.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()?.getCommunityId() else "user " + (it.getTarget() as AmityPost.Target.USER).getUser()?.getUserId(),
                        it.getFeedType().apiKey,
                        it.getDataType().apiKey,
                        (it.getData() as? AmityPost.Data.TEXT)?.getText(),
                        it.getMyReactions(),
                        it.getReactionCount(),
                        it.getCommentCount(),
                        it.getFlagCount(),
                        it.isFlaggedByMe,
                        it.isDeleted(),
                        it.getUpdatedAt(),
                        it.getCreatedAt())
            }
            
            post_textview.text = displayPost
            post_textview.setOnClickListener {
                val child = post.getChildren().firstOrNull()
                child?.let {
                    if(it.getData() is AmityPost.Data.VIDEO) {
                        showVideoChildrenPostList(post.getChildren())
                    }
                }
            }
        }
    }

    private fun openVideo(videoData: AmityPost.Data.VIDEO, quality: AmityVideo.Quality) {
        videoData.getVideo(quality)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { video ->
                    val playVideo = Intent(Intent.ACTION_VIEW)
                    playVideo.setDataAndType(Uri.parse(video.getUrl()), "video/mp4")
                    startActivity(playVideo)
                }
    }


    private fun showVideoChildrenPostList(videoPosts: List<AmityPost>) {
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this)
        builderSingle.setTitle("Select children posts")
        builderSingle.setCancelable(true)
        builderSingle.setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
        videoPosts.forEachIndexed { index, post ->
            val number = index + 1
            arrayAdapter.add("\n$number : " + post.getPostId())
        }
        builderSingle.setAdapter(arrayAdapter) { _, which ->
            val videoData = videoPosts[which].getData() as AmityPost.Data.VIDEO
            showVideoList(videoData)
        }
        builderSingle.show()
    }

    private fun showVideoList(videoData: AmityPost.Data.VIDEO) {
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this)
        builderSingle.setTitle("Select qualities")
        builderSingle.setCancelable(true)
        builderSingle.setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
        videoData.getAvailableQualities().forEach { quality ->
            arrayAdapter.add(quality.apiString)
        }
        builderSingle.setAdapter(arrayAdapter) { _, which ->
            val quality = AmityVideo.Quality.fromApiKey(arrayAdapter.getItem(which))
            openVideo(videoData, quality)
        }
        builderSingle.show()
    }
}