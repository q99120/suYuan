package cn.work.suyuan.wxapi

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.work.suyuan.Const
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    override fun onReq(baseReq: BaseReq) {}
    override fun onResp(baseResp: BaseResp) {
//登录回调
        when (baseResp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val code = (baseResp as SendAuth.Resp).code
                //获取accesstoken
                getAccessToken(code)
                Log.d("fantasychongwxlogin", code.toString() + "")
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> finish()
            BaseResp.ErrCode.ERR_USER_CANCEL -> finish()
            else -> finish()
        }
    }

    private fun getAccessToken(code: String) {
//        createProgressDialog();
        //获取授权
        val loginUrl = StringBuffer()
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
            .append("?appid=")
            .append(Const.wxAppId)
            .append("&secret=")
            .append(Const.wxAppKey)
            .append("&code=")
            .append(code)
            .append("&grant_type=authorization_code")
        Log.e("urlurl", loginUrl.toString())
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(loginUrl.toString())
            .get() //默认就是GET请求，可以不写
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("fan12", "onFailure: ")
                //                mProgressDialog.dismiss();
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseInfo = response.body()!!.string()
                Log.e("fan12", "onResponse: $responseInfo")
                var access: String? = null
                var openId: String? = null
                try {
                    val jsonObject = JSONObject(responseInfo)
                    access = jsonObject.getString("access_token")
                    openId = jsonObject.getString("openid")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                //                getUserInfo(access, openId);
            }
        })
    } //    private void createProgressDialog() {
    //        mContext=this;
    //        mProgressDialog=new ProgressDialog(mContext);
    //        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
    //        mProgressDialog.setCancelable(false);
    //        mProgressDialog.setCanceledOnTouchOutside(false);
    //        mProgressDialog.setTitle("提示");
    //        mProgressDialog.setMessage("登录中，请稍后");
    //        mProgressDialog.show();
    //    }
}