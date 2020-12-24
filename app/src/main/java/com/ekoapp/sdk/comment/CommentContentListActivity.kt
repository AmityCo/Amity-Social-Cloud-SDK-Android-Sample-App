package com.ekoapp.sdk.comment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.comment.EkoComment
import com.ekoapp.sdk.R
import com.ekoapp.sdk.comment.options.CommentOption
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.intent.OpenCommentContentListIntent
import com.ekoapp.sdk.intent.OpenCommentReactionListIntent
import com.ekoapp.sdk.intent.OpenCreateCommentContentIntent
import com.ekoapp.sdk.intent.OpenEditCommentIntent
import com.ekoapp.sdk.messagelist.option.ReactionOption
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_content_list.*

class CommentContentListActivity : AppCompatActivity() {
    private val repository = EkoClient.newCommentRepository()
    private val adapter = CommentListAdapter()
    private val compositeDisposable = CompositeDisposable()

    lateinit var contentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_content_list)
        contentId = OpenCommentContentListIntent.getContentId(intent)

        initView()
        initListeners()

        val disposable = repository
                .getCommentCollection()
                .content(contentId)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ commentEntityList ->
                    adapter.submitList(commentEntityList)
                }, {
                    showToast(it.message ?: "Sorry, Get comments error !")
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
                startActivity(OpenCreateCommentContentIntent(this, contentId))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun initView() {
        content_comment_list_recyclerview.adapter = adapter
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //TODO Handle event click on adapter item.
                }, { })

        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onLongClick(it)
                }, { })
    }

    private fun onLongClick(comment: EkoComment) {
        val actionItems = mutableListOf<String>()
        for (option in CommentOption.values()) {
            actionItems.add(option.value)
        }
        MaterialDialog(this).show {
            listItems(items = actionItems) { _, _, text ->
                when (CommentOption.enumOf(text.toString())) {
                    CommentOption.EDIT -> {
                        startActivity(OpenEditCommentIntent(this@CommentContentListActivity, comment))
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

    private fun flagComment(comment: EkoComment) {
        if (comment.isFlaggedByMe()) {
            MaterialDialog(this).show {
                title = "un-flag a comment"
                positiveButton(text = "un-flag a comment") {
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

    private fun showAddReactionDialog(comment: EkoComment) {
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

    private fun showRemoveReactionDialog(comment: EkoComment) {
        val reactionItems = comment.getMyReactions()
        MaterialDialog(this).show {
            listItems(items = reactionItems) { dialog, position, text ->
                comment.react()
                        .removeReaction(text.toString())
                        .subscribe()
            }
        }
    }

    private fun showReactionHistory(comment: EkoComment) {
        startActivity(OpenCommentReactionListIntent(this, comment))
    }
}