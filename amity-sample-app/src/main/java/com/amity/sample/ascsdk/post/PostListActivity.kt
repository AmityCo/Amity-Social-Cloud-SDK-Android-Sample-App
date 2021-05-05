package com.amity.sample.ascsdk.post

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityFeedRepository
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.*
import com.amity.sample.ascsdk.messagelist.option.ReactionOption
import com.amity.sample.ascsdk.post.option.PostOption
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_list.*
import java.util.*

abstract class PostListActivity : AppCompatActivity() {

    val feedRepository: AmityFeedRepository = AmitySocialClient.newFeedRepository()
    val adapter = PostListAdapter()
    abstract val targetType: String
    abstract val targetId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        post_list_recyclerview.adapter = adapter
        initListeners()
        getPostCollection {}
        pullToRefresh()
    }

    private fun pullToRefresh() {
        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true
            adapter.submitList(null)
            getPostCollection { swiperefresh.isRefreshing = false }
        }
    }

    private fun getPostCollection(callback: (PagedList<AmityPost>) -> Unit = {}) {
        getPostCollection()
                .doOnNext {
                    adapter.submitList(it)
                    callback.invoke(it)
                }
                .doOnError(Throwable::printStackTrace)
                .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        inflateMenu { menuInflater.inflate(it, menu) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_create_post -> {
                showCreatePostDialog()
                return true
            }
            R.id.menu_delete_post -> {
                showDialog(R.string.delete_post, "post id", "", false) { dialog, input ->
                    if (input.toString().isNotBlank()) {
                        feedRepository.deletePost(input.toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnComplete { showToast("delete post successful !") }
                                .doOnError { showToast("delete post failed !") }
                                .subscribe()
                    }
                }
                return true
            }
            R.id.menu_sort_feed -> {
                selectSortOption {
                    sortPostCollection(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext { pagedList ->
                                post_list_recyclerview.adapter = adapter
                                adapter.submitList(pagedList)
                            }
                            .doOnError(Throwable::printStackTrace)
                            .subscribe()
                }
            }
            R.id.menu_member -> {
                startActivity(ViewCommunityMembershipsIntent(this, targetId))
            }
            R.id.include_deleted_feed -> {
                selectIncludeDeleted {
                    getPostCollectionByIncludeDeleted(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext { pagedList ->
                                post_list_recyclerview.adapter = adapter
                                adapter.submitList(pagedList)
                            }
                            .doOnError(Throwable::printStackTrace)
                            .subscribe()
                }
            }
            R.id.action_check_permission -> {
                checkPermission()
            }
            R.id.notification_setting_community -> {
                notificationSetting()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun inflateMenu(menuRes: (Int) -> Unit)
    abstract fun getPostCollection(): Flowable<PagedList<AmityPost>>
    abstract fun selectSortOption(callback: (Int) -> Unit)
    abstract fun sortPostCollection(checkedItem: Int): Flowable<PagedList<AmityPost>>
    abstract fun selectIncludeDeleted(callback: (Boolean) -> Unit)
    abstract fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagedList<AmityPost>>
    abstract fun checkPermission()
    abstract fun notificationSetting()


    private fun initListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    startActivity(OpenCommentListIntent(this@PostListActivity, it.getPostId()))
                }, Consumer { })

        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    onLongClick(it)
                }, Consumer { })
    }

    private fun onLongClick(post: AmityPost) {
        val actionItems = mutableListOf<String>()
        for (option in PostOption.values()) {
            actionItems.add(option.value)
        }

        MaterialDialog(this).show {
            listItems(items = actionItems) { dialog, position, text ->
                when (PostOption.enumOf(text.toString())) {
                    PostOption.VIEW_POST -> {
                        startActivity(OpenSinglePostActivityIntent(context, post))
                    }
                    PostOption.FLAG_POST -> {
                        flagPost(post)
                    }
                    PostOption.EDIT -> {
                        showEditPostDialog(post)
                    }
                    PostOption.DELETE -> {
                        post.delete().subscribe()
                    }
                    PostOption.ADD_REACTION -> {
                        showAddReactionDialog(post)
                    }
                    PostOption.REMOVE_REACTION -> {
                        showRemoveReactionDialog(post)
                    }
                    PostOption.REACTION_HISTORY -> {
                        startActivity(OpenPostReactionActivityIntent(this@PostListActivity, post))
                    }
                }
            }
        }

    }

    private fun goToCreateTextPost() {
        startActivity(OpenCreateTextPostIntent(this, targetType, targetId))
    }

    private fun goToCreateImagePost() {
        startActivity(OpenCreateImagePostIntent(this, targetType, targetId))
    }

    private fun goToCreateFilePost() {
        startActivity(OpenCreateFilePostIntent(this, targetType, targetId))
    }

    private fun showCreatePostDialog() {
        val postTypes = listOf("text", "image", "file")

        MaterialDialog(this).show {
            listItems(items = postTypes) { dialog, position, text ->
                when (text) {
                    "text" -> {
                        goToCreateTextPost()
                    }
                    "image" -> {
                        goToCreateImagePost()
                    }
                    "file" -> {
                        goToCreateFilePost()
                    }
                }
            }
        }

    }

    private fun showEditPostDialog(post: AmityPost) {
        val data = post.getData()
        if (data is AmityPost.Data.TEXT)

            showDialog(R.string.edit_text_message, "enter text", data.getText(), false, { dialog, input ->
                if (input.toString() != data.getText()) {
                    data.edit().text(input.toString()).build().apply().subscribe()
                }
            })
    }

    private fun flagPost(post: AmityPost) {
        if (post.isFlaggedByMe) {
            MaterialDialog(this).show {
                title = "un-flag a post"
                positiveButton(text = "un-flag a post") {
                    post.report()
                            .unflag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                Toast.makeText(this@PostListActivity, "successfully un-flagged a post", Toast.LENGTH_SHORT).show()
                            }
                            .subscribe()
                }
            }

        } else {
            MaterialDialog(this).show {
                title = "flag a post"
                positiveButton(text = "flag") {
                    post.report()
                            .flag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                Toast.makeText(this@PostListActivity, "successfully flagged the a post", Toast.LENGTH_SHORT).show()
                            }
                            .subscribe()
                }
            }
        }
    }

    private fun showAddReactionDialog(post: AmityPost) {
        val reactionItems = mutableListOf<String>()
        ReactionOption.values().filter {
            !post.getMyReactions().contains(it.value())
        }.forEach {
            reactionItems.add(it.value())
        }

        MaterialDialog(this).show {
            listItems(items = reactionItems) { dialog, position, text ->
                post.react()
                        .addReaction(text.toString())
                        .subscribe()
            }
        }

    }

    private fun showRemoveReactionDialog(post: AmityPost) {
        val options = post.getMyReactions()

        MaterialDialog(this).show {
            listItems(items = options) { dialog, position, text ->
                post.react()
                        .removeReaction(text.toString())
                        .subscribe()
            }
        }
    }

}