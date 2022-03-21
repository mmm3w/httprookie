package com.mitsuki.armory.httprookie.request

import okhttp3.OkHttpClient
import okhttp3.RequestBody

class UnknownRequest<T : Any>(client: OkHttpClient, url: String, private val method: String) :
    Request<T>(client, url) {

    var requestBody: RequestBody? = null

    override fun generateRequest(): okhttp3.Request {
        requestBody?.apply { header(Headers.HEAD_CONTENT_LENGTH, contentLength().toString()) }
        return generateRequestBuilder().tag(tag).url(url()).method(method, requestBody).build()
    }

    override fun generateRequestBuilder(): okhttp3.Request.Builder {
        return okhttp3.Request.Builder().run { appendHeaders(this) }
    }
}