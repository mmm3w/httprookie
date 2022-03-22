package com.mitsuki.armory.httprookie.request

import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.Mediator
import com.mitsuki.armory.httprookie.callback.Callback
import com.mitsuki.armory.httprookie.callback.DefaultCallback
import com.mitsuki.armory.httprookie.convert.Convert
import com.mitsuki.armory.httprookie.observable.EnqueueObservable
import com.mitsuki.armory.httprookie.observable.ObservableFactory
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observable
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import kotlin.collections.LinkedHashMap

@Suppress("MemberVisibilityCanBePrivate")
abstract class Request<T : Any>(val client: OkHttpClient, val rawUrl: String) : UrlParams, Headers {
    val mHttpRookie: HttpRookie = HttpRookie

    lateinit var mMediator: Mediator<T>
    lateinit var convert: Convert<T>

    var tag: Any? = null
    var rawRequest: Request? = null
    var callback: Callback<T> = DefaultCallback()

    override val headers: LinkedHashMap<String, LinkedList<String>> = LinkedHashMap()
    override val urlParams: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

    init {
        //默认的Accept-Language
        //默认的User-Agent
        header(mHttpRookie)
        urlParams(mHttpRookie)
    }

    fun callback(
        onStart: (() -> Unit)? = null,
        onSuccess: ((response: Response.Success<T>) -> Unit)? = null,
        onError: ((response: Response.Fail<T>) -> Unit)? = null,
        onFinish: (() -> Unit)? = null
    ) {
        this.callback = DefaultCallback(onStart, onSuccess, onError, onFinish)
    }

    fun callback(callback: Callback<T>) {
        this.callback = callback
    }

    abstract fun generateRequest(): Request

    abstract fun generateRequestBuilder(): Request.Builder

    fun url(): String = appendParams(rawUrl)

    private fun mediator(): Mediator<T> {
        if (!this::mMediator.isInitialized) {
            mMediator = Mediator(this)
        }
        return mMediator
    }

    fun generateCall(): Call {
        return generateRequest().let {
            rawRequest = it
            client.newCall(it)
        }
    }

    //异步回调形式
    fun enqueue() {
        mediator().execute(callback)
    }

    //同步调用
    fun execute(): Response<T> {
        return mediator().execute()
    }

    fun enqueueObservable(): Observable<Response<T>> {
        return ObservableFactory.obtain(mediator())
    }

    fun executeObservable(): Observable<Response<T>> {
        return ObservableFactory.obtain(mediator(), isAsync = false)
    }
}