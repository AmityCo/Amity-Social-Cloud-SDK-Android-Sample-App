package com.ekoapp.sample.core.base.components.toolbar

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekoapp.sample.core.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.component_search_toolbar.view.*
import kotlinx.android.synthetic.main.component_toolbar.view.toolbar_main


class SearchAppbar : AppBarLayout {

    private val keywordRelay = MutableLiveData<String>()

    fun observeKeywordSearch(): LiveData<String> = keywordRelay

    constructor(context: Context) : super(context, null as AttributeSet?) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.component_search_toolbar, this, true)
        setupEvent()
    }

    fun setup(activity: AppCompatActivity, hasBack: Boolean) {
        toolbar_main?.let {
            activity.setSupportActionBar(it)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
            activity.setupBackButton(it, hasBack)
        }
    }

    private fun setupEvent() {
        edit_text_search.clearComposingText()
        edit_text_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edit_text_search.removeTextChangedListener(this)
                keywordRelay.postValue(s.toString())
                edit_text_search.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }
}