package com.draco.pdnsqs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings

class SecureSettings(private val context: Context) {
    companion object {
        const val SETTINGS_GLOBAL_PRIVATE_DNS_MODE = "private_dns_mode"
        const val ON = "hostnane"
        const val AUTO = "opportunistic"
        const val OFF = "off"
    }

    fun granted() = context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) ==
            PackageManager.PERMISSION_GRANTED

    fun togglePDNS(state: String) {
        Settings.Global.putString(
            context.contentResolver,
            SETTINGS_GLOBAL_PRIVATE_DNS_MODE,
            state
        )
    }

    fun state() = Settings.Global.getString(
        context.contentResolver,
        SETTINGS_GLOBAL_PRIVATE_DNS_MODE
    )
}