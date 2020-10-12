package cn.work.suyuan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_scan_qr.*

class ScanQrActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
    }

    override fun setupViews() {
        super.setupViews()


//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        //        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }


}
