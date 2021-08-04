package com.amity.sample.ascsdk.post

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import com.amity.socialcloud.sdk.social.feed.AmityPost
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_post, parent, false)) {

    private var postTextView: TextView? = null

    init {
        postTextView = itemView.findViewById(R.id.post_textview)
    }

    fun bind(post: AmityPost) {
        val rootPost = "postId: %s\n" +
                "feedType: %s\n" +
                "dataType: %s\n" +
                "data: %s\n" +
                "myReactions: %s\n" +
                "reactionCount: %s\n" +
                "commentCount: %s\n" +
                "childrenCount: %s\n" +
                "deleted: %s\n" +
                "updatedAt: %s\n" +
                "createdAt: %s"

        val childPost = "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n\n" +
                "       postId: %s\n" +
                "       parentId: %s\n" +
                "       feedType: %s\n" +
                "       dataType: %s\n" +
                "       data: %s\n" +
                "       myReactions: %s\n" +
                "       reactionCount: %s\n" +
                "       commentCount: %s\n" +
                "       deleted: %s\n" +
                "       updatedAt: %s\n" +
                "       createdAt: %s\n"

        var displayPost = String.format(
            rootPost,
            post.getPostId(),
            post.getFeedType().apiKey,
            post.getType().getApiKey(),
            (post.getData() as? AmityPost.Data.TEXT)?.getText()
                ?: (post.getData() as? AmityPost.Data.IMAGE)?.getImage()?.getUrl()
                ?: (post.getData() as? AmityPost.Data.FILE)?.getFile()?.getUrl()
                ?: (post.getData() as? AmityPost.Data.CUSTOM)?.getRawData()?.toString()
                ?: (post.getData() as? AmityPost.Data.VIDEO)?.getThumbnailImage()?.getUrl()
                ?: "unknown",
            post.getMyReactions(),
            post.getReactionCount(),
            post.getCommentCount(),
            post.getChildren().size,
            post.isDeleted(),
            post.getUpdatedAt()?.toString(ISODateTimeFormat.dateHourMinuteSecond()),
            post.getCreatedAt()?.toString(ISODateTimeFormat.dateHourMinuteSecond())
        )

        post.getChildren().forEach {
            displayPost += String.format(
                childPost,
                it.getPostId(),
                it.getParentPostId(),
                it.getFeedType().apiKey,
                it.getType().getApiKey(),
                (it.getData() as? AmityPost.Data.TEXT)?.getText(),
                it.getMyReactions(),
                it.getReactionCount(),
                it.getCommentCount(),
                it.isDeleted(),
                it.getUpdatedAt()?.toString(ISODateTimeFormat.dateHourMinuteSecond()),
                it.getCreatedAt()?.toString(ISODateTimeFormat.dateHourMinuteSecond())
            )
        }
        postTextView?.text = displayPost
    }

    object PostDiffUtil : DiffUtil.ItemCallback<AmityPost>() {
        override fun areItemsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean {
            return oldItem.getPostId() == newItem.getPostId()
        }

        override fun areContentsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean {
            return oldItem.getPostId() == newItem.getPostId()
                    && oldItem.getUpdatedAt() == newItem.getUpdatedAt()
        }
    }
}