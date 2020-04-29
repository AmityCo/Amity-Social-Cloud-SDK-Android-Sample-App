package com.ekoapp.sample.core.ui.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import timber.log.Timber

fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, render: (T) -> Unit) {
	observe(lifecycleOwner, Observer {
		if (it != null) render(it)
		else Timber.w("observeNotNull is null")
	})
}

fun <T> Publisher<T>.toLiveData(): LiveData<T> {
	return LiveDataReactiveStreams.fromPublisher(this)
}

fun <T> createFuncLive(invokeFunc: () -> Unit, cancelFunc: () -> Unit): LiveData<T> {
	return Flowable.create<T>({ emitter ->
		invokeFunc()
		emitter.setCancellable { cancelFunc() }
	}, BackpressureStrategy.LATEST).toLiveData()
}

