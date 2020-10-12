package cn.work.suyuan

import android.app.Application
import android.content.Context
import com.uuzuche.lib_zxing.activity.ZXingLibrary

class SuYuanApplication:Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        context = this
        ZXingLibrary.initDisplayOpinion(this)
        super.onCreate()
    }
}