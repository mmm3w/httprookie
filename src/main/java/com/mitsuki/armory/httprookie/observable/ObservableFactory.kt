package com.mitsuki.armory.httprookie.observable

import com.mitsuki.armory.httprookie.Mediator
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observable

internal object ObservableFactory {
    fun <T:Any> obtain(
        mediator: Mediator<T>,
        isAsync: Boolean = true
    ): Observable<Response<T>> {
        return if (isAsync) {
            EnqueueObservable(mediator)
        } else {
            ExecuteObservable(mediator)
        }
    }

}