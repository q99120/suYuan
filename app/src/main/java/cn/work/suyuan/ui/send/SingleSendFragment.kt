package cn.work.suyuan.ui.send

import android.R.attr
import android.R.attr.path
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.MainActivity
import cn.work.suyuan.ui.dialog.FileChooseDialog
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.util.SinglePopUtil
import kotlinx.android.synthetic.main.fragment_send_manage.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*


class SingleSendFragment : BaseFragment() {
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
        return super.onCreateView(inflater.inflate(R.layout.fragment_send_manage, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvSendQrTitle.text = "产品条码"
        layoutFloWater.visibility = View.GONE
        initViews()
        observer()
    }


    private fun initViews() {
        setOnClickListener(
            tvChooseDistributor,
            btnSend,
            tvChooseProduct,
            tvTracingTime,
            llActionImFiles
        ) {
            when (this) {
                tvChooseDistributor -> {
                    viewModel.getDistributor()
                }
                tvChooseProduct -> {
                    viewModel.getProduct()
                }
                llActionImFiles -> {
                    FileUtils.searchFile(activity)
                }
//                    fileChooseDialog.setData(FileUtils.queryFiles(),object :FileChooseDialog.FileClick{
//                        override fun fileClick(fileName: String, filePath: String) {
//                            tvFileName.text = fileName
//                            val file = File(filePath)
//                            Log.e("获取filepath",file.absolutePath)
//                            viewModel.upLoadFile(file)
//                        }
//
//                    })
                btnSend -> {
                    viewModel.sendProduct(
                        1,
                        productId,
                        distributorId,
                        productCode,
                        productTime,
                        productFile
                    )
                }
                tvTracingTime -> {
                    DateUtil.showDate(activity, true, object : DateUtil.ChooseDate {
                        override fun getTime(result: String) {
                            tvTracingTime.text = result
                        }
                    }
                    )
                }
            }
        }
    }

    private var productId = -1
    private var distributorId = -1
    private var productCode = ""
    private var productTime = ""
    private var productFile = ""
    private fun observer() {
        viewModel.distributorLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            val popUtil = SinglePopUtil(requireContext(), object : SinglePopUtil.popClick {
                override fun clickPop(type: String, pi: Int) {
                    tvChooseDistributor.text = type
                    distributorId = pi
                }

            })
            popUtil.setData(rp.data, 1)
            popUtil.showAsDropDown(tvChooseDistributor)
        })
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            val popUtil = SinglePopUtil(requireContext(), object : SinglePopUtil.popClick {
                override fun clickPop(type: String, pi: Int) {
                    tvChooseProduct.text = type
                    productId = pi
                }

            })
            popUtil.setData(rp.data, 2)
            popUtil.showAsDropDown(tvChooseProduct)
        })
        viewModel.sendProductLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) rp.msg.toast()
        })

        viewModel.upLoadFileLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            Log.e("火球环境1", rp.msg.toString())
            Log.e("火球环境2", rp.code.toString())
//            Log.e("火球环境3",rp.data.toString())

        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
    }


    companion object {
        fun newInstance() = SingleSendFragment()
    }

    private val fileChooseDialog by lazy {
        FileChooseDialog(requireContext())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1007) {
            }

        }

    }

}