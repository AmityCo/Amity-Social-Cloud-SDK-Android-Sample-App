package com.ekoapp.sample.chatfeature.settings

import android.content.Intent
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.constants.EXTRA_CHANNEL_SETTINGS
import com.ekoapp.sample.chatfeature.di.DaggerChatActivityComponent
import com.ekoapp.sample.chatfeature.settings.list.MainChannelSettingsAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.core.ui.extensions.observeOnce
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
        viewModel.observeChannelTypes().observeNotNull(this, { types ->
            viewModel.observeSave().observeOnce(this, {
                viewModel.saveChannelTypes(types)
                send(viewModel)
            })
        })
        viewModel.observeMembership().observeNotNull(this, { filter ->
            viewModel.observeSave().observeOnce(this, {
                viewModel.saveMembership(filter)
                send(viewModel)
            })
        })
        viewModel.observeIncludeTags().observeNotNull(this, { includingTags ->
            viewModel.observeSave().observeOnce(this, {
                viewModel.saveIncludeTags(includingTags)
                send(viewModel)
            })
        })
        viewModel.observeExcludeTags().observeNotNull(this, { excludingTags ->
            viewModel.observeSave().observeOnce(this, {
                viewModel.saveExcludeTags(excludingTags)
                send(viewModel)
            })
        })
    }

    private fun send(viewModel: ChannelSettingsViewModel) {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_CHANNEL_SETTINGS, viewModel.getChannelSettingsData())
        setResult(RESULT_OK, returnIntent)
        finish()
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