package com.yurnero.mqtt

import android.app.Application
import com.yurnero.mqtt.util.Utils

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}