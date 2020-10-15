package cn.work.suyuan.ui.packmanage

import android.R.attr
import android.content.Intent
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
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.InjectorUtil
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.fragment_pack_manage.*
import kotlinx.android.synthetic.main.layout_pack_mg.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.tvActionQr

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
        initClicks()
        observer()
    }

    private fun observer() {
        viewModel.doPackSingBoxLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            rp.msg.toast()
        })
    }

    private fun initClicks() {
        setOnClickListener(tvActionQr, BigBoxActionQr,btnDonePack){
            when(this){
                tvActionQr -> {
                    val intent = Intent(requireContext(), CaptureActivity::class.java)
                    startActivityForResult(intent, 7777)
                }
                BigBoxActionQr -> {
                    val intent = Intent(requireContext(), CaptureActivity::class.java)
                    startActivityForResult(intent, 9999)
                }
                btnDonePack->viewModel.doPackSingBox("","","2020-10-09 11:05:06",
                    "/uploads/video/20201009/2f905868d24a13ff66d4c002f8c8d2a5.mp4",1,"")
            }
        }
    }

    companion object {
        fun newInstance() = SinglePackFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7777) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                val bundle: Bundle = data.extras ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    Log.e("解析结果",result.toString())
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Log.e("解析二位日吗失败",resultCode.toString())
                }
            }
        }
    }




}