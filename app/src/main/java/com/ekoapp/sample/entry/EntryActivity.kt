package com.ekoapp.sample.entry

import com.ekoapp.sample.R
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.di.DaggerMainNavigationComponent
import com.ekoapp.sample.intents.openMainNavigationPage
import com.ekoapp.sample.intents.openRegisterPage


class EntryActivity : SingleViewModelActivity<EntryViewModel>() {

    override fun bindViewModel(viewModel: EntryViewModel) {
        navigationPage(viewModel)
    }

    private fun navigationPage(viewModel: EntryViewModel) {
        viewModel.observeEntryAction().observeNotNull(this, {
            when (it) {
                is EntryNavigation.MainPage -> openMainNavigationPage()
                is EntryNavigation.RegisterPage -> openRegisterPage()
            }
        })
    }

    override fun getViewModelClass(): Class<EntryViewModel> {
        return EntryViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_entry
    }

    override fun initDependencyInjection() {
        DaggerMainNavigationComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}