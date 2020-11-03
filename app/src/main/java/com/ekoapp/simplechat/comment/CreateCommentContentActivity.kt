package com.ekoapp.simplechat.comment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.intent.OpenCreateCommentContentIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_comment.*

class CreateCommentContentActivity : AppCompatActivity() {

    private val repository = EkoClient.newCommentRepository()
    private val compositeDisposable = CompositeDisposable()

    lateinit var contentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)
        contentId = OpenCreateCommentContentIntent.getContentId(intent)

        initView()
        initListeners()
    }

    private fun initListeners() {
        button_submit.setOnClickListener {
            createComment()
        }
    }

    private fun initView() {
        post_id_textview.text = contentId
    }

    private fun createComment() {
        val disposable = repository.createComment()
                .content(contentId)
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