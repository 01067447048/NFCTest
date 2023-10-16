package com.example.nfctest

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

/**
 * Created by Jaehyeon on 2023/10/13.
 */
class MyHostApduService : HostApduService(){

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val value = commandApdu?.decodeToString()
        Log.d(MyHostApduService::class.java.simpleName, value.toString())

        return "Hello World!".toByteArray()
    }

    override fun onDeactivated(reason: Int) {
        when (reason) {
            DEACTIVATION_LINK_LOSS -> {
                Log.e(MyHostApduService::class.java.simpleName, "$reason / Deactivation Link Loss")
            }

            DEACTIVATION_DESELECTED -> {
                Log.e(MyHostApduService::class.java.simpleName, "$reason / Deactivation Deselected")
            }

            else -> {
                throw Throwable("Unknown Error")
            }
        }
    }
}