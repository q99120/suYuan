package cn.work.suyuan.ui.packmanage

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.ui.BaseFragment
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.layout_pack_mg.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.tvActionQr


class SinglePackFragment :BaseFragment(){

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
    }

    private fun initClicks() {
        setOnClickListener(tvActionQr, BigBoxActionQr){
            when(this){
                tvActionQr -> {
                    val intent = Intent(requireContext(), CaptureActivity::class.java)
                    startActivityForResult(intent, 7777)
                }
                BigBoxActionQr -> {
                }
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