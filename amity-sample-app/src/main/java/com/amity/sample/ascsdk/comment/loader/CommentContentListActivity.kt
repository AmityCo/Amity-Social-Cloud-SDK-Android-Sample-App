package com.amity.sample.ascsdk.comment.loader

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.comment.AmityCommentLoader
import com.amity.socialcloud.sdk.social.comment.AmityCommentSortOption

import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.comment.options.CommentOption
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenCommentContentListIntent
import com.amity.sample.ascsdk.intent.OpenCommentReactionListIntent
import com.amity.sample.ascsdk.intent.OpenCreateCommentContentIntent
import com.amity.sample.ascsdk.intent.OpenEditCommentIntent
import com.amity.sample.ascsdk.messagelist.option.ReactionOption
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_content_list.*

class CommentContentListActivity : AppCompatActivity() {
    private val repository = AmitySocialClient.newCommentRepository()
    private val adapter = CommentAdapter(mutableListOf<AmityComment>())
    private lateinit var loader : AmityCommentLoader
    private val compositeDisposable = CompositeDisposable()

    lateinit var contentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_content_list)
        contentId = OpenCommentContentListIntent.getContentId(intent)

        initView()
        initListeners()
        loader = repository
                .getComments()
                .content(contentId)
                .sortBy(AmityCommentSortOption.LAST_CREATED)
                .includeDeleted(false)
                .build()
                .loader()


        val disposable = loader
                .getResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ commentEntityList ->
                    adapter.updateListItems(commentEntityList)
                }, {
                    showToast(it.message ?: "Sorry, Get comments error !")
                })

        loader.load()
                .subscribeOn(Schedulers.io())
                .subscribe()
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
        val layoutManager = LinearLayoutManager(this)
       content_comment_list_recyclerview.layoutManager = layoutManager
        content_comment_list_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount-1){
                    if(loader.hasMore()) {
                        loader.load()
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                    } else {
                        Log.e("Comment manual load" ,"Last page reached")
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
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

    private fun onLongClick(comment: AmityComment) {
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

    private fun flagComment(comment: AmityComment) {
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