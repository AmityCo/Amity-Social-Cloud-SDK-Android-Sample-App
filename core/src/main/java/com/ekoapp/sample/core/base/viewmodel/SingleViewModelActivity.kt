package com.ekoapp.sample.core.base.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekoapp.sample.core.base.BaseActivity
import javax.inject.Inject

abstract class SingleViewModelActivity<VM : ViewModel> : HasSingleViewModel<VM>, BaseActivity() {
    @Inject
    lateinit var vmFactory: SingleViewModelFactory<VM>
    protected var viewModel: VM? = null
    private val onViewModelActiveQueue: MutableList<(VM) -> Unit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencyInjection()
        val viewModel = ViewModelProvider(this, vmFactory).get(getViewModelClass())
        this.viewModel = viewModel
        bindViewModel(viewModel)
        invokeViewModelActiveQueue(viewModel)
    }

    fun onViewModelActive(func: (VM) -> Unit) {
        val localVm = viewModel
        if (localVm != null) func(localVm)
        else onViewModelActiveQueue.add(func)
    }

    private fun invokeViewModelActiveQueue(viewModel: VM) {
        val localQueue = onViewModelActiveQueue.toList()
        if (localQueue.isNotEmpty()) {
            localQueue.forEach { it(viewModel) }
            onViewModelActiveQueue.clear()
        }
    }

    open fun initDependencyInjection() {

    }
}