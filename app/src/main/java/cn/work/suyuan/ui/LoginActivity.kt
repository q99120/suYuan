package cn.work.suyuan.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.APUtils
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        observer()
    }


    private fun observer() {
        viewModel.loginLiveData.observe(this, Observer {
            val rp = it.getOrNull()?:return@Observer
            if (rp.code == 200){
                val token = rp.data.token
                APUtils.putString("tokens",token)
                MainActivity.start(this)
                finish()
            }

        })
    }

    override fun setupViews() {
        super.setupViews()
         btnLogin.setOnClickListener { viewModel.login(editAccount.text.toString(),editPassword.text.toString()) }
        }

    override fun onStart() {
        super.onStart()
        if (APUtils.getString("tokens")!="")  MainActivity.start(this)
    }
}