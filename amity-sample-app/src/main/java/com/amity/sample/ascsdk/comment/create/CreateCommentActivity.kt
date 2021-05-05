package com.amity.sample.ascsdk.comment.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenCreateCommentIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_comment.*

class CreateCommentActivity : AppCompatActivity() {

    private val repository = AmitySocialClient.newCommentRepository()
    private val compositeDisposable = CompositeDisposable()

    private var postId: String = ""
    private var commentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)
        postId = OpenCreateCommentIntent.getPostId(intent) ?: ""
        commentId = OpenCreateCommentIntent.getCommentId(intent) ?: ""

        initView()
        initListeners()
    }

    private fun initListeners() {
        button_submit.setOnClickListener {
            createComment()
        }
    }

    private fun initView() {
        post_id_textview.text = postId
    }

    private fun createComment() {
        val commentCreation = repository.createComment().post(postId)

        if (commentId.isNotEmpty()) {
            commentCreation.parentId(commentId)
        }

        val disposable = commentCreation
                .with()
                .text(comment_edittext.text.toString())
                .build()
                .send()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast("Create successful !")
                    finish()
                }, {
                    it.printStackTrace()
                    showToast("Create failed !")
                })
        compositeDisposable.addAll(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}