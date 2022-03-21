package com.mitsuki.armory.httprookie

import com.mitsuki.armory.httprookie.request.*
import okhttp3.OkHttpClient

/**
 * java 支持
 */
object HttpRookie : UrlParams, Headers {

    //公共url参数
    override val urlParams: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

    //公共header
    override val headers: LinkedHashMap<String, String> = LinkedHashMap()

    fun <T : Any> get(
        client: OkHttpClient,
        url: String,
        func: (GetRequest<T>.() -> Unit)? = null
    ): GetRequest<T> =
        client.get(url, func)

    fun <T : Any> post(
        client: OkHttpClient,
        url: String,
        func: (PostRequest<T>.() -> Unit)? = null
    ): PostRequest<T> =
        client.post(url, func)

    fun <T : Any> put(
        client: OkHttpClient,
        url: String,
        func: (PutRequest<T>.() -> Unit)? = null
    ): PutRequest<T> =
        client.put(url, func)

    fun <T : Any> patch(
        client: OkHttpClient,
        url: String,
        func: (PatchRequest<T>.() -> Unit)? = null
    ): PatchRequest<T> =
        client.patch(url, func)

    fun <T : Any> delete(
        client: OkHttpClient,
        url: String,
        func: (DeleteRequest<T>.() -> Unit)? = null
    ): DeleteRequest<T> =
        client.delete(url, func)

    fun <T : Any> method(
        client: OkHttpClient,
        url: String,
        method: String,
        func: (UnknownRequest<T>.() -> Unit)? = null
    ): UnknownRequest<T> =
        client.method(url, method, func)

    fun cancel(client: OkHttpClient, tag: Any) {
        client.cancel(tag)
    }

    fun cancelAll(client: OkHttpClient) {
        client.cancelAll()
    }
}
