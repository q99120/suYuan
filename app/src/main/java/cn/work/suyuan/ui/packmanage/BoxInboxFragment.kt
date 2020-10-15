package cn.work.suyuan.ui.packmanage

import android.content.Intent
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
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.InjectorUtil
import com.uuzuche.lib_zxing.activity.CaptureActivity
import kotlinx.android.synthetic.main.fragment_pack_manage.*
import kotlinx.android.synthetic.main.layout_pack_mg.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.tvActionQr

/**
 * 大箱装小箱
 */
class BoxInboxFragment :BaseFragment() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
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


    override fun loadDataOnce() {
        super.loadDataOnce()
        BigBoxQr2.visibility = View.VISIBLE
        BigBoxQr.visibility = View.INVISIBLE
        etBoxQr.hint = "请扫描大箱条码..."
    }

    private fun initClicks() {
        setOnClickListener(tvActionQr, BigBoxActionQr, btnDonePack) {
            when (this) {
                tvActionQr -> {
                    val intent = Intent(requireContext(), CaptureActivity::class.java)
                    startActivityForResult(intent, 7777)
                }
                BigBoxActionQr -> {
                    val intent = Intent(requireContext(), CaptureActivity::class.java)
                    startActivityForResult(intent, 9999)
                }
                btnDonePack -> viewModel.doPackSingBox(
                    "", "", "2020-10-09 11:05:06",
                    "/uploads/video/20201009/2f905868d24a13ff66d4c002f8c8d2a5.mp4", 2, ""
                )
            }
        }
    }

    companion object {
        fun newInstance() = BoxInboxFragment()
    }


}