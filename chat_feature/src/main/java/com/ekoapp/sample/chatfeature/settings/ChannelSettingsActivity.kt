package com.ekoapp.sample.chatfeature.settings

import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.settings.list.MainChannelSettingsAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import kotlinx.android.synthetic.main.activity_settings.*


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
        var types: Set<String> = emptySet()
        var filter = ""
        var includingTags: Set<String> = emptySet()
        var excludingTags: Set<String> = emptySet()

        viewModel.observeChannelTypes().observeNotNull(this, { value ->
            types = value
        })
        viewModel.observeMembership().observeNotNull(this, { value ->
            filter = value
        })
        viewModel.observeIncludeTags().observeNotNull(this, { value ->
            includingTags = value
        })
        viewModel.observeExcludeTags().observeNotNull(this, { value ->
            excludingTags = value
        })
        viewModel.observeSave().observeNotNull(this, {
            viewModel.saveChannelTypes(types)
            viewModel.saveMembership(filter)
            viewModel.saveIncludeTags(includingTags)
            viewModel.saveExcludeTags(excludingTags)
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