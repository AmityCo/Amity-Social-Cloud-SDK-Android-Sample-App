package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import kotlinx.android.synthetic.main.component_selected_photo.view.*

class SelectedPhotoComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_selected_photo, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(uri: Uri, action: (Uri) -> Unit) {
        image_selected_photo.setImageURI(uri)
        button_send.setOnClickListener { action.invoke(uri) }
    }
}