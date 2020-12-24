package com.ekoapp.sdk.comment.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.intent.OpenCreateCommentIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_comment.*

class CreateCommentActivity : AppCompatActivity() {

    private val repository = EkoClient.newCommentRepository()
    private val compositeDisposable = CompositeDisposable()

    lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)
        postId = OpenCreateCommentIntent.getPostId(intent)

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
        val disposable = repository.createComment()
                .post(postId)
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