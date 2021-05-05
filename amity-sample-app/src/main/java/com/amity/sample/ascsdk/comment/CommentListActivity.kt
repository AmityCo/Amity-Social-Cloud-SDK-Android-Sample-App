package com.amity.sample.ascsdk.comment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.comment.AmityCommentSortOption
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.comment.options.CommentOption
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.*
import com.amity.sample.ascsdk.messagelist.option.ReactionOption
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_list.*

class CommentListActivity : AppCompatActivity() {

    private val repository = AmitySocialClient.newCommentRepository()
    private val adapter = CommentListAdapter()
    private val compositeDisposable = CompositeDisposable()

    lateinit var postId: String

    private var sortOption = AmityCommentSortOption.FIRST_CREATED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        postId = OpenCommentListIntent.getPostId(intent)

        initView()
        initListeners()
        getCommentCollection(sortOption) {}
        pullToRefresh()
    }

    private fun pullToRefresh() {
        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true
            adapter.submitList(null)
            getCommentCollection(sortOption) { swiperefresh.isRefreshing = false }
        }
    }

    private fun getCommentCollection(sortBy: AmityCommentSortOption, callback: (PagedList<AmityComment>) -> Unit = {}) {
        val disposable = repository
                .getComments()
                .post(postId)
                .sortBy(sortBy)
                .parentId(null)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ commentEntityList ->
                    adapter.submitList(commentEntityList)
                    callback.invoke(commentEntityList)
                }, {
                    showToast(it.message ?: "Sorry, Get comments error !")
                    it.printStackTrace()
                })
        compositeDisposable.add(disposable)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_comment_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_create_comment -> {
                startActivity(OpenCreateCommentIntent(this, postId))
                return true
            }
            R.id.menu_sort_by_first_created -> {
                sortOption = AmityCommentSortOption.FIRST_CREATED
                getCommentCollection(sortOption) {}
            }
            R.id.menu_sort_by_last_created -> {
                sortOption = AmityCommentSortOption.LAST_CREATED
                getCommentCollection(sortOption) {}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun initView() {
        post_list_recyclerview.adapter = adapter
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startActivity(OpenReplyCommentListIntent(this, postId, it.getCommentId()))
                }, { })

        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onLongClick(it)
                }, { })
    }

    private fun onLongClick(comment: AmityComment) {
        val actionItems = mutableListOf<String>()
        for (option in CommentOption.values()) {
            actionItems.add(option.value)
        }
        MaterialDialog(this).show {
            listItems(items = actionItems) { _, _, text ->
                when (CommentOption.enumOf(text.toString())) {
                    CommentOption.EDIT -> {
                        startActivity(OpenEditCommentIntent(this@CommentListActivity, comment))
                    }
                    CommentOption.DELETE -> {
                        comment.delete().subscribe()
                    }
                    CommentOption.FLAG -> {
                        flagComment(comment)
                    }
                    CommentOption.ADD_REACTION -> {
                        showAddReactionDialog(comment)
                    }
                    CommentOption.REMOVE_REACTION -> {
                        showRemoveReactionDialog(comment)
                    }
                    CommentOption.ALL_REACTION -> {
                        showReactionHistory(comment)
                    }
                }
            }
        }
    }

    private fun flagComment(comment: AmityComment) {
        if (comment.isFlaggedByMe()) {
            MaterialDialog(this).show {
                title = "un-flag a comment"
                positiveButton(text = "un-flag a post") {
                    comment.report()
                            .unflag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                showToast("successfully un-flagged a comment")
                            }
                            .subscribe()
                }
            }

        } else {
            MaterialDialog(this).show {
                title = "flag a comment"
                positiveButton(text = "flag") {
                    comment.report()
                            .flag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                showToast("successfully flagged the a comment")
                            }
                            .subscribe()
                }
            }
        }
    }

    private fun showAddReactionDialog(comment: AmityComment) {
        val reactionItems = mutableListOf<String>()
        ReactionOption.values().filter {
            !comment.getMyReactions().contains(it.value())
        }.forEach {
            reactionItems.add(it.value())
        }

        if (reactionItems.isNullOrEmpty()) {
            return
        }

        MaterialDialog(this).show {
            listItems(items = reactionItems) { dialog, position, text ->
                comment.react()
                        .addReaction(text.toString())
                        .subscribe()
            }
        }
    }

    private fun showRemoveReactionDialog(comment: AmityComment) {
        val reactionItems = comment.getMyReactions()
        MaterialDialog(this).show {
            listItems(items = reactionItems) { dialog, position, text ->
                comment.react()
                        .removeReaction(text.toString())
                        .subscribe()
            }
        }
    }

    private fun showReactionHistory(comment: AmityComment) {
        startActivity(OpenCommentReactionListIntent(this, comment))
    }

}