package cn.work.suyuan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.singleClick
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.ui.adapter.QuListAdapter
import cn.work.suyuan.ui.dialog.ExitDialog
import cn.work.suyuan.ui.dialog.PhotoDialog
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.InjectorUtil
import com.yanzhenjie.recyclerview.*
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_qutality_list.*
import kotlinx.android.synthetic.main.layout_qutality_list.smartRefresh
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import kotlinx.android.synthetic.main.layout_zhijian.*
import kotlinx.android.synthetic.main.layout_zhijian.ivBack
import kotlinx.android.synthetic.main.smart_refresh_recly.*

class QualityActivity:BaseActivity() {
    private val spinnerAdapter = QuListAdapter(1)


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_qutality_list)
        recyclerQualityList.layoutManager = LinearLayoutManager(this)
        recyclerQualityList.adapter =spinnerAdapter
        initView()
        initObserver()

    }

    var zhijianID = ""
    var productFile = ""
    private fun initObserver() {
        viewModel.qutalityListLiveData.observe(this, Observer {
            val rp = it.getOrNull()
            if (rp ==null){
                Requfresh()
                return@Observer
            }
            totalPage = rp.data.last_page
            tvPageInfo.text = "当前页为第"+rp.data.current_page+"页  "+"总页数为"+rp.data.last_page+"页"
            if (rp.code == 200) {
                Log.e("获取质检报告", rp.data.toString())
                if (currentPage == 1){
                    spinnerAdapter.setList(rp.data.data)
                }else{
                    if (!isSwap){
                        spinnerAdapter.addData(rp.data.data)
                    }else{
                        spinnerAdapter.setList(rp.data.data)
                    }
                }
//                        "获取质检报告成功".toast()
//                        zhijianID = id.toString()
//                        checkQuality.text = reportName
            }
        })
        smartRefresh.setOnLoadMoreListener {
            isSwap = false
            smartRefresh.finishLoadMore(1000)
            if (currentPage<totalPage){
                next()
              Requfresh()
            }else{
                "没有更多数据了".toast()
                smartRefresh.finishLoadMoreWithNoMoreData() //设置之后，将不会再触发加载事件
            }
        }
        viewModel.deleteReportLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200){
                "删除成功".toast()
                spinnerAdapter.remove(spinnerAdapter.data[currentPosition])
//                Requfresh()
            }else{
                rp.msg.toast()
            }
        })
    }

    var isSwap = false
    private fun Requfresh() {
        viewModel.getQutalityList(currentPage,"")
    }

    private fun initView() {
        btnSwap.singleClick {
            if (editPageInfo.text.toString() == "0"){
                "跳转页码不能为0".toast()
                return@singleClick
            }else{
                isSwap = true
                currentPage == (editPageInfo.text.toString()).toInt()
                Requfresh()
            }
        }
        editQuSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.getQutalityList(1,s.toString())
            }

        })
        btnScan.singleClick {
            viewModel.getQutalityList(1,editQuSearch.text.toString())
        }
        smartRefresh.setEnableFooterFollowWhenNoMoreData(true)

        ivBack.setOnClickListener {
            finish()
        }
        ivAddQu.setOnClickListener {
            AddQualityActivity.start(this,1,0)
        }
        smartRefresh.setOnRefreshListener{
            currentPage = 1
            Requfresh()
            smartRefresh.finishRefresh(1500)
        }

            smartRefresh.setOnLoadMoreListener {
                smartRefresh.finishLoadMore(2000)
//                if (currentPage < totalPage) {
//                    next()
//                    refreshPack()
//                } else {
//                    smartRefresh.finishLoadMore(true)
//                    "没有更多数据了".toast()
//                }
            }
        spinnerAdapter.addChildClickViewIds(R.id.ivSeePic,R.id.ivTrace,R.id.ivUpdate,R.id.ivDelete)
        spinnerAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.ivSeePic->{
                    photoDialog.initData(spinnerAdapter.data[position].test_report_img)
                }
                R.id.ivTrace->{
                    val intent = intent
                    intent.putExtra("quId",spinnerAdapter.data[position].id)
                    intent.putExtra("quContent",spinnerAdapter.data[position].test_report)
                    this.setResult(-1,intent)
                    finish()
                }
                R.id.ivUpdate->{
                    AddQualityActivity.start(this,2,spinnerAdapter.data[position].id)
//                    photoDialog.initData(spinnerAdapter.data[position].test_report_img)
                }
                R.id.ivDelete->{
                    exitDialog.setClick("确认删除","确定删除该质检报告吗?",object :ExitDialog.HomeNormalClick{
                        override fun dialogClick() {
                            currentPosition = position
                            viewModel.deleteReport(spinnerAdapter.data[position].id)
                        }
                    })
//                    photoDialog.initData(spinnerAdapter.data[position].test_report_img)
                }
            }}
        editPageInfo.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if ((s.toString()).toInt()>totalPage){
                editPageInfo.setText(totalPage.toString())
                }
            }

        })

    }

    var currentPosition = 0
    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, QualityActivity::class.java))
        }
    }



    var currentPage = 1
    var totalPage = 1


    fun next():Int{
        currentPage+=1
        return currentPage
    }

    private val photoDialog by lazy {
        PhotoDialog(this)
    }
    val exitDialog by lazy {
        ExitDialog(this)
    }

    override fun onResume() {
        Requfresh()
        super.onResume()
    }
}