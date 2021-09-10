package com.mitsuki.armory.httprookie.request


class GetRequest<T : Any>(url: String) : Request<T>(url) {
    override fun generateRequest(): okhttp3.Request {
        return generateRequestBuilder().tag(tag).get().url(url()).build()
    }

    override fun generateRequestBuilder(): okhttp3.Request.Builder {
        return okhttp3.Request.Builder().run { appendHeaders(this) }
    }
}