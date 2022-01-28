package com.mitsuki.armory.httprookie

import com.mitsuki.armory.httprookie.request.DeleteRequest
import com.mitsuki.armory.httprookie.request.GetRequest
import com.mitsuki.armory.httprookie.request.PostRequest
import com.mitsuki.armory.httprookie.request.PutRequest
import okhttp3.OkHttpClient

fun <T : Any> OkHttpClient.get(
    url: String,
    func: (GetRequest<T>.() -> Unit)? = null
): GetRequest<T> {
    return GetRequest<T>(this, url).apply { func?.let { this.it() } }
}

fun <T : Any> OkHttpClient.post(
    url: String,
    func: (PostRequest<T>.() -> Unit)? = null
): PostRequest<T> =
    PostRequest<T>(this, url).apply { func?.let { this.it() } }

fun <T : Any> OkHttpClient.put(
    url: String,
    func: (PutRequest<T>.() -> Unit)? = null
): PutRequest<T> =
    PutRequest<T>(this, url).apply { func?.let { this.it() } }

fun <T : Any> OkHttpClient.delete(
    url: String,
    func: (DeleteRequest<T>.() -> Unit)? = null
): DeleteRequest<T> =
    DeleteRequest<T>(this, url).apply { func?.let { this.it() } }

fun OkHttpClient.cancel(tag: Any) {
    for (call in dispatcher.queuedCalls()) {
        if (tag == call.request().tag()) {
            call.cancel()
        }
    }
    for (call in dispatcher.runningCalls()) {
        if (tag == call.request().tag()) {
            call.cancel()
        }
    }
}

fun OkHttpClient.cancelAll() {
    for (call in dispatcher.queuedCalls()) {
        call.cancel()
    }
    for (call in dispatcher.runningCalls()) {
        call.cancel()
    }
}