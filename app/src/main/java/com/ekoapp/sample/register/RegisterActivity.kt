package com.ekoapp.sample.register

import android.content.Intent
import com.ekoapp.sample.MainNavigationActivity
import com.ekoapp.sample.R
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.di.DaggerMainNavigationComponent
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : SingleViewModelActivity<RegisterViewModel>() {

    override fun bindViewModel(viewModel: RegisterViewModel) {
        setupView(viewModel)
    }

    private fun setupView(viewModel: RegisterViewModel) {
        edit_text_register_id.setText(viewModel.getDisplayName())
        button_register.setOnClickListener {
            viewModel.register(edit_text_register_id.text.toString())
        }
        viewModel.observeRegisterAction().observeNotNull(this, { this.navMainNavigation() })
    }

    private fun navMainNavigation() {
        startActivity(Intent(this, MainNavigationActivity::class.java))
        finish()
    }

    override fun getViewModelClass(): Class<RegisterViewModel> {
        return RegisterViewModel::class.java
    }

    override fun getLayout(): Int {
        return R.layout.activity_register
    }

    override fun initDependencyInjection() {
        DaggerMainNavigationComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }
}