package com.ekoapp.sample.core.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

fun <T> Completable.asFlowable(
	startWith: () -> T,
	complete: () -> T,
	onErrorReturn: (Throwable) -> T
): Flowable<T> {
	val startStream = Flowable.fromCallable { startWith() }
	val completeStream = andThen(Flowable.fromCallable { complete() })
	return Flowable.merge(startStream, completeStream)
		.onErrorReturn { onErrorReturn(it) }
}

fun <T> Completable.asFlowable(
	startWith: () -> T,
	onErrorReturn: (Throwable) -> T
): Flowable<T> {
	val startStream = Flowable.fromCallable(startWith)
	val completeStream = this.toFlowable<T>()
	return startStream.mergeWith(completeStream)
		.onErrorReturn { onErrorReturn(it) }
}

fun <A, T> Single<A>.asFlowable(
	startWith: () -> T,
	complete: (A) -> T,
	onErrorReturn: (Throwable) -> T
): Flowable<T> {
	val startStream = Flowable.fromCallable { startWith() }
	val completeStream = map { complete(it) }.toFlowable()
	return Flowable.merge(startStream, completeStream)
		.onErrorReturn { onErrorReturn(it) }
}

fun <T> Flowable<T>.logNext(): Flowable<T> {
	return doOnNext { Timber.v("\n=====\n$it\n=====\n") }
}