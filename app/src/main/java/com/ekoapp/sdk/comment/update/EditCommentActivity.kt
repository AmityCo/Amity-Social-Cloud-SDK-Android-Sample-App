package com.ekoapp.sdk.comment.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.comment.EkoComment
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.intent.OpenEditCommentIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_comment.*

class EditCommentActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    lateinit var comment: EkoComment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_comment)
        comment = OpenEditCommentIntent.getComment(intent)

        initView()
        initListeners()
    }

    private fun initListeners() {
        button_submit.setOnClickListener {
            editComment()
        }
    }

    private fun initView() {
        comment_id_textview.text = comment.getCommentId()
    }

    private fun editComment() {
        val data = comment.getData() as EkoComment.Data.TEXT
        val disposable = data.edit()
                .text(comment_edittext.text.toString())
                .build()
                .apply()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToast("Edit successful !")
                    finish()
                }, {
                    showToast("Edit failed !")
                })

        compositeDisposable.addAll(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}