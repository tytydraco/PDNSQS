package com.draco.pdnsqs

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QSTileService : TileService() {
    companion object {
        const val ADB_COMMAND = "adb shell pm grant ${BuildConfig.APPLICATION_ID} android.permission.WRITE_SECURE_SETTINGS"
    }

    private lateinit var secureSettings: SecureSettings
    private lateinit var dialog: AlertDialog

    private lateinit var clipboardManager: ClipboardManager

    override fun onBind(intent: Intent?): IBinder? {
        secureSettings = SecureSettings(applicationContext)
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        dialog = AlertDialog.Builder(applicationContext)
            .setTitle(R.string.permission_title)
            .setMessage(R.string.permission_message)
            .setPositiveButton(R.string.permission_dismiss) { _, _ -> }
            .setNeutralButton(R.string.permission_copy) { _, _ ->
                val clipData = ClipData.newPlainText(
                    getString(R.string.permission_title),
                    ADB_COMMAND
                )
                clipboardManager.setPrimaryClip(clipData)
            }
            .create()

        return super.onBind(intent)
    }

    private fun updateState() {
        qsTile.state = when (secureSettings.state()) {
            true -> Tile.STATE_ACTIVE
            false -> Tile.STATE_INACTIVE
        }

        qsTile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateState()
    }

    override fun onClick() {
        super.onClick()

        if (!secureSettings.granted()) {
            showDialog(dialog)
            return
        }

        secureSettings.togglePDNS(qsTile.state == Tile.STATE_INACTIVE)
        updateState()
    }
}