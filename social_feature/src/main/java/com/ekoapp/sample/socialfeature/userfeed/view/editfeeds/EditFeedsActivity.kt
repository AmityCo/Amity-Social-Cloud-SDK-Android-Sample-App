package com.ekoapp.sample.socialfeature.userfeed.view.editfeeds

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
import com.ekoapp.sample.socialfeature.userfeed.EXTRA_NAME_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.toolbars.EditFeedsToolbarMenu
import kotlinx.android.synthetic.main.activity_create_feeds.*


class EditFeedsActivity : SingleViewModelActivity<EditFeedsViewModel>() {

    override fun getToolbar(): ToolbarMenu? {
        return EditFeedsToolbarMenu(create_feeds.getDescription(), eventEdit = this::sendResult,
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
        val spannable = SpannableString(getString(R.string.temporarily_edit))
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 0, spannable.length, 0)
        item.title = spannable
    }

    override fun bindViewModel(viewModel: EditFeedsViewModel) {
        val item = intent.extras?.getParcelable<SampleFeedsResponse>(EXTRA_NAME_FEEDS)
        item?.let { setupView(item) }
        setupAppBar()
    }

    private fun setupAppBar() {
        appbar_create_feeds.setup(this, true)
        appbar_create_feeds.setTitle(getString(R.string.temporarily_edit_post))
    }

    private fun setupView(item: SampleFeedsResponse) {
        create_feeds.setDescription(item.description)
    }

    override fun getViewModelClass(): Class<EditFeedsViewModel> {
        return EditFeedsViewModel::class.java
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
            /* val data = Intent()
             data.putExtra(EXTRA_NAME_EDIT_FEEDS, it.mockEditFeeds(id, description))
             setResult(Activity.RESULT_OK, data)*/
        }
    }
}