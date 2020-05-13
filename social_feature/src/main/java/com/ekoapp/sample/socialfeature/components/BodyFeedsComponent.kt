package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.component_body_feeds.view.*
import org.json.JSONObject


class BodyFeedsComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_body_feeds, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {
        val dataJson: String = item.data.toString()
        val dataObject = JSONObject(dataJson)

        val textBody: Any = dataObject.get("text")

        (textBody as String).let {
            text_description.text = it
        }
    }

    fun getDescription() = text_description.text.toString()

}