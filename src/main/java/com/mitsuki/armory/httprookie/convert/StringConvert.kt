package com.mitsuki.armory.httprookie.convert

import okhttp3.Response

class StringConvert: Convert<String> {
    override fun convertResponse(response: Response): String {
        val str = response.body?.string() ?: ""
        response.close()
        return str
    }
}