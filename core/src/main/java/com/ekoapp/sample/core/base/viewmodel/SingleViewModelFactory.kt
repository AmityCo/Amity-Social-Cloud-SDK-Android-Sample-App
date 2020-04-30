package com.ekoapp.sample.core.base.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class SingleViewModelFactory<VM : ViewModel> @Inject constructor(private val viewModel: Provider<VM>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(p0: Class<T>): T {
        return viewModel.get() as T
    }
}

inline fun <reified VM : ViewModel> SingleViewModelFactory<VM>.getViewModel(activity: FragmentActivity): VM {
    return ViewModelProvider(activity, this).get(VM::class.java)
}

inline fun <reified VM : ViewModel> SingleViewModelFactory<VM>.getViewModel(fragment: Fragment): VM {
    return ViewModelProvider(fragment, this).get(VM::class.java)
}
