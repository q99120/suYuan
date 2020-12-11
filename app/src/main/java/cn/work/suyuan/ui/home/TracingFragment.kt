package cn.work.suyuan.ui.home

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.singleClick
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.event.MessageEvent
import cn.work.suyuan.event.StringEvent
import cn.work.suyuan.ui.ScanQrCodeActivity
import cn.work.suyuan.ui.adapter.AddPicAdapter
import cn.work.suyuan.ui.dialog.QutalityListDialog
import cn.work.suyuan.ui.dialog.QutalityListDialog.*
import cn.work.suyuan.ui.dialog.SingleSpinnerDialog
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.*
import cn.work.suyuan.widget.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import kotlinx.android.synthetic.main.dialog_qutality_list.*
import kotlinx.android.synthetic.main.fragment_home_tracing.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import kotlinx.android.synthetic.main.layout_tracing_fm.editProductName
import kotlinx.android.synthetic.main.layout_tracing_fm.tvActionQr
import kotlinx.android.synthetic.main.layout_tracing_fm.tvChooseDistributor
import kotlinx.android.synthetic.main.layout_tracing_fm.tvTracingTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * 流程追朔
 */
class TracingFragment : BaseFragment() {

    val addPicAdapter = AddPicAdapter()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }
    private val sendPackViewModel by lazy {
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
        sendPackViewModel.getDistributor()
    }

    lateinit var arrayAgent: Array<Int?>
    val listAgent = mutableListOf<String>()
    var zhijianID = ""
    private var distributorId = -1
    private fun observer() {
        viewModel.qutalityListLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                qutalityListDialog.initDialog(rp.data.data, rp.data.last_page, object : FileClick {
                    override fun fileClick(reportName: String, id: Int) {
                        "获取质检报告成功".toast()
                        zhijianID = id.toString()
                        editQResult.setText(reportName)
                    }
                }, object : quRefresh {
                    override fun refresh(page: Int) {
                        viewModel.getQutalityList(page, "")
                    }

                })
                if (rp.data.current_page > 1) {
                    qutalityListDialog.addList(rp.data.data)
                }
            }
        })
        viewModel.uploadHeadLiveData.observe(viewLifecycleOwner, Observer {
            isUpLoad = false
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                "上传图片成功".toast()
                arrayId[currentPosition] = rp.data.lid
            }else{
                rp.msg.toast()
                return@Observer
            }
            for (a in arrayId){
                Log.e("拿去id",a.toString())
            }
        })
        viewModel.setTracingLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            Log.e("message", rp.msg)
            Log.e("code", rp.code.toString())
            rp.msg.toast()
        })
        viewModel.upLoadFileLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200)
                "上传图片成功".toast()
            isUpLoad = false
            arrayId[currentPosition] = rp.data

        })

        sendPackViewModel.distributorLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (listAgent.isNotEmpty()) listAgent.clear()
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
    }

    var categoryId = 1
    var tracingTime = ""
    var productFile = ""
    var isOpen = false
    lateinit var arrayCode: Array<String?>
    private fun initViews() {
        recyclerAddPic.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerAddPic.adapter = addPicAdapter
        initAdapterData()
        if (APUtils.getString("trace_process_title") == "包裝發貨") {
            llDescribe.visibility = View.VISIBLE
        }
        categoryId = APUtils.getInt("trace_process_id", 0)
        editUName.setText(APUtils.getString("nickName"))
        if (APUtils.getString("trace_process_title") == "") {
            tvChooseCate.text = "暂无流程，请在后台添加,如果添加了请重启APP"
        } else {
            tvChooseCate.text = APUtils.getString("trace_process_title")
        }
        tvTracingTime.text = DateUtil.getCurrentTime(true)
        setOnClickListener(
            btnConfirm,
            tvTracingTime,
            llActionImFiles,
            tvActionQr,
            btnImportQ,
            btnAddPicD
        ) {
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
                    if (editProductNum.text!!.isEmpty()) {
                        fuToast("生产批次不能为空")
                        return@setOnClickListener
                    }
                    if (editOrderNum.text!!.isEmpty()) {
                        fuToast("订单号不能为空")
                        return@setOnClickListener
                    }
                    val imageArray = JSONArray()
                    for (a in arrayId) {
                        imageArray.put(a.value)

                    }
                    viewModel.setTracing(
                        categoryId,
                        editUName.text.toString(),
                        SuYuanUtil.getEditProduct(editProductName.text.toString()),
                        tracingTime,
                        "",
                        richEditText.text.toString(),
                        editProductNum.text.toString(),
                        editOrderNum.text.toString(), zhijianID,distributorId,imageArray
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
                    goQrCode()
                }
                btnImportQ -> {
                    viewModel.getQutalityList(1, "")
                }
                btnAddPicD -> {
                    initAdapterData()
                }
            }
        }
        addPicAdapter.addChildClickViewIds(R.id.ivPic, R.id.btnAddPic)
        addPicAdapter.setOnItemChildClickListener { adapter, view, position ->
            currentPosition = position
            when (view.id) {
                R.id.ivPic -> {
                    EasyPhotos.createAlbum(activity, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("cn.work.suyuan.fileprovider")
                        .start(103)
                }
                R.id.btnAddPic -> {
                    if (!isUpLoad) {
                        for (a in array) {
                            if (position == a.key) {
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
        qutalityListDialog.btnScan.singleClick { viewModel.getQutalityList(1,qutalityListDialog.editQuSearch.text.toString()) }
        qutalityListDialog.editQuSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.getQutalityList(1,s.toString())
            }

        })
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            isOpen = group.checkedRadioButtonId == R.id.radioOpen
        }

    }

    val isQrCode = false

        private fun goQrCode() {
            val player = MediaPlayer.create(requireContext(), R.raw.ding)
            ScanQrCodeActivity.start(activity, object : ScanQrCodeActivity.QrCallBack {
            override fun qrData(result: String) {
                editProductName.append(result + "\n")
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

    var isUpLoad = false
    var currentPosition = 0
    val list = mutableListOf<String>()
    private fun initAdapterData() {
        if (list.size < 9) {
            list.add("1")
            addPicAdapter.setList(list)
        } else {
            "不能再添加图片了".toast()
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

    private val qutalityListDialog by lazy {
        QutalityListDialog(requireContext())
    }

    val array: HashMap<Int, String> = HashMap()
    val arrayId: HashMap<Int, String> = HashMap()
    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is StringEvent) {
            if (messageEvent.code == 103) {
                array[currentPosition] = messageEvent.path
                addPicAdapter.setPic(currentPosition, Uri.parse(messageEvent.message))
                Log.e("获取路径1", messageEvent.path)
            }
            if (messageEvent.code == 2000) {
                Log.e("12121", messageEvent.path)
                zhijianID = messageEvent.message
                editQResult.setText(messageEvent.path)
            }
        }

    }


}