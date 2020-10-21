package cn.work.suyuan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.Const
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.APUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.util.ResponseHandler
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity:BaseActivity() {
    lateinit var wxApi:IWXAPI
//
//
//    private val mTencent //qq主操作对象
//            : Tencent? = null
//    private val loginListener //授权登录监听器
//            : IUiListener? = null
//    private val userInfoListener //获取用户信息监听器
//            : IUiListener? = null
//    private val scope //获取信息的范围参数
//            : String? = null
//    private val userInfo //qq用户信息
//            : UserInfo? = null


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        wxApi = WXAPIFactory.createWXAPI(this, Const.wxAppId, true)
        wxApi.registerApp(Const.wxAppId)
        observer()
    }


    private fun observer() {
        viewModel.loginLiveData.observe(this, Observer {
            val rp = it.getOrNull()
            if (rp == null) {
                ResponseHandler.getFailureTips(it.exceptionOrNull()).toast()
               return@Observer
            }
            if (rp.code == 200) {
                val token = rp.data.token
                APUtils.putString("tokens", token)
                MainActivity.start(this)
                finish()
            }else{
                rp.msg.toast()
            }

        })
    }

    override fun setupViews() {
        super.setupViews()
         btnLogin.setOnClickListener { viewModel.login(
             editAccount.text.toString(),
             editPassword.text.toString()
         ) }
        ivWx.setOnClickListener {
            wxLogin()
        }
        ivQq.setOnClickListener {
            qqLogin()
        }
        }

    private fun qqLogin() {
//        mTencent = Tencent.createInstance(Const.qqAppId, applicationContext)
//        mTencent.login(this, "all", BaseUiListener())

    }



//    /**
//     * 微信第三方登录
//     */
    private fun wxLogin() {
        if (isWXAppInstalledAndSupported()) {
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "none"
            wxApi.sendReq(req)
        }else{
            "您的设备未安装微信客户端".toast()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mTencent?.logout(this)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private fun isWXAppInstalledAndSupported(): Boolean {
        val msgApi = WXAPIFactory.createWXAPI(this, null)
        msgApi.registerApp(Const.wxAppId)
        return (msgApi.isWXAppInstalled)
    }

}