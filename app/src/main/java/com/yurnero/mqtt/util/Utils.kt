package com.yurnero.mqtt.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.yurnero.mqtt.App
import java.lang.reflect.InvocationTargetException

object Utils {
    private var application: App? = null

    fun init(context: Context) {
        if (application == null) {
            application = context.applicationContext as App
        }
    }

    fun getApp(): Application {
        if (application == null) {
            return getApplicationByReflect()
        }
        return application!!
    }

    private fun getApplicationByReflect(): Application {
        try {
            @SuppressLint("PrivateApi") val activityThread: Class<*> =
                Class.forName("android.app.ActivityThread")
            val thread: Any = activityThread.getMethod("currentActivityThread").invoke(null)
            val app: Any = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw NullPointerException("u should init first")
    }

}