package com.amity.sample.ascsdk.post.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.sample.ascsdk.R
import kotlinx.android.synthetic.main.activity_post_create.*

abstract class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)
        button_post.setOnClickListener {
            post()
        }
    }

    abstract fun post()

}