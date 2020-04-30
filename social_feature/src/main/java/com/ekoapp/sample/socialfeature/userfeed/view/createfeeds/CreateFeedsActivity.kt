package com.ekoapp.sample.socialfeature.userfeed.view.createfeeds

import android.app.Activity
import android.content.Intent
import android.view.Menu
import com.ekoapp.sample.core.base.components.toolbar.ToolbarMenu
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.hideKeyboard
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.EXTRA_NAME_CREATE_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.userfeed.view.toolbars.CreateFeedsToolbarMenu
import kotlinx.android.synthetic.main.activity_create_feeds.*
import timber.log.Timber


class CreateFeedsActivity : SingleViewModelActivity<CreateFeedsViewModel>() {

    var menu: Menu? = null

    override fun getToolbar(): ToolbarMenu? {
        return CreateFeedsToolbarMenu(onPostCallback = {
            val localMenu = menu
            if (localMenu == null) {
                Timber.e("${getCurrentClassAndMethodNames()}${Throwable("Invalid menu state, cannot initial menu since it is null")}")
                create_feeds.getDescription()?.let(this::sendResult)
                hideKeyboard()
                finish()
            } else this.onPrepareOptionsMenu(localMenu)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_feeds, menu)
        return true
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
        viewModel?.let {
            val data = Intent()
            data.putExtra(EXTRA_NAME_CREATE_FEEDS, it.mockCreateFeeds(description))
            setResult(Activity.RESULT_OK, data)
        }
    }
}