package cn.work.suyuan

import android.app.Application
import android.content.Context

class SuYuanApplication:Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        context = this
        super.onCreate()
    }
}