package cn.work.suyuan.ui.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        ivBack.setOnClickListener { finish() }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,SettingActivity::class.java))
        }
    }
}