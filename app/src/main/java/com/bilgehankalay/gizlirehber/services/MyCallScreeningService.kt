package com.bilgehankalay.gizlirehber.services

import android.telecom.Call
import android.telecom.CallScreeningService
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.commons.FORBIDDEN_PHONE_CALL_NUMBER
import com.bilgehankalay.gizlirehber.commons.events.MessageEvent
import com.bilgehankalay.gizlirehber.commons.extensions.parseCountryCode
import com.bilgehankalay.gizlirehber.commons.extensions.removeTelPrefix
import com.bilgehankalay.gizlirehber.commons.utils.NotificationManagerImpl
import org.greenrobot.eventbus.EventBus

class MyCallScreeningService : CallScreeningService() {

    private val notificationManager = NotificationManagerImpl()
    private lateinit var kisilerDb : KisilerDatabase
    private lateinit var kisilerList : List<KisiModel?>

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
        kisilerDb = KisilerDatabase.getirKisilerDatabase(applicationContext)!!
        kisilerList = kisilerDb.kisiDAO().tumKisiler()

        for (kisi in kisilerList){
            if (kisi !=null){
                if (phoneNumber == kisi.telefonNumarasi && kisi.yapilacakIslem == 0){
                    displayToast(String.format("Incoming call from ${kisi.ad} ${kisi.soyad}"))
                }
                else if (phoneNumber == kisi.telefonNumarasi && kisi.yapilacakIslem == 1){
                    //aramayı reddet
                    response.apply {
                        setRejectCall(true)
                        setDisallowCall(true)
                        setSkipCallLog(false)
                        displayToast(String.format("Reddet -- Rejected call from ${kisi.ad} ${kisi.soyad}"))
                    }
                }
                else if (phoneNumber == kisi.telefonNumarasi && kisi.yapilacakIslem == 2){
                    // aramayi gizle
                    response.apply {
                        setRejectCall(false)
                        setDisallowCall(true)
                        setSkipCallLog(false)
                        displayToast(String.format("Gosterme -- Rejected call from ${kisi.ad} ${kisi.soyad}"))
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