package com.mitsuki.armory.httprookie.observable

import com.mitsuki.armory.httprookie.Mediator
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.plugins.RxJavaPlugins

internal class ExecuteObservable<T : Any>(private val mMediator: Mediator<T>) :
    Observable<Response<T>>() {

    override fun subscribeActual(observer: Observer<in Response<T>>?) {
        val mediator = mMediator.clone()
        observer?.onSubscribe(CallDisposable(mediator))
        var terminated = false
        try {
            val response: Response<T> = mediator.execute()
            if (!mediator.isCanceled()) {
                observer?.onNext(response)
            }
            if (!mediator.isCanceled()) {
                terminated = true
                observer?.onComplete()
            }
        } catch (t: Throwable) {
            Exceptions.throwIfFatal(t)
            if (terminated) {
                RxJavaPlugins.onError(t)
            } else if (!mediator.isCanceled()) {
                try {
                    observer?.onError(t)
                } catch (inner: Throwable) {
                    Exceptions.throwIfFatal(inner)
                    RxJavaPlugins.onError(CompositeException(t, inner))
                }
            }
        }
    }


    private class CallDisposable<T : Any>(private val mMediator: Mediator<T>) : Disposable {
        override fun isDisposed(): Boolean {
            return mMediator.isCanceled()
        }

        override fun dispose() {
            mMediator.cancel()
        }
    }
}