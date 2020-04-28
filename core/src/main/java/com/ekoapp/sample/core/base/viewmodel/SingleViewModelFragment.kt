package com.ekoapp.sample.core.base.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekoapp.sample.core.base.BaseFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class SingleViewModelFragment<VM : ViewModel> : BaseFragment(), HasSingleViewModel<VM> {
    @Inject
    lateinit var vmFactory: SingleViewModelFactory<VM>
    protected var viewModel: VM? = null
    private val onViewModelActiveQueue: MutableList<(VM) -> Unit> = mutableListOf()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity(), vmFactory).get(getViewModelClass())
        this.viewModel = viewModel
        bindViewModel(viewModel)
        invokeViewModelActiveQueue(viewModel)
    }

    override fun onAttach(context: Context) {
        initDependencyInjection()
        super.onAttach(context)
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