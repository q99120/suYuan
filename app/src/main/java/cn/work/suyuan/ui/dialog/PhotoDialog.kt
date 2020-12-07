package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.widget.GlideEngine
import kotlinx.android.synthetic.main.dialog_photo.*

class PhotoDialog : Dialog {
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_photo)
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        initView()
    }

    var urls = ""
    fun initData(testReportImg: String) {
        urls = "http://track.for315.cn$testReportImg"
        Log.e("拿到urls",urls)
        GlideEngine.getInstance().loadPhotoNoCircle(context, Uri.parse(urls),iv_photo)
        ivBack.setOnClickListener { dismiss() }
        show()
    }

    private fun initView() {


    }



}