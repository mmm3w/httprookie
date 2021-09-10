package com.mitsuki.armory.httprookie.request

import java.net.URLEncoder

interface UrlParams {
    val urlParams: LinkedHashMap<String, MutableList<String>>
}

fun UrlParams.urlParams(key: String, value: String) {
    if (key.isEmpty()) return
    if (value.isEmpty()) return
    urlParams[key]?.add(value) ?: {
        urlParams[key] = arrayListOf(value)
    }()
}

fun UrlParams.urlParams(data: UrlParams) {
    if (data.urlParams.isEmpty()) return
    urlParams.putAll(data.urlParams)
}

fun UrlParams.appendParams(url: String): String {
    return urlBuilder(url) {
        for (params in urlParams) {
            for (item in params.value) {
                param(params.key, item)
            }
        }
    }.build()
}

fun UrlParams.clearUrlParams() {
    urlParams.clear()
}

inline fun urlBuilder(base: String, action: UrlBuilder.() -> Unit) =
    UrlBuilder(base).apply(action)

class UrlBuilder(url: String) {
    private val base: StringBuilder = StringBuilder(url)

    fun param(key: String, value: String) {
        //TODO:这一块可能要调整
        if (base.contains("?")) {
            base.append("&")
        } else {
            base.append("?")
        }.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"))
    }

    fun build(): String = base.toString()
}
