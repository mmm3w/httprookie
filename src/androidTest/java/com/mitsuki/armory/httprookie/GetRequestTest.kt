package com.mitsuki.armory.httprookie


import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mitsuki.armory.httprookie.convert.StringConvert
import com.mitsuki.armory.httprookie.request.urlParams
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class GetRequestTest {

    @Test
    fun callbackTest() {
        val latch = CountDownLatch(3)
        val testStr = Array(3) { "" }
//        HttpRookie.get<String>("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm") {
//            convert = StringConvert()
//            urlParams("tel" to "13858386438")
//            callback(
//                onStart = {
//                    testStr[0] = "onStart"
//                    latch.countDown()
//                },
//                onSuccess = {
//                    testStr[1] = "${it.requireBody()}"
//                    latch.countDown()
//                },
//                onError = {
//                    testStr[1] = "${it.throwable}"
//                    latch.countDown()
//                },
//                onFinish = {
//                    testStr[2] = "onFinish"
//                    latch.countDown()
//                }
//            )
//        }.enqueue()
//
//        latch.await()
//
//        println(testStr[1])
//        assertEquals("onStart", testStr[0])
//        assertNotNull(testStr[1])
//        assertEquals("onFinish", testStr[2])
    }

    @Test
    fun observableTest() {
        val latch = CountDownLatch(3)
        val testStr = Array(3) { "" }
//        HttpRookie.get<String>("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm") {
//            convert = StringConvert()
//            urlParams("tel" to "13858386438")
//        }
//            .enqueueObservable()
//            .subscribeOn(Schedulers.io())
//            .doOnSubscribe {
//                testStr[0] = "onStart"
//                latch.countDown()
//            }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<Response<String?>> {
//                override fun onComplete() {
//                    testStr[2] = "onFinish"
//                    latch.countDown()
//                }
//
//                override fun onSubscribe(d: Disposable?) {
//                }
//
//                override fun onNext(t: Response<String?>) {
//                    testStr[1] = "${t.body}"
//                    latch.countDown()
//                }
//
//                override fun onError(e: Throwable?) {
//                    testStr[1] = "$e"
//                    latch.countDown()
//                }
//
//            })
        latch.await()

        println(testStr[1])
        assertEquals("onStart", testStr[0])
        assertNotNull(testStr[1])
        assertEquals("onFinish", testStr[2])
    }

    @Test
    fun apiTest() {
//        val url = "http://admin.seproofreading.com/api/invite/detail"
//        val latch = CountDownLatch(1)
//        var result = ""
//        HttpRookie.get<String>(url) {
//            urlParams("access_token" to "1111")
//            urlParams("app_os" to "Android")
//            urlParams("app_version" to "1.27.3")
//            urlParams("device_id" to "1.0")
//            convert = StringConvert()
//            callback(
//                onSuccess = {
//                    result = it.requireBody() ?: ""
//                    latch.countDown()
//                },
//                onError = {
//                    result = it.throwable.toString()
//                    latch.countDown()
//                }
//            )
//        }.enqueue()
//
//        latch.await()
//
//        println(result)
    }
}