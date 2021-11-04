package com.bilgehankalay.gizlirehber.services

import android.telecom.Call
import android.telecom.CallScreeningService
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Fragments.AnaSayfaFragment
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.commons.CONSTANT_KISILER_LIST
import com.bilgehankalay.gizlirehber.commons.FORBIDDEN_PHONE_CALL_NUMBER
import com.bilgehankalay.gizlirehber.commons.events.MessageEvent
import com.bilgehankalay.gizlirehber.commons.extensions.parseCountryCode
import com.bilgehankalay.gizlirehber.commons.extensions.removeTelPrefix
import com.bilgehankalay.gizlirehber.commons.utils.NotificationManagerImpl
import org.greenrobot.eventbus.EventBus

class MyCallScreeningService : CallScreeningService() {

    private val notificationManager = NotificationManagerImpl()

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = getPhoneNumber(callDetails)
        var response = CallResponse.Builder()
        response = handlePhoneCall(response, phoneNumber)

        respondToCall(callDetails, response.build())
    }

    private fun handlePhoneCall(
            response: CallResponse.Builder,
            phoneNumber: String
    ): CallResponse.Builder {
        if (phoneNumber == FORBIDDEN_PHONE_CALL_NUMBER) {
            response.apply {
                setRejectCall(true)
                setDisallowCall(true)
                setSkipCallLog(false)
                //displayToast(String.format("Rejected call from %s", phoneNumber))
            }
        }
        else {
            for (kisi in CONSTANT_KISILER_LIST){
                if (kisi !=null){
                    if (phoneNumber == kisi.telefonNumarasi){
                        displayToast(String.format("Incoming call from ${kisi.ad} ${kisi.soyad}"))
                    }
                }
            }

        }
        return response
    }

    private fun getPhoneNumber(callDetails: Call.Details): String {
        return callDetails.handle.toString().removeTelPrefix().parseCountryCode()
    }

    private fun displayToast(message: String) {
        notificationManager.showToastNotification(applicationContext, message)
        EventBus.getDefault().post(MessageEvent(message))
    }

}