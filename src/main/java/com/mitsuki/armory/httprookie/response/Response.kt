package com.mitsuki.armory.httprookie.response

import okhttp3.Call

sealed class Response<T : Any>(
    val rawCall: Call?,
    val rawResponse: okhttp3.Response?
) {

    class Success<T : Any>(val body: T?, rawCall: Call?, rawResponse: okhttp3.Response) :
        Response<T>(rawCall, rawResponse) {

        fun requireBody(): T {
            return body ?: throw RuntimeException("$this`s content is null")
        }

        fun requireRawResponse(): okhttp3.Response {
            return rawResponse ?: throw RuntimeException("$this`s rawResponse is null")
        }
    }

    class Fail<T : Any>(val throwable: Throwable, rawCall: Call?, rawResponse: okhttp3.Response?) :
        Response<T>(rawCall, rawResponse)
}