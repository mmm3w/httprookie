package com.mitsuki.armory.httprookie.convert

interface Convert<T> {
    fun convertResponse(response: okhttp3.Response): T?
}