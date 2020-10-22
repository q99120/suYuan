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
import cn.work.suyuan.util.*
import kotlinx.android.synthetic.main.fragment_send_manage.*
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.tvActionQr
import kotlinx.android.synthetic.main.layout_send_manage_fm.tvTracingTime
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import java.io.File

/**
 * 整箱发货
 */
class PackSendFragment: BaseFragment(){


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
        tvTracingTime.text = DateUtil.getCurrentTime(true)
        tvSendQrTitle.text = "外箱条码"
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
            llActionImFiles,tvActionQr
        ) {
            when (this) {
                tvActionQr->{
                    ScanQrCodeActivity.start(activity, object : ScanQrCodeActivity.QrCallBack {
                        override fun qrData(result: String) {
                            editQrCode.append(result+"\n")
                        }
                    })
                }
                tvChooseDistributor -> {
                    if (APUtils.getInt("agentLevel",0)!=0) viewModel.getDistributor(2) else viewModel.getDistributor(1)
                }
                tvChooseProduct -> {
                    viewModel.getProduct()
                }
                llActionImFiles -> FileUtils.upLoadFiles(activity,fileChooseDialog,object :
                    FileUtils.CallBackFile{
                    override fun backFile(file: File) { viewModel.upLoadFile(file) } })
                btnSend -> {
                    viewModel.sendProduct(2, productId, distributorId, SuYuanUtil.getEditProduct(editQrCode.text.toString()), productTime, productFile)
                }
                tvTracingTime -> {
                    DateUtil.showDate(activity, true, object : DateUtil.ChooseDate {
                        override fun getTime(result: String) {
                            tvTracingTime.text = result
                            productTime = result
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
    private var productTime = DateUtil.getCurrentTime(true)
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
            if (rp.code == 200)
                productFile = rp.data
            tvImportFile.text = "导入文件成功"


        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
    }



    companion object {
        fun newInstance() = PackSendFragment()
    }

}