package com.ekoapp.sample.core.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


infix fun Disposable?.into(compositeDisposable: CompositeDisposable): Boolean? {
	return if (this != null) compositeDisposable.add(this)
	else null
}

fun <T> emptyWhenNoFirstDataInTimeLimit(
	timeoutValue: Long = 10L,
	timeoutUnit: TimeUnit = TimeUnit.SECONDS,
	onTimeout: () -> T
): FlowableTransformer<T, T> {
	return FlowableTransformer { upstream ->
		val upStreamShare = upstream.share()
		val empty = Flowable.timer(timeoutValue, timeoutUnit)
			.doOnSubscribe { Timber.v("start timer with $timeoutValue second") }
			.map<T> { onTimeout() }
			.take(1)
			.takeUntil(upStreamShare)
		upStreamShare.mergeWith(empty)
	}
}

fun <T> doOnNoFirstDataInTimeLimit(
	timeoutValue: Long = 10L,
	timeoutUnit: TimeUnit = TimeUnit.SECONDS,
	onTimeout: () -> Unit
): FlowableTransformer<T, T> {
	return FlowableTransformer { upstream ->
		val upStreamShare = upstream.share()
		val empty = Flowable.timer(timeoutValue, timeoutUnit)
			.doOnSubscribe { Timber.v("start timer with $timeoutValue second") }
			.doOnNext { onTimeout() }
			.take(1)
			.takeUntil(upStreamShare)
		upStreamShare.mergeWith(empty.ignoreElements())
	}
}

fun <T> Flowable<T>.doOnErrorPrintMoreInfo(): Flowable<T> {
	val breadcrumb = BreadcrumbException()
	return this.onErrorResumeNext { error: Throwable ->
		throw CompositeException(error, breadcrumb)
	}
}

fun Completable.doOnErrorPrintMoreInfo(): Completable {
	val breadcrumb = BreadcrumbException()
	return this.onErrorResumeNext { error: Throwable ->
		throw CompositeException(error, breadcrumb)
	}
}

fun <T> Single<T>.doOnErrorPrintMoreInfo(): Single<T> {
	val breadcrumb = BreadcrumbException()
	return this.onErrorResumeNext { error: Throwable ->
		throw CompositeException(error, breadcrumb)
	}
}

object ScheduleFactory {
	val debounceSchedule = Schedulers.io()
}

fun <T> Flowable<T>.safeStream(): Flowable<T> {
	return debounce(400, TimeUnit.MILLISECONDS, ScheduleFactory.debounceSchedule)
		.distinctUntilChanged()
}

class BreadcrumbException : Exception()