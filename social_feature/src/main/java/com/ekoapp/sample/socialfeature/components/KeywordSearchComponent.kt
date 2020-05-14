package com.ekoapp.sample.socialfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.socialfeature.R
import kotlinx.android.synthetic.main.component_keyword_search.view.*


class KeywordSearchComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_keyword_search, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun renderKeywordView(keyword: String) {
        if (keyword.isEmpty()) {
            text_search_results_keyword.visibility = View.GONE
        } else {
            text_search_results_keyword.visibility = View.VISIBLE
        }
        text_search_results_keyword.text = String.format(context.getString(R.string.general_search_see_results_for), keyword)
    }
}