package com.mitsuki.armory.httprookie

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mitsuki.armory.httprookie.convert.StringConvert
import com.mitsuki.armory.httprookie.request.params
import com.mitsuki.armory.httprookie.response.Response
import org.junit.Test
import org.junit.runner.RunWith
import java.security.MessageDigest
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class PostRequestTest {

    @Test
    fun normalRequest() {
        val latch = CountDownLatch(1)
        HttpRookie.post<String>("http://jsonplaceholder.typicode.com/posts") {
            convert = StringConvert()
            params("token" to "ttttt")
            params("idd" to "iiiiiiii")
            callback(
                onSuccess = {
                    println("${it.body}")
                    latch.countDown()
                },
                onError = {
                    println("${it.throwable}")
                    latch.countDown()
                }
            )
        }.enqueue()
        latch.await()
    }

}