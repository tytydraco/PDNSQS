package com.draco.pdnsqs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings

class SecureSettings(private val context: Context) {
    companion object {
        const val SETTINGS_GLOBAL_PRIVATE_DNS_MODE = "private_dns_mode"
    }

    fun granted() = context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) ==
            PackageManager.PERMISSION_GRANTED

    fun togglePDNS(state: Boolean) {
        val value = when (state) {
            true -> "hostname"
            false -> "off"
        }

        Settings.Global.putString(
            context.contentResolver,
            SETTINGS_GLOBAL_PRIVATE_DNS_MODE,
            value
        )
    }

    fun state() = Settings.Global.getString(
        context.contentResolver,
        SETTINGS_GLOBAL_PRIVATE_DNS_MODE
    ) == "hostname"
}