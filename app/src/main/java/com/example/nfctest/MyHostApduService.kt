package com.example.nfctest

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

/**
 * Created by Jaehyeon on 2023/10/13.
 */
class MyHostApduService : HostApduService(){

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        TODO("Not yet implemented")
    }

    override fun onDeactivated(reason: Int) {
        TODO("Not yet implemented")
    }
}