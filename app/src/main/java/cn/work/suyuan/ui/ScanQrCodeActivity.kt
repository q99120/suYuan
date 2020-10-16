package cn.work.suyuan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_scan_qr.*

class ScanQrCodeActivity:QRCodeView.Delegate,BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        BGAQRCodeUtil.setDebug(true)
        setStatusBarBackground(R.color.normalBlue)



        mZXingView.setDelegate(this)

        initView()
    }

    private fun initView() {
        iv_back.setOnClickListener { finish() }
        btnConfirm.setOnClickListener {
            qrCallBack.qrData(QRresult)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mZXingView.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect() // 显示扫描框，并开始识别
    }
    override fun onStop() {
        mZXingView.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }
    override fun onDestroy() {
        mZXingView.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(500)
    }



    companion object{
        lateinit var qrCallBack: QrCallBack
        fun start(context: Context,callBack: QrCallBack){
            qrCallBack = callBack
            context.startActivity(Intent(context, ScanQrCodeActivity::class.java))
        }
    }

    var QRresult =  ""
    override fun onScanQRCodeSuccess(result: String?) {
        QRresult = result.toString()
        Log.e("扫描结果", "result:$result")
//        title = "扫描结果为：$result"
        tvQrResult.text = result
//        vibrate()
        mZXingView.startSpot() // 开始识别
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
    }

    override fun onScanQRCodeOpenCameraError() {
        Log.e("打开相机出错", "打开相机出错")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mZXingView.startSpotAndShowRect() // 显示扫描框，并开始识别
    }

    interface  QrCallBack{
        fun qrData(result:String)
    }
}