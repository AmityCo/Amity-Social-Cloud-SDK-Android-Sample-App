package com.ekoapp.sample.chatfeature.settings

import android.content.SharedPreferences
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.settings.list.MainChannelSettingsAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.preferences.PreferenceHelper.channelTypes
import com.ekoapp.sample.core.preferences.PreferenceHelper.defaultPreference
import com.ekoapp.sample.core.preferences.PreferenceHelper.excludeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.includeTags
import com.ekoapp.sample.core.preferences.PreferenceHelper.membership
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.activity_settings.*


class ChannelSettingsActivity : SingleViewModelActivity<ChannelSettingsViewModel>() {

    private lateinit var adapter: MainChannelSettingsAdapter
    private lateinit var prefs: SharedPreferences

    override fun getLayout(): Int {
        return R.layout.activity_settings
    }

    override fun bindViewModel(viewModel: ChannelSettingsViewModel) {
        prefs = defaultPreference(this)
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
            prefs.channelTypes = it
        })
        viewModel.observeMembership().observeNotNull(this, {
            prefs.membership = it
        })
        viewModel.observeIncludeTags().observeNotNull(this, {
            prefs.includeTags = it
        })
        viewModel.observeExcludeTags().observeNotNull(this, {
            prefs.excludeTags = it
        })

        viewModel.observeSave().observeNotNull(this, {

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