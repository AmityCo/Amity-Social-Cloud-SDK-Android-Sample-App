package com.ekoapp.sample.socialfeature.userfeed.view.createfeeds

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.hideKeyboard
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.userfeed.view.toolbars.CreateFeedsToolbarMenu
import kotlinx.android.synthetic.main.activity_create_feeds.*


class CreateFeedsActivity : SingleViewModelActivity<CreateFeedsViewModel>() {

    override fun getToolbar(): ToolbarMenu? {
        return CreateFeedsToolbarMenu(create_feeds.getDescription(), eventPost = this::sendResult,
                onBackPressed = {
                    hideKeyboard()
                    finish()
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_feeds, menu)
        setupMenuPost(menu)
        return true
    }

    private fun setupMenuPost(menu: Menu) {
        val positionOfMenuItem = 0
        val item: MenuItem = menu.getItem(positionOfMenuItem)
        val spannable = SpannableString(getString(R.string.temporarily_post))
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 0, spannable.length, 0)
        item.title = spannable
    }

    override fun bindViewModel(viewModel: CreateFeedsViewModel) {
        setupAppBar()
    }

    private fun setupAppBar() {
        appbar_create_feeds.setup(this, true)
        appbar_create_feeds.setTitle(getString(R.string.temporarily_create_post))
    }

    override fun getViewModelClass(): Class<CreateFeedsViewModel> {
        return CreateFeedsViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_create_feeds
    }

    override fun initDependencyInjection() {
        DaggerSocialActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    private fun sendResult(description: String) {
        viewModel?.createPost(description)
    }
}