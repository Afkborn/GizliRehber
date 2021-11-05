package com.bilgehankalay.gizlirehber.services

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.R
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
        val arayanKisi = findCaller(phoneNumber)


        var callDirection = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callDirection = callDetails.callDirection
        }
        //0 INCOMING, 1 OUTGOING
        //TODO arama geliyorsa yazılar çıksın, hata çöz

        response = handlePhoneCall(response, phoneNumber,arayanKisi,callDirection)

        respondToCall(callDetails, response.build())
    }

    private fun handlePhoneCall(
            response: CallResponse.Builder,
            phoneNumber: String,
            arayanKisi : KisiModel?,
            callDirection : Int
    ): CallResponse.Builder {
        if (arayanKisi != null && callDirection == 0){
                if (phoneNumber == arayanKisi.telefonNumarasi && arayanKisi.yapilacakIslem == 0){
                    //bilgi ver
                    displayToast(getString(R.string.gelen_cagri_goster,arayanKisi.ad,arayanKisi.soyad))
                }
                else if (phoneNumber == arayanKisi.telefonNumarasi && arayanKisi.yapilacakIslem == 1){
                    //aramayı reddet
                    response.apply {
                        setRejectCall(true)
                        setDisallowCall(true)
                        setSkipCallLog(false)
                    }
                    displayToast(getString(R.string.gelen_cagri_red,arayanKisi.ad,arayanKisi.soyad))
                }
                else if (phoneNumber == arayanKisi.telefonNumarasi && arayanKisi.yapilacakIslem == 2){
                    // aramayi gizle
                    response.apply {
                        setRejectCall(false)
                        setDisallowCall(true)
                        setSkipCallLog(false)
                    }
                    displayToast(getString(R.string.gelen_cagri_gizle,arayanKisi.ad,arayanKisi.soyad))
                }
        }
        return response
    }

    private fun getPhoneNumber(callDetails: Call.Details): String {
        return callDetails.handle.toString().removeTelPrefix().parseCountryCode()
    }

    private fun displayToast(message: String) {
        notificationManager.showToastNotification(applicationContext, message)
        EventBus.getDefault().post(message)
    }

    private fun findCaller(phoneNumber: String) : KisiModel? {
        kisilerDb = KisilerDatabase.getirKisilerDatabase(applicationContext)!!
        val kisi = kisilerDb.kisiDAO().telefonNoIleKisiGetir(phoneNumber)
        return kisi
    }
}


