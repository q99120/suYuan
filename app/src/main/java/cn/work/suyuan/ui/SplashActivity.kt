package cn.work.suyuan.ui

import android.os.Bundle
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.util.APUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity :BaseActivity(){

    private val job by lazy { Job() }

    private val splashDuration = 1 * 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun setupViews() {
        super.setupViews()
        CoroutineScope(job).launch {
            delay(splashDuration)
            if (APUtils.getString("tokens")!="")  {
                MainActivity.start(0,this@SplashActivity,"","")
            }else{
                LoginActivity.start(this@SplashActivity)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {

        /**
         * 是否首次进入APP应用
         */
    }
}