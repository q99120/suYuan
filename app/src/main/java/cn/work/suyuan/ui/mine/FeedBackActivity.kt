package cn.work.suyuan.ui.mine

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.singleClick
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.ui.ScanQrCodeActivity
import cn.work.suyuan.ui.adapter.AddPicAdapter
import cn.work.suyuan.ui.dialog.QutalityListDialog
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.APUtils
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.widget.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.ivBack
import kotlinx.android.synthetic.main.layout_feed_back.*
import kotlinx.android.synthetic.main.layout_feed_back.btnAddPicD
import kotlinx.android.synthetic.main.layout_feed_back.btnImportQ
import kotlinx.android.synthetic.main.layout_feed_back.editProductName
import kotlinx.android.synthetic.main.layout_feed_back.editQResult
import kotlinx.android.synthetic.main.layout_feed_back.editUName
import kotlinx.android.synthetic.main.layout_feed_back.recyclerAddPic
import kotlinx.android.synthetic.main.layout_feed_back.tvActionQr
import org.json.JSONArray
import java.util.ArrayList

class FeedBackActivity :BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }
    val addPicAdapter = AddPicAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        recyclerAddPic.layoutManager = GridLayoutManager(this, 3)
        recyclerAddPic.adapter = addPicAdapter
        ivBack.setOnClickListener { finish() }
        initView()
        initClick()
        initObserver()
    }
    var zhijianID = ""
    private fun initObserver() {
        viewModel.qutalityListLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            Log.e("拿到质检报告2",rp.msg.toString())
            Log.e("拿到质检报告3",rp.code.toString())
            Log.e("拿到质检报告1",rp.data.toString())
            if (rp.code == 200) {
                qutalityListDialog.initDialog(rp.data.data, rp.data.last_page, object :
                    QutalityListDialog.FileClick {
                    override fun fileClick(reportName: String, id: Int) {
                        "获取质检报告成功".toast()
                        zhijianID = id.toString()
                        editQResult.setText(reportName)
                    }
                }, object : QutalityListDialog.quRefresh {
                    override fun refresh(page: Int) {
                        viewModel.getQutalityList(page, "")
                    }

                })
                if (rp.data.current_page > 1) {
                    qutalityListDialog.addList(rp.data.data)
                }
            }
        })
        viewModel.uploadHeadLiveData.observe(this, Observer {
            isUpLoad = false
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                "上传图片成功".toast()
                arrayId[currentPosition] = rp.data.lid
            } else {
                rp.msg.toast()
                return@Observer
            }
//            for (a in 0 until arrayId.size) {
//                resultId[a] = arrayId[a].toString()
//            }
//            resultId[arrayId.size - 1] = arrayId[arrayId.size - 1].toString().replace(",","")
        })
        viewModel.feedBackLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200){
                "上传反馈信息成功".toast()
                editQResult.setText("")
                editProductName.setText("")
                arrayId.clear()
                richEditText.setText("")
                list.clear()
                list.add("1")
                addPicAdapter.setList(list)
                addPicAdapter.setPic(-1)
            }else{
                rp.msg.toast()
            }
            Log.e("获取rppp1",rp.code.toString())
            Log.e("获取rppp2",rp.msg.toString())
            Log.e("获取rppp3",rp.data.toString())

        })
    }

    var isUpLoad = false
    var currentPosition = 0
    val array: HashMap<Int, String> = HashMap()
    val arrayId: HashMap<Int, String> = HashMap()
    private fun initClick() {
        addPicAdapter.addChildClickViewIds(R.id.ivPic, R.id.btnAddPic)
        addPicAdapter.setOnItemChildClickListener { adapter, view, position ->
            currentPosition = position
            when (view.id) {
                R.id.ivPic -> {
                    EasyPhotos.createAlbum(activity, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("cn.work.suyuan.fileprovider")
                        .start(105)
                }
                R.id.btnAddPic -> {
                    if (!isUpLoad) {
                        for (a in array) {
                            if (position == a.key) {
                                Log.e("拿到arry",a.toString())
//                                val file = File(a.value)
                                isUpLoad = true
                                val base64Result = FileUtils.imageToBase64(a.value)
                                viewModel.uploadHead(base64Result.toString())
                            }
                        }
                    } else {
                        "有图片正在上传中,请稍等".toast()
                    }

                }
            }
        }
        btnAddPicD.singleClick(500) {
            initAdapterData()
        }
        btnImportQ.singleClick {
            viewModel.getQutalityList(1, "")
        }
        tvActionQr.singleClick {
            ScanQrCodeActivity.start(this, object : ScanQrCodeActivity.QrCallBack {
                override fun qrData(result: String) {
                    editProductName.append(result + "\n") }
            })
        }
        btnUpLoadFeed.singleClick {
            if (editProductName.text.isEmpty()){
                "请输入产品条码".toast()
                return@singleClick
            }
            if (editQResult.text.isEmpty()){
                "请输入质检单号".toast()
                return@singleClick
            }
            if (editUName.text.isEmpty()){
                "请输入操作员".toast()
                return@singleClick
            }
            val imageArray = JSONArray()
            if (arrayId.isNotEmpty()){
                for (a in arrayId) {
                    imageArray.put(a.value)
                }
            }else{
                "如果添加了图片请检查是否上传图片".toast()
            }
            Log.e("imagearray",imageArray.toString())
            viewModel.sendFeedBack(editProductName.text.toString(),editQResult.text.toString(),editUName.text.toString(),richEditText
                .text.toString(),imageArray)
        }

    }
    val list = mutableListOf<String>()
    private fun initAdapterData() {
        if (list.size < 9) {
            list.add("1")
            addPicAdapter.setList(list)
        } else {
            "不能再添加图片了".toast()
        }

    }

    private fun initView() {
        editUName.setText(APUtils.getString("nickName"))
        initAdapterData()

    }
    private val qutalityListDialog by lazy {
        QutalityListDialog(this)
    }
    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,FeedBackActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 105) {
                val resultPhotos: ArrayList<Photo>? =
                    data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)
                for (f in resultPhotos!!) {
                    array[currentPosition] = f.path
                    addPicAdapter.setPic(currentPosition, Uri.parse(f.uri.toString()))
                    val base64Result = FileUtils.imageToBase64(f.path)
                    viewModel.uploadHead(base64Result.toString())
                }
            }

        }
    }
}