package com.mitsuki.armory.httprookie

import com.mitsuki.armory.httprookie.callback.Callback
import com.mitsuki.armory.httprookie.request.Request
import com.mitsuki.armory.httprookie.response.Response
import okhttp3.Call
import java.io.IOException


class Mediator<T : Any>(private val mRequest: Request<T>) : Cloneable {

    private var mCallback: Callback<T>? = null
    private var mRawCall: Call? = null

    @Volatile
    private var isCanceled = false
    private var isExecuted = false

    @Synchronized
    private fun prepareRawCall(): Call? {
        if (isExecuted) throw java.lang.RuntimeException("")
        isExecuted = true
        mRawCall = mRequest.generateCall()
        if (isCanceled) mRawCall?.cancel()
        return mRawCall
    }

    private fun runOnUiThread(func: () -> Unit) {
        UiThreadProvider.runOnUiThread(func)
    }

    fun execute(cb: Callback<T>) {
        this.mCallback = cb
        runOnUiThread { this.mCallback?.onStart() }
        prepareRawCall()?.enqueue(object : okhttp3.Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val code = response.code
                if (code == 404 || code >= 500) {
                    runOnUiThread {
                        error { Response.Fail(Throwable("404 or 50x"), call, response) }
                    }
                    return
                }
                try {
                    mRequest.convert.convertResponse(response).apply {
                        runOnUiThread {
                            success { Response.Success(this, call, response) }
                        }
                    }
                } catch (e: Throwable) {
                    runOnUiThread {
                        error { Response.Fail(e, call, response) }
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { error { Response.Fail(e, call, null) } }
            }
        })
    }

    fun execute(): Response<T> {
        try {
            val response: okhttp3.Response = prepareRawCall()?.execute() ?: throw Throwable("Null response")
            val code = response.code
            if (code == 404 || code >= 500) {
                return Response.Fail(Throwable("404 or 50x"), mRawCall, response)
            }
            return mRequest.convert.convertResponse(response).run {
                Response.Success(this, mRawCall, response)
            }
        } catch (throwable: Throwable) {
            return Response.Fail(throwable, mRawCall, null)
        }
    }

    fun cancel() {
        isCanceled = true
        mRawCall?.cancel()
    }

    fun isCanceled(): Boolean {
        if (isCanceled) return true
        synchronized(this) {
            return mRawCall?.isCanceled() ?: false
        }
    }

    private inline fun error(func: () -> Response.Fail<T>) {
        mCallback?.onError(func())
        mCallback?.onFinish()
    }

    private inline fun success(func: () -> Response.Success<T>) {
        mCallback?.onSuccess(func())
        mCallback?.onFinish()
    }

    public override fun clone() = Mediator(mRequest)
}