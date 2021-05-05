package com.amity.sample.ascsdk.comment.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.OpenEditCommentIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_comment.*

class EditCommentActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    lateinit var comment: AmityComment

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
        val data = comment.getData() as AmityComment.Data.TEXT
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