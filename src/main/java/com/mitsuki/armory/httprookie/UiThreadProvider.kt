package com.mitsuki.armory.httprookie

import android.os.Handler
import android.os.Looper

internal object UiThreadProvider {
    private val mDelivery: Handler? = try {
        Handler(Looper.getMainLooper())
    } catch (inner: Throwable) {
        null
    }

    fun runOnUiThread(action: () -> Unit) {
        if (mDelivery == null) {
            action()
        } else {
            mDelivery.post { action() }
        }
    }

}