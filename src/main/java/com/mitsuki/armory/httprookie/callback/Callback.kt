package com.mitsuki.armory.httprookie.callback

import com.mitsuki.armory.httprookie.response.Response

interface Callback<T : Any> {
    fun onStart()

    fun onSuccess(response: Response.Success<T>)

    fun onError(response: Response.Fail<T>)

    fun onFinish()
}