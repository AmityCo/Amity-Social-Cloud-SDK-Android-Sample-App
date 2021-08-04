package com.amity.sample.ascsdk.post

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.*
import com.amity.sample.ascsdk.messagelist.option.ReactionOption
import com.amity.sample.ascsdk.post.option.PostOption
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityFeedType
import com.amity.socialcloud.sdk.social.feed.AmityPost
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_list.*

abstract class PostPagingActivity : AppCompatActivity() {

    private val pagingDataAdapter by lazy { PostPagingDataAdapter() }

    private val disposable = CompositeDisposable()

    abstract val targetType: String
    abstract val targetId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        usePagingDataAdapter()
        initListeners()
        pullToRefresh()
    }

    private fun usePagingDataAdapter() {
        disposable.clear()
        post_list_recyclerview.adapter = pagingDataAdapter
        getPostsAndSubmitPagingDataAdapter {}
    }

    private fun pullToRefresh() {
        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true
            getPostsAndSubmitPagingDataAdapter { swiperefresh.isRefreshing = false }
        }
    }


    private fun getPostsAndSubmitPagingDataAdapter(callback: (PagingData<AmityPost>) -> Unit = {}) {
        disposable.add(getPostsAsPagingData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                pagingDataAdapter.submitData(lifecycle, it)
                callback.invoke(it)
            }
            .doOnError(Throwable::printStackTrace)
            .subscribe())
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
                        AmitySocialClient.newPostRepository().deletePost(input.toString())
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
                    disposable.clear()
                    disposable.add(sortPostCollection(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { pagingData ->
                            post_list_recyclerview.adapter = pagingDataAdapter
                            pagingDataAdapter.submitData(lifecycle, pagingData)
                        }
                        .doOnError(Throwable::printStackTrace)
                        .subscribe())
                }
            }
            R.id.menu_post_type_config -> {
                selectFilterOption {
                    disposable.clear()
                    disposable.add(filterPostCollection(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { pagingData ->
                            post_list_recyclerview.adapter = pagingDataAdapter
                            pagingDataAdapter.submitData(lifecycle, pagingData)
                        }
                        .doOnError(Throwable::printStackTrace)
                        .subscribe())
                }
            }

            R.id.include_deleted_feed -> {
                selectIncludeDeleted {
                    disposable.clear()
                    disposable.add(getPostCollectionByIncludeDeleted(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { pagingData ->
                            post_list_recyclerview.adapter = pagingDataAdapter
                            pagingDataAdapter.submitData(lifecycle, pagingData)
                        }
                        .doOnError(Throwable::printStackTrace)
                        .subscribe())
                }
            }
            R.id.action_check_permission -> {
                checkPermission()
            }
            R.id.notification_setting_community -> {
                notificationSetting()
            }
            R.id.menu_member -> {
                startActivity(ViewCommunityMembershipsIntent(this, targetId))
            }
            R.id.pending_posts -> {
                disposable.clear()
                disposable.add(getPostCollectionByFeedType(AmityFeedType.REVIEWING)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { pagingData ->
                        post_list_recyclerview.adapter = pagingDataAdapter
                        pagingDataAdapter.submitData(lifecycle, pagingData)
                    }
                    .doOnError(Throwable::printStackTrace)
                    .subscribe())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun inflateMenu(menuRes: (Int) -> Unit)
    abstract fun getPostsAsPagingData(): Flowable<PagingData<AmityPost>>
    abstract fun selectSortOption(callback: (Int) -> Unit)
    abstract fun selectFilterOption(callback: (List<AmityPost.Type>) -> Unit)
    abstract fun sortPostCollection(checkedItem: Int): Flowable<PagingData<AmityPost>>
    abstract fun filterPostCollection(postTypes: List<AmityPost.Type>): Flowable<PagingData<AmityPost>>
    abstract fun selectIncludeDeleted(callback: (Boolean) -> Unit)
    abstract fun getPostCollectionByIncludeDeleted(isIncludeDeleted: Boolean): Flowable<PagingData<AmityPost>>
    abstract fun getPostCollectionByFeedType(feedType: AmityFeedType): Flowable<PagingData<AmityPost>>
    abstract fun checkPermission()
    abstract fun notificationSetting()


    private fun initListeners() {
        pagingDataAdapter.onClickFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                startActivity(OpenCommentListIntent(this@PostPagingActivity, it.getPostId()))
            }, { })

        pagingDataAdapter.onLongClickFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onLongClick(it)
            }, { })
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
                        startActivity(
                            OpenPostReactionActivityIntent(
                                this@PostPagingActivity,
                                post
                            )
                        )
                    }
                    PostOption.APPROVE_POST -> {
                        approvePost(post)
                    }
                    PostOption.DECLINE_POST -> {
                        declinePost(post)
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

    private fun goToCreateVideoPost() {
        startActivity(OpenCreateVideoPostIntent(this, targetType, targetId))
    }

    private fun showCreatePostDialog() {
        val postTypes = listOf("text", "image", "file", "video")

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
                    "video" -> {
                        goToCreateVideoPost()
                    }
                }
            }
        }

    }

    private fun showEditPostDialog(post: AmityPost) {
        val data = post.getData()
        if (data is AmityPost.Data.TEXT)

            showDialog(
                R.string.edit_text_message,
                "enter text",
                data.getText(),
                false
            ) { dialog, input ->
                if (input.toString() != data.getText()) {
                    data.edit().text(input.toString()).build().apply().subscribe()
                }
            }
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
                            Toast.makeText(
                                this@PostPagingActivity,
                                "successfully un-flagged a post",
                                Toast.LENGTH_SHORT
                            ).show()
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
                            Toast.makeText(
                                this@PostPagingActivity,
                                "successfully flagged the a post",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun approvePost(post: AmityPost) {
        disposable.add(AmitySocialClient.newFeedRepository().reviewPost(postId = post.getPostId())
            .approve()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                showToast("Success approve post!")
            }
            .doOnError {
                showToast("Sorry! " + it.message.toString())
            }
            .subscribe())
    }

    private fun declinePost(post: AmityPost) {
        disposable.add(AmitySocialClient.newFeedRepository().reviewPost(postId = post.getPostId())
            .decline()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                showToast("Success decline post!")
            }
            .doOnError {
                showToast("Sorry! " + it.message.toString())
            }
            .subscribe())
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

}