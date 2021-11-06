package com.bilgehankalay.gizlirehber.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1 != null) {

            if(p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                Toast.makeText(p0,"call started", Toast.LENGTH_LONG).show()
            }
            else if(p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(p0,"call ended", Toast.LENGTH_LONG).show()
            }
            else if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(p0,"call ringing", Toast.LENGTH_LONG).show()
            }
        }
    }
}