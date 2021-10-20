package com.yurnero.mqtt.exchange

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yurnero.mqtt.util.Utils

class MessageManager private constructor() {
    companion object {
        private const val TAG = "MessageManager"
        private val messageManager = MessageManager()

        fun getInstance(): MessageManager {
            return messageManager
        }
    }

    private var mqttService: MQTTService? = null

    private var isBind = false

    private val newMessage: MutableLiveData<String> = MutableLiveData("")

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d(TAG, "mqtt service connect")
            isBind = true

            val binder = p1 as MQTTService.MqttBinder
            mqttService = binder.service
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(TAG, "mqtt service disconnect")
            isBind = false
        }

    }

    fun init() {
        val context: Context = Utils.getApp()
        if (!isBind) {
            context.bindService(
                Intent(context, MQTTService::class.java),
                serviceConn,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    fun publishMessage(msg: String) {
        mqttService?.publish(msg)
    }

    fun newMessage(byteArray: ByteArray){
        newMessage.value = String(byteArray)
    }

    fun getNewMessage():LiveData<String> {
       return newMessage
    }
}