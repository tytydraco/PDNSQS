package com.draco.pdnsqs

import android.app.AlertDialog
import android.content.Intent
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QSTileService : TileService() {
    private lateinit var secureSettings: SecureSettings
    private lateinit var dialog: AlertDialog

    override fun onBind(intent: Intent?): IBinder? {
        secureSettings = SecureSettings(applicationContext)

        dialog = AlertDialog.Builder(applicationContext)
            .setTitle(R.string.permission_title)
            .setMessage(R.string.permission_message)
            .setPositiveButton(R.string.permission_dismiss) { _, _ -> }
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