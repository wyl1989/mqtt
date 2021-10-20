package com.yurnero.mqtt.exchange

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MessageReceiver : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}