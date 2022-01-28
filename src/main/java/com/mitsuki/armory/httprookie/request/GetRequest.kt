package com.mitsuki.armory.httprookie.request

import okhttp3.OkHttpClient


class GetRequest<T : Any>(client: OkHttpClient, url: String) : Request<T>(client, url) {
    override fun generateRequest(): okhttp3.Request {
        return generateRequestBuilder().tag(tag).get().url(url()).build()
    }

    override fun generateRequestBuilder(): okhttp3.Request.Builder {
        return okhttp3.Request.Builder().run { appendHeaders(this) }
    }
}