package com.yurnero.mqtt

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.yurnero.mqtt.util.Utils

class App : ViewModelStoreOwner, Application() {
    private lateinit var viewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        viewModelStore = ViewModelStore()
        Utils.init(this)
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }
}