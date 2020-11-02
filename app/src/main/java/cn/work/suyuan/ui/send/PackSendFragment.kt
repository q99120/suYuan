package cn.work.suyuan.ui.send

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.Const
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
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.io.File
import java.util.*

/**
 * 整箱发货
 */
class PackSendFragment : BaseFragment() {


    var isClick = false
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
        getDisAndProduct()
        initViews()
    }

    override fun onInvisible() {
    }

    override fun initData() {
    }

    private fun getDisAndProduct() {
        viewModel.getDistributor()
        viewModel.getProduct()
    }


    private fun initViews() {

        tvChooseProduct.onSpinnerItemSelectedListener =
            OnSpinnerItemSelectedListener { parent, view, position, id ->
                productId = arrayProduct[position]!!
                Log.e("productId",productId.toString())
            }
        tvChooseDistributor.onSpinnerItemSelectedListener =
            OnSpinnerItemSelectedListener { parent, view, position, id ->
                distributorId = arrayAgent[position]!!
                Log.e("distributorId",distributorId.toString())
            }
        setOnClickListener(
            tvChooseDistributor,
            btnSend,
            tvChooseProduct,
            tvTracingTime,
            llActionImFiles, tvActionQr
        ) {
            when (this) {
                tvActionQr -> {
                    ScanQrCodeActivity.start(activity, object : ScanQrCodeActivity.QrCallBack {
                        override fun qrData(result: String) {
                            editQrCode.append(result + "\n")
                        }
                    })
                }
                llActionImFiles -> FileUtils.upLoadFiles(activity, fileChooseDialog, object :
                    FileUtils.CallBackFile {
                    override fun backFile(file: File) {
                        viewModel.upLoadFile(file)
                    }
                })
                btnSend -> {
                    if (editQrCode.text.isNotEmpty()) {
                        viewModel.sendProduct(
                            2,
                            productId,
                            distributorId,
                            SuYuanUtil.getEditProduct(editQrCode.text.toString()),
                            productTime,
                            productFile
                        )
                            sendObserVer()
                    }else "请先填写外箱条码".toast()
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

    lateinit var popUtil: SinglePopUtil
    private var productId = -1
    private var distributorId = -1
    private var productCode = ""
    private var productTime = DateUtil.getCurrentTime(true)
    private var productFile = ""
    lateinit var arrayProduct: Array<Int?>
    val listProduct = mutableListOf<String>()
    lateinit var arrayAgent: Array<Int?>
    val listAgent = mutableListOf<String>()
    private fun observer() {
        viewModel.distributorLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (listAgent.isNotEmpty())listAgent.clear()
            arrayAgent = arrayOfNulls<Int>(rp.data.size)
            for (i in 0 until rp.data.size) {
                arrayAgent[i] = rp.data[i].id
                listAgent.add(rp.data[i].title)
            }
            distributorId = rp.data[0].id
            val dataset: List<String> =
                LinkedList(listAgent)
            tvChooseDistributor.attachDataSource(dataset)
        })
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (listProduct.isNotEmpty())listProduct.clear()
            arrayProduct = arrayOfNulls<Int>(rp.data.size)
            for (i in 0 until rp.data.size) {
                arrayProduct[i] = rp.data[i].id
                listProduct.add(rp.data[i].name)
            }
            val dataset: List<String> =
                LinkedList(listProduct)
            productId = rp.data[0].id
            tvChooseProduct.attachDataSource(dataset)
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



    override fun onPause() {
        viewModel.sendProductLiveData.removeObservers(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun sendObserVer() {
        viewModel.sendProductLiveData.observe(viewLifecycleOwner, Observer {
            Log.e("livedata2","111")
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
        })
    }

}