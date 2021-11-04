package com.bilgehankalay.gizlirehber.Activitys

import android.content.Intent
import android.os.Bundle
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.commons.base.BaseActivity
import com.bilgehankalay.gizlirehber.commons.events.PermissionDenied
import com.bilgehankalay.gizlirehber.commons.events.PhoneManifestPermissionsEnabled
import com.bilgehankalay.gizlirehber.commons.utils.CapabilitiesRequestorImpl
import com.bilgehankalay.gizlirehber.commons.utils.ManifestPermissionRequesterImpl
import pub.devrel.easypermissions.AppSettingsDialog
import java.lang.ref.WeakReference

class MainActivity : BaseActivity() {
    private val manifestPermissionRequestor = ManifestPermissionRequesterImpl()
    private val capabilitiesRequestor = CapabilitiesRequestorImpl()
    private var checkCapabilitiesOnResume = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listenUiEvents()
        //
        manifestPermissionRequestor.activity = WeakReference(this)
        capabilitiesRequestor.activityReference = WeakReference(this)
        //
        manifestPermissionRequestor.getPermissions()
    }
    override fun onResume() {
        super.onResume()
        if (checkCapabilitiesOnResume) {
            capabilitiesRequestor.invokeCapabilitiesRequest()
            checkCapabilitiesOnResume = false
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        manifestPermissionRequestor.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        capabilitiesRequestor.onActivityResult(requestCode, resultCode, data)
    }

    private fun listenUiEvents() {
        uiEvent.observe(this, {
            when (it) {
                is PermissionDenied -> {
                    checkCapabilitiesOnResume = true
                    // This will display a dialog directing them to enable the permission in app settings.
                    AppSettingsDialog.Builder(this).build().show()
                }
                is PhoneManifestPermissionsEnabled -> {
                    // now we can load phone dialer capabilities requests
                    capabilitiesRequestor.invokeCapabilitiesRequest()
                }
                else -> {
                    // NOOP
                }
            }
        })
    }

}