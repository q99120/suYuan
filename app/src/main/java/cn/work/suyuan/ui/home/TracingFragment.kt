package cn.work.suyuan.ui.home

import android.os.Bundle
import android.util.Log
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
import cn.work.suyuan.ui.dialog.SingleSpinnerDialog
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.util.SuYuanUtil
import kotlinx.android.synthetic.main.fragment_home_tracing.*
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import java.io.File

/**
 * 流程追朔
 */
class TracingFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(
            inflater.inflate(
                R.layout.fragment_home_tracing,
                container,
                false
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observer()
    }

    override fun onInvisible() {
    }

    override fun initData() {
    }

    private fun observer() {
        viewModel.upLoadFileLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            if (rp.code == 200)
                productFile = rp.data.toString()
            tvImportFile.text = "导入文件成功"
        })
        viewModel.setTracingLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            Log.e("message", rp.msg)
            Log.e("code", rp.code.toString())
            rp.msg.toast()
        })
    }

    var categoryId = 1
    var tracingTime = ""
    var productFile = ""
    lateinit var arrayCode: Array<String?>
    private fun initViews() {
        tvTracingTime.text = DateUtil.getCurrentTime(true)
        setOnClickListener(btnConfirm, tvChooseCate, tvTracingTime, llActionImFiles, tvActionQr) {
            when (this) {
                btnConfirm -> {
                    if (editUName.text!!.isEmpty()) {
                        fuToast("责任人或工号不能为空")
                        return@setOnClickListener
                    }
                    if (editProductName.text!!.isEmpty()) {
                        fuToast("产品条码不能为空")
                        return@setOnClickListener
                    }
                    if (richEditText.text!!.isEmpty()) {
                        fuToast("富文本编辑器不能为空")
                        return@setOnClickListener
                    }
                    if (editProductNum.text!!.isEmpty()){
                        fuToast("生产批次不能为空")
                        return@setOnClickListener
                    }
                    if (editOrderNum.text!!.isEmpty()) {
                        fuToast("订单号不能为空")
                        return@setOnClickListener
                    }
                    viewModel.setTracing(
                        categoryId,
                        editUName.text.toString(),
                        SuYuanUtil.getEditProduct(editProductName.text.toString()),
                        tracingTime,
                        productFile,
                        richEditText.text.toString(),
                        editProductNum.text.toString(),
                        editOrderNum.text.toString()
                    )
                }
                tvChooseCate -> {
                    spinnerDialog.initSpinner(viewModel.getCate(),
                        object : SingleSpinnerDialog.HomeNormalClick {
                            override fun dialogClick(processName: String, sort: Int) {
                                tvChooseCate.text = processName
                                categoryId = sort
                            }
                        })
                }
                tvTracingTime -> {
                    DateUtil.showDate(activity, true, object : DateUtil.ChooseDate {
                        override fun getTime(result: String) {
                            tvTracingTime.text = result
                            tracingTime = result
                        }
                    })
                }
                llActionImFiles -> {
                    FileUtils.upLoadFiles(activity, fileChooseDialog, object :
                        FileUtils.CallBackFile {
                        override fun backFile(file: File) {
                            viewModel.upLoadFile(file)
                        }
                    })
                }
                tvActionQr -> {
                    ScanQrCodeActivity.start(activity, object : ScanQrCodeActivity.QrCallBack {
                        override fun qrData(result: String) {
                            editProductName.append(result + "\n")
                        }

                    })
                }

            }
        }

    }

    private fun isEnableClick(view: View?) {

    }

    private fun fuToast(content: String) {
        content.toast()
    }


    companion object {
        fun newInstance() = TracingFragment()


    }

    override fun loadDataOnce() {
        super.loadDataOnce()
    }

    private val spinnerDialog by lazy {
        SingleSpinnerDialog(requireContext())
    }

}