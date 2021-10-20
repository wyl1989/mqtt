package com.yurnero.mqtt.exchange

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.yurnero.mqtt.R
import com.yurnero.mqtt.ui.MainActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MQTTService : Service() {
    companion object {
        private const val TAG = "MQTTService"

        private const val MQTT_SERVER_HOST = "ws://110.42.232.247:8083/mqtt"

        private const val MQTT_SERVER_TOPIC = "/topic/qos0"

        private const val MQTT_SERVER_QOS = 0

        private const val MQTT_SERVER_CLIENT_ID = "yurnero_android"
    }

    private lateinit var mqttAndroidClient: MqttAndroidClient

    private lateinit var mqttConnectOptions: MqttConnectOptions

    private val binder = MqttBinder()

    private val listener = MqttListener()

    override fun onCreate() {
        Log.d(TAG, "mqtt onCreate")
        super.onCreate()
        initMqtt()
        notification()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG, "mqtt onBind")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "mqtt onStartCommand")
        return START_NOT_STICKY
    }

    private fun initMqtt() {
        mqttAndroidClient = MqttAndroidClient(this, MQTT_SERVER_HOST, MQTT_SERVER_CLIENT_ID)
        mqttAndroidClient.setCallback(listener)
        mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isCleanSession = true
        //单位都是秒，从源码看有默认值
        mqttConnectOptions.connectionTimeout = 10
        mqttConnectOptions.keepAliveInterval = 20

        //还需要判断网络
        if (!mqttAndroidClient.isConnected) {
            try {
                mqttAndroidClient.connect(mqttConnectOptions, null, listener)
            } catch (e: MqttException) {
                Log.e(TAG, "connect mqtt exception ${e.message}")
            }
        }
    }

    private fun notification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val channel =
            NotificationChannel("1", "channel_name", NotificationManager.IMPORTANCE_HIGH)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val builder: Notification.Builder = Notification.Builder(this, "1")
        builder.setContentTitle(TAG)
        builder.setContentText(TAG)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        builder.setChannelId("1")
        builder.setContentIntent(pendingIntent)
        val notification = builder.build()
        startForeground(1, notification)
    }

    inner class MqttBinder : Binder() {
        val service: MQTTService
            get() = this@MQTTService
    }

    inner class MqttListener : MqttCallback, IMqttActionListener {
        override fun connectionLost(cause: Throwable?) {
            Log.w(TAG, "mqtt connectionLost")
        }

        override fun messageArrived(topic: String, message: MqttMessage) {
            Log.d(
                TAG,
                "mqtt message arrived,topic is $topic, payload is ${String(message.payload)}"
            )
            MessageManager.getInstance().newMessage(message.payload)
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d(TAG, "mqtt deliveryComplete")
        }

        override fun onSuccess(asyncActionToken: IMqttToken?) {
            //连接mqtt服务成功时，订阅话题
            try {
                Log.d(TAG, "mqtt connect success")
                mqttAndroidClient.subscribe(MQTT_SERVER_TOPIC, MQTT_SERVER_QOS)
            } catch (e: MqttException) {
                Log.e(TAG, "subscribe mqtt exception ${e.message}")
            }
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            //连接失败时，重连
            Log.w(TAG, "mqtt connect failure")
        }

    }

    fun publish(msg: String) {
        try {
            mqttAndroidClient.publish(MQTT_SERVER_TOPIC, msg.toByteArray(), MQTT_SERVER_QOS, false)
        } catch (e: MqttException) {
            Log.e(TAG, "publish mqtt exception ${e.message}")
        }
    }
}