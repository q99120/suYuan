package cn.work.suyuan.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.event.StringEvent
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.widget.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_zhijian.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.ArrayList

class QualityActivity:BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_zhijian)
        initView()
        initObserver()
    }

    var zhijianID = ""
    var productFile = ""
    private fun initObserver() {
        viewModel.sendReportLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            zhijianID = rp.data.toString()
            if (rp.code == 200) "上传质检报告成功".toast() else rp.msg.toast()
        })
        viewModel.upLoadFileLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200)
                "上传图片成功".toast()
                productFile = rp.data.toString()
        })
    }

    private fun initView() {
        ivBack.setOnClickListener {
            finish()
        }
        ivPic.setOnClickListener {
            EasyPhotos.createAlbum(activity, true, GlideEngine.getInstance())
                .setFileProviderAuthority("cn.work.suyuan.fileprovider")
                .start(103)
        }
        btnConfirmReport.setOnClickListener {
            if (editZhijian.text.isEmpty()){
                "质检单号不能为空".toast()
                return@setOnClickListener
            }
            if (productFile == "") {
                "请先上传质检图片文件".toast()
                return@setOnClickListener
            }
            viewModel.sendReport(editZhijian.text.toString(),productFile)
        }
        tvActionZhijian.setOnClickListener {
            ScanQrCodeActivity.start(this, object : ScanQrCodeActivity.QrCallBack {
                override fun qrData(result: String) {
                    editZhijian.append(result + "\n")
                }

            })
        }

    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,QualityActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 103) {
                val resultPhotos: ArrayList<Photo>? =
                    data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)
                for (f in resultPhotos!!) {
                    GlideEngine.getInstance()
                        .loadPhotoNoCircle(this, Uri.parse(f.uri.toString()), ivPic)
                    val file = File(f.path)
                    viewModel.upLoadFile(file)
                }
            }
        }
    }
}