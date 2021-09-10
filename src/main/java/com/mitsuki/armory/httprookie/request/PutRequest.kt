package com.mitsuki.armory.httprookie.request

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.*

class PutRequest<T : Any>(url: String) : Request<T>(url), HasBody {

    override var type: HasBody.Type = HasBody.Type.NONE
    override val bodyParams: LinkedList<Pair<String, String>> = LinkedList()

    override var mediaType: MediaType? = null
    override lateinit var requestBody: RequestBody
    override lateinit var content: String
    override lateinit var bs: ByteArray
    override lateinit var file: File

    override fun generateRequest(): okhttp3.Request {
        val requestBody = generateRequestBody()
        header(Headers.HEAD_CONTENT_LENGTH, requestBody.contentLength().toString())
        return generateRequestBuilder().tag(tag).put(requestBody).url(url()).build()
    }

    override fun generateRequestBuilder(): okhttp3.Request.Builder {
        return okhttp3.Request.Builder().run { appendHeaders(this) }
    }
}