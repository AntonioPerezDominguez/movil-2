package com.lrgs18120163.telefonia

import android.telephony.SmsManager
import androidx.lifecycle.ViewModel

class ScreenViewModel : ViewModel() {

    fun SendSMS() {
        val smsMessage = SmsManager.getDefault()
        smsMessage.sendTextMessage(
            "4171785080",
            null,
            "Chinga tu madre",
            null,
            null
        )
    }
}

