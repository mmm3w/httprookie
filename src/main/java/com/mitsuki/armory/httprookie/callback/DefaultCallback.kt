package com.mitsuki.armory.httprookie.callback

import com.mitsuki.armory.httprookie.response.Response


class DefaultCallback<T : Any>(
    var onStart: (() -> Unit)? = null,
    var onSuccess: ((response: Response.Success<T>) -> Unit)? = null,
    var onError: ((response: Response.Fail<T>) -> Unit)? = null,
    var onFinish: (() -> Unit)? = null
) : Callback<T> {
    override fun onStart() {
        onStart?.invoke()
    }

    override fun onSuccess(response: Response.Success<T>) {
        onSuccess?.invoke(response)
    }

    override fun onError(response: Response.Fail<T>) {
        onError?.invoke(response)
    }

    override fun onFinish() {
        onFinish?.invoke()
    }
}