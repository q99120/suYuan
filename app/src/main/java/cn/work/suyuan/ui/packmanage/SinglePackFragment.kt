package cn.work.suyuan.ui.packmanage

import android.R.attr
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.ScanQrCodeActivity
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.util.SuYuanUtil
import kotlinx.android.synthetic.main.fragment_pack_manage.*
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_pack_mg.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * 单个装箱
 */
class SinglePackFragment :BaseFragment(){

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        BigBoxQr2.visibility = View.INVISIBLE
        BigBoxQr.visibility = View.VISIBLE
        etBoxQr.hint = "请扫描外箱条码..."
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_pack_manage, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvPackTime.text = DateUtil.getCurrentTime(true)
        initClicks()
        observer()
    }

    override fun onInvisible() {

    }

    override fun initData() {

    }

    private fun observer() {
        viewModel.doPackSingBoxLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            rp.msg.toast()
        })
        viewModel.upLoadFileLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            if (rp.code == 200)
                productFile = rp.data.toString()
            tvImportFile.text = "导入文件成功"
        })
    }

    var productTime = ""
    var productFile = ""
    private fun initClicks() {
        setOnClickListener(tvActionQr, BigBoxActionQr,btnDonePack,tvPackTime,llActionImFiles){
            when(this){
                tvActionQr -> goQrCode()
                BigBoxActionQr -> ScanQrCodeActivity.start(activity,object :ScanQrCodeActivity.QrCallBack{
                    override fun qrData(result: String) {
                        etBoxQr.append(result+"\n")
                    }
                })
                tvPackTime->DateUtil.showDate(activity,true,object :DateUtil.ChooseDate{
                    override fun getTime(result: String) {
                        productTime = result
                        tvPackTime.text = productTime
                    }
                })
                llActionImFiles->FileUtils.upLoadFiles(activity,fileChooseDialog,object :FileUtils.CallBackFile{
                    override fun backFile(file: File) {
                        viewModel.upLoadFile(file)
                    }

                })

                btnDonePack->
                    if (etProductQr.text.isNotEmpty() && etBoxQr.text.isNotEmpty()){
                        viewModel.doPackSingBox(SuYuanUtil.getEditProduct(etProductQr.text.toString()),etBoxQr.text.toString(),productTime,
                            productFile,1,"")
                    }else "请先填写产品和外箱条码".toast()

            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            isOpen = group.checkedRadioButtonId == R.id.radioOpen
        }

    }

    companion object {
        fun newInstance() = SinglePackFragment()
    }
    var isOpen = false
    private fun goQrCode() {
        val player = MediaPlayer.create(requireContext(), R.raw.ding)
        ScanQrCodeActivity.start(activity, object : ScanQrCodeActivity.QrCallBack {
            override fun qrData(result: String) {
                etProductQr.append(result+"\n")
                player.start()
                if (isOpen){
                    "2秒后继续自动扫描".toast()
                    CoroutineScope(job).launch {
                        delay(2000)
                        goQrCode()
                    }
                }
            }

        })
    }



}