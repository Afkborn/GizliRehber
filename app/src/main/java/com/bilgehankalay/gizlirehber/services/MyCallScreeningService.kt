package com.bilgehankalay.gizlirehber.services


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService

import androidx.core.app.NotificationCompat
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.commons.extensions.parseCountryCode
import com.bilgehankalay.gizlirehber.commons.extensions.removeTelPrefix
import com.bilgehankalay.gizlirehber.commons.utils.NotificationManagerImpl

import android.app.Notification

import com.bilgehankalay.gizlirehber.Activitys.MainActivity
import android.app.NotificationChannel


import androidx.annotation.RequiresApi

import android.annotation.TargetApi
import com.bilgehankalay.gizlirehber.R


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
            val telNoList : ArrayList<String> = ArrayList()
            telNoList.add(arayanKisi.telefonNumarasi)
            telNoList.add(arayanKisi.ulkeKodu + arayanKisi.telefonNumarasi)
            telNoList.add("+"+ arayanKisi.ulkeKodu + arayanKisi.telefonNumarasi)
            telNoList.add("0" + arayanKisi.telefonNumarasi)



            for (telNo in telNoList){
                if (phoneNumber == telNo && arayanKisi.yapilacakIslem == 0){
                    //bilgi ver
                        val string = getString(R.string.gelen_cagri_goster,arayanKisi.ad,arayanKisi.soyad)
                    displayToast(string)
                    //bildirim ver

                    show_Notification(string)

                }
                else if (phoneNumber == telNo && arayanKisi.yapilacakIslem == 1){
                    //aramayı reddet
                    response.apply {
                        setRejectCall(true)
                        setDisallowCall(true)
                        setSkipCallLog(false)
                    }
                    val string = getString(R.string.gelen_cagri_red,arayanKisi.ad,arayanKisi.soyad)
                    displayToast(string)
                    //bildirim ver
                    show_Notification(string)
                }
                else if (phoneNumber == telNo && arayanKisi.yapilacakIslem == 2){
                    // aramayi gizle
                    response.apply {
                        setRejectCall(false)
                        setDisallowCall(true)
                        setSkipCallLog(false)
                    }
                    val string = getString(R.string.gelen_cagri_gizle,arayanKisi.ad,arayanKisi.soyad)
                    displayToast(string)
                    show_Notification(string)
                }
            }

        }
        return response
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun show_Notification(subhead : String ) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val CHANNEL_ID = "MYCHANNEL"
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)
        val notification = Notification.Builder(
            applicationContext, CHANNEL_ID
        )
            //.setContentText(head)
            .setContentTitle(subhead)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(R.drawable.call)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(1, notification)
    }

    private fun getPhoneNumber(callDetails: Call.Details): String {
        return callDetails.handle.toString().removeTelPrefix().parseCountryCode()
    }

    private fun displayToast(message: String) {
        notificationManager.showToastNotification(applicationContext, message)

    }

    private fun findCaller(phoneNumber: String) : KisiModel? {
        kisilerDb = KisilerDatabase.getirKisilerDatabase(applicationContext)!!
        var kisi = kisilerDb.kisiDAO().telefonNoIleKisiGetir(phoneNumber)
        if (kisi==null){
            kisi = kisilerDb.kisiDAO().fullTelefonNoIleKisiGetir(phoneNumber)
            if (kisi == null){
                val deleteZero = phoneNumber
                kisi = kisilerDb.kisiDAO().telefonNoIleKisiGetir(deleteZero.drop(1))
            }
        }
        return kisi
    }

}


