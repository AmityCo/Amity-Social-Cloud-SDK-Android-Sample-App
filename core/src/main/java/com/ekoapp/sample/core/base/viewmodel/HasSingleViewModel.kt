package com.ekoapp.sample.core.base.viewmodel

import androidx.lifecycle.ViewModel

interface HasSingleViewModel<VM : ViewModel> {
	fun bindViewModel(viewModel: VM)
	fun getViewModelClass(): Class<VM>
}