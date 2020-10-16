package cn.work.suyuan.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.ScanQrCodeActivity
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_cancel_send.*
import kotlinx.android.synthetic.main.fragment_cancel_send.editQrCode
import kotlinx.android.synthetic.main.fragment_cancel_send.tvSendQrTitle
import java.io.File

/**
 * 取消整箱发货
 */

class CancelPackFragment: BaseFragment(){
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_cancel_send, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvSendQrTitle.text = "外箱条码"
        editQrCode.hint = "外箱条码"
        initView()
        observer()
    }

    var productFile = ""
    var product = ""
    var productTime = ""
    private fun initView() {
        setOnClickListener(btnImportFile,btnCancel,tvActionQr){
            when(this){
                btnImportFile-> FileUtils.upLoadFiles(activity,fileChooseDialog,object : FileUtils.CallBackFile{
                    override fun backFile(file: File) { viewModel.upLoadFile(file) } })
                    btnCancel-> viewModel.cancelSendPack(2,editQrCode.text.toString(),productTime,productFile)
                tvActionQr->ScanQrCodeActivity.start(activity,object :ScanQrCodeActivity.QrCallBack{
                    override fun qrData(result: String) {
                        editQrCode.setText(result)
                    }
                })
            }
        }
    }

    fun observer(){
        viewModel.upLoadFileLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            if (rp.code == 200)
                productFile = rp.data.toString()
            tvImportFile.text = "导入文件成功"
        })
        viewModel.cancelSendLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
        })
    }

    companion object {
        fun newInstance() = CancelPackFragment()
    }


}