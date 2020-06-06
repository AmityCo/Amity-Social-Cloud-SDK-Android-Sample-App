package com.ekoapp.sample.chatfeature.settings

import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.settings.list.MainChannelSettingsAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.core.utils.getCurrentClassAndMethodNames
import kotlinx.android.synthetic.main.activity_settings.*
import timber.log.Timber


class ChannelSettingsActivity : SingleViewModelActivity<ChannelSettingsViewModel>() {

    private lateinit var adapter: MainChannelSettingsAdapter

    override fun getLayout(): Int {
        return R.layout.activity_settings
    }

    override fun bindViewModel(viewModel: ChannelSettingsViewModel) {
        setupAppBar()
        renderList(viewModel)
        setupView(viewModel)
    }

    private fun setupAppBar() {
        appbar_settings.setup(this, true)
        appbar_settings.setTitle(getString(R.string.temporarily_settings))
    }

    private fun renderList(viewModel: ChannelSettingsViewModel) {
        adapter = MainChannelSettingsAdapter(this, viewModel)
        RecyclerBuilder(context = this, recyclerView = recycler_settings)
                .builder()
                .build(adapter)
    }

    private fun setupView(viewModel: ChannelSettingsViewModel) {
        viewModel.observeChannelTypes().observeNotNull(this, {
            //TODO Save Preference
            Timber.d(getCurrentClassAndMethodNames() +" observeChannelTypes " + it)
        })
        viewModel.observeMembership().observeNotNull(this, {
            //TODO Save Preference
            Timber.d(getCurrentClassAndMethodNames() +" observeMembership " + it)
        })
        viewModel.observeIncludeTags().observeNotNull(this, {
            //TODO Save Preference
            Timber.d(getCurrentClassAndMethodNames() +" observeIncludeTags " + it)
        })
        viewModel.observeExcludeTags().observeNotNull(this, {
            //TODO Save Preference
            Timber.d(getCurrentClassAndMethodNames() +" observeExcludeTags " + it)
        })
    }

    override fun getViewModelClass(): Class<ChannelSettingsViewModel> {
        return ChannelSettingsViewModel::class.java
    }

    override fun initDependencyInjection() {
        DaggerChatActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

}