package com.mitsuki.armory.httprookie

import android.os.Handler
import android.os.Looper
import com.mitsuki.armory.httprookie.request.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpRookie : UrlParams, Headers {

    //公共url参数
    override val urlParams: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

    //公共header
    override val headers: LinkedHashMap<String, String> = LinkedHashMap()


    val client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor { onRequestAfter(it.proceed(it.request())) }
            .addInterceptor { it.proceed(onRequestBefore(it, it.request())) }
            .apply {
                for (item in mInterceptors) addInterceptor(item)
                for (item in mNetworkInterceptors) addNetworkInterceptor(item)
            }
            .apply(configOkHttp)
            .build()
    }


    fun <T : Any> get(url: String, func: (GetRequest<T>.() -> Unit)? = null): GetRequest<T> =
        GetRequest<T>(url).apply { func?.let { this.it() } }

    fun <T : Any> post(url: String, func: (PostRequest<T>.() -> Unit)? = null): PostRequest<T> =
        PostRequest<T>(url).apply { func?.let { this.it() } }

    fun <T : Any> put(url: String, func: (PutRequest<T>.() -> Unit)? = null): PutRequest<T> =
        PutRequest<T>(url).apply { func?.let { this.it() } }

    fun <T : Any> delete(
        url: String,
        func: (DeleteRequest<T>.() -> Unit)? = null
    ): DeleteRequest<T> =
        DeleteRequest<T>(url).apply { func?.let { this.it() } }

    fun cancel(tag: Any) {
        for (call in client.dispatcher.queuedCalls()) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
        for (call in client.dispatcher.runningCalls()) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
    }

    fun cancelAll() {
        for (call in client.dispatcher.queuedCalls()) {
            call.cancel()
        }
        for (call in client.dispatcher.runningCalls()) {
            call.cancel()
        }
    }

    /**********************************************************************************************/
    private val mInterceptors: MutableList<Interceptor> by lazy { ArrayList<Interceptor>() }
    private val mNetworkInterceptors: MutableList<Interceptor> by lazy { ArrayList<Interceptor>() }

    fun addInterceptor(interceptor: Interceptor) {
        mInterceptors.add(interceptor)
    }

    fun addNetworkInterceptor(interceptor: Interceptor) {
        mNetworkInterceptors.add(interceptor)
    }

    var configOkHttp: OkHttpClient.Builder.() -> Unit = {}

    var onRequestBefore: (chain: Interceptor.Chain, request: okhttp3.Request) -> okhttp3.Request =
        { _, request -> request }

    var onRequestAfter: (response: okhttp3.Response) -> okhttp3.Response = { it }
}
