package com.mitsuki.armory.httprookie.request

import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLConnection
import java.util.*

interface HasBody {
    var type: Type
    var mediaType: MediaType?
    var requestBody: RequestBody
    var content: String
    var bs: ByteArray
    var file: File

    val bodyParams: LinkedList<Pair<String, String>>

    enum class Type {
        STRING, JSON, BYTES, FILE, CUSTOM, NONE
    }

    companion object {
        val STREAM = "application/octet-stream".toMediaType()
        val JSON = "application/json;charset=utf-8".toMediaType()
        val TEXT = "text/plain;charset=utf-8".toMediaType()
    }
}

fun HasBody.generateRequestBody(): RequestBody {
    return when (type) {
        HasBody.Type.STRING, HasBody.Type.JSON -> content.toRequestBody(mediaType)
        HasBody.Type.BYTES -> bs.toRequestBody(mediaType)
        HasBody.Type.FILE -> file.asRequestBody(mediaType)
        HasBody.Type.CUSTOM -> requestBody
        HasBody.Type.NONE -> generateParamsRequestBody()
    }
}

fun HasBody.string(data: String, mediaType: MediaType? = null) {
    this.type = HasBody.Type.STRING
    this.content = data
    this.mediaType = mediaType ?: HasBody.TEXT
}

fun HasBody.file(data: File, mediaType: MediaType? = null) {
    this.type = HasBody.Type.FILE
    this.file = data
    this.mediaType = mediaType ?: (data.name.fileNameToMediaType()?.toMediaType() ?: HasBody.STREAM)
}

fun HasBody.bytes(data: ByteArray) {
    this.type = HasBody.Type.BYTES
    this.bs = data
    this.mediaType = HasBody.STREAM
}

fun HasBody.json(data: String) {
    this.type = HasBody.Type.JSON
    this.content = data
    this.mediaType = HasBody.JSON
}

fun HasBody.params(key: String, value: String) {
    this.type = HasBody.Type.NONE
    bodyParams.add(key to value)
}

fun HasBody.requestBody(data: RequestBody) {
    this.type = HasBody.Type.CUSTOM
    this.requestBody = data
}

internal fun String.fileNameToMediaType(): String? {
    return URLConnection.getFileNameMap().getContentTypeFor(replace("#", ""))
}

internal fun HasBody.generateParamsRequestBody(): RequestBody {
    return FormBody.Builder().apply {
        for (pair in bodyParams) addEncoded(pair.first, pair.second)
    }.build()
}