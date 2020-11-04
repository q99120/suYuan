package cn.work.suyuan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
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
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity:BaseActivity(),IUiListener {
    lateinit var wxApi: IWXAPI

    //
//
    private lateinit var mTencent: Tencent
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
        initThreeLogin()
        wxApi = WXAPIFactory.createWXAPI(this, Const.wxAppId, true)
        wxApi.registerApp(Const.wxAppId)
        observer()
    }

    //初始化第三方登录
    private fun initThreeLogin() {
        mTencent = Tencent.createInstance(Const.qqAppId, this.applicationContext)
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
                APUtils.putInt("agentId",rp.data.agent_info.agent_id)
                APUtils.putInt("agentLevel",rp.data.agent_info.agent_level)
                MainActivity.start(0, this, "", "")
                finish()
            } else {
                rp.msg.toast()
            }

        })
    }

    override fun setupViews() {
        super.setupViews()
        btnLogin.setOnClickListener {
            viewModel.login(
                editAccount.text.toString(),
                editPassword.text.toString()
            )
        }
        ivWx.setOnClickListener {
            wxLogin()
        }
        ivQq.setOnClickListener {
            qqLogin()
        }
    }

     var isServerSideLogin = false
    private fun qqLogin() {
        if (!mTencent.isSessionValid) {
            mTencent.login(this, "all", this)
            isServerSideLogin = false
            Log.e("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this)
                mTencent.login(this, "all", this)
                isServerSideLogin = false
                Log.e("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return
            }
            mTencent.logout(this)
        }
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
        } else {
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

    val context = this@LoginActivity




    var isgetTokenQQ = false
    //初始化OPENID和TOKEN值（为了得了用户信息）
    private fun initOpenidAndToken(jsonObject: JSONObject?) {
        try {
            if (jsonObject != null) {
                val token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
                val expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
                val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
                Log.e("登录的参数", jsonObject.toString())
                if (token.isNotEmpty() && expires.isNotEmpty() && openId.isNotEmpty()) {
                    mTencent.setAccessToken(token, expires)
                    mTencent.openId = openId
                    //拿到openid后给后台传码
                    viewModel.login(openId,"123456")
                    "登录成功".toast()
                    /**
                     * 获取用户昵称，头像等信息
                     */
                    isgetTokenQQ = true
//                    val userInfo = UserInfo(this, mTencent.qqToken)
//                    userInfo.getUserInfo(this)
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN ||
            requestCode == Constants.REQUEST_APPBAR
        ) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this)
        }
    }

    override fun onComplete(response: Any) {
        if (!isgetTokenQQ){
            initOpenidAndToken(response as JSONObject)
        }else{
//            val jsonObject: JSONObject = JSONObject(response.toString())
//            Log.e("获取登录的用户详情",jsonObject.toString())
//            val nickname = jsonObject.optString("nickname")
//            val figuralQq2 = jsonObject.optString("figureurl_qq_2")
//            MainActivity.start(1, this, nickname, figuralQq2)
        }

    }

    override fun onError(error: UiError?) {
        error?.errorMessage?.toast()
    }

    override fun onCancel() {
    }
}