package com.mitsuki.armory.httprookie.request

interface Headers {
    val headers: LinkedHashMap<String, String>

    companion object {
        const val HEAD_CONTENT_LENGTH = "Content-Length"
    }
}

fun Headers.header(key: String, value: String) {
    if (key.isEmpty()) return
    if (value.isEmpty()) return
    headers[key] = value
}

fun Headers.header(data: Headers) {
    if (data.headers.isEmpty()) return
    headers.putAll(data.headers)
}

fun Headers.appendHeaders(builder: okhttp3.Request.Builder): okhttp3.Request.Builder {
    if (headers.isEmpty()) return builder
    return builder.headers(okhttp3.Headers.Builder().apply {
        for (header in headers) add(header.key, header.value)
    }.build())
}

fun Headers.clearHeaders() {
    headers.clear()
}