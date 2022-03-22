package com.mitsuki.armory.httprookie.request

import java.util.*
import kotlin.collections.LinkedHashMap

interface Headers {
    val headers: LinkedHashMap<String, LinkedList<String>>

    companion object {
        const val HEAD_CONTENT_LENGTH = "Content-Length"
    }
}

fun Headers.header(key: String, value: String, replace: Boolean = false) {
    if (key.isEmpty()) return
    if (value.isEmpty()) return
    if (replace) {
        headers[key] = LinkedList<String>().apply { add(value) }
    } else {
        (headers[key] ?: LinkedList<String>().apply { headers[key] = this }).add(value)
    }
}

fun Headers.header(key: String, value: List<String>, replace: Boolean = false) {
    if (key.isEmpty()) return
    if (value.isEmpty()) return
    if (replace) {
        headers[key] = LinkedList<String>(value)
    } else {
        headers[key]?.apply { addAll(value) } ?: let {
            headers[key] = LinkedList<String>(value)
        }
    }
}

fun Headers.header(data: Headers, replace: Boolean = false) {
    if (data.headers.isEmpty()) return
    if (replace) {
        headers.clear()
        headers.putAll(data.headers)
    } else {
        data.headers.forEach { entry ->
            (headers[entry.key] ?: LinkedList<String>().apply { headers[entry.key] = this })
                .addAll(entry.value)
        }
    }
}

fun Headers.appendHeaders(builder: okhttp3.Request.Builder): okhttp3.Request.Builder {
    if (headers.isEmpty()) return builder
    return builder.headers(okhttp3.Headers.Builder().apply {
        headers.forEach { entry -> entry.value.forEach { add(entry.key, it) } }
    }.build())
}

fun Headers.clearHeaders() {
    headers.clear()
}

fun Headers.clearHeaders(name: String) {
    headers[name]?.clear()
}