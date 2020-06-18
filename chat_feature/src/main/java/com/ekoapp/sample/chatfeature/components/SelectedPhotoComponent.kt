package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_selected_photo.view.*

class SelectedPhotoComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_selected_photo, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(path: String, action: () -> Unit) {
        Glide.with(this).clear(image_selected_photo)
        BitmapFactory.decodeFile(path)?.also { bitmap ->
            Glide.with(this)
                    .load(bitmap)
                    .placeholder(R.drawable.ic_thumbnail_file)
                    .into(image_selected_photo)
        }

        button_send.setOnClickListener { action.invoke() }
    }
}