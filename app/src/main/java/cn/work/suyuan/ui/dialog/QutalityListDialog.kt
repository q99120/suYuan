package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.logic.model.QutalityBean
import cn.work.suyuan.ui.adapter.QuListAdapter
import kotlinx.android.synthetic.main.dialog_qutality_list.*
import kotlinx.android.synthetic.main.dialog_qutality_list.recyclerQualityList
import kotlinx.android.synthetic.main.dialog_qutality_list.smartRefresh
import kotlinx.android.synthetic.main.layout_qutality_list.*

class QutalityListDialog : Dialog {
    lateinit var quClick:FileClick
    lateinit var refresh:quRefresh
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_qutality_list)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        initView()
    }

    private val spinnerAdapter = QuListAdapter()
    var dialogPosition  = -1
    var totalPage = 1
    var currentPage = 1
    private fun initView() {
        btnCancel.setOnClickListener { dismiss() }
        blackBack.setOnClickListener { dismiss() }

        smartRefresh.setOnRefreshListener{
            currentPage = 1
            refresh.refresh(1)
            smartRefresh.finishRefresh(1500)
        }

        smartRefresh.setOnLoadMoreListener {
            smartRefresh.finishLoadMore(1000)
            if (currentPage<totalPage){
                next()
                refresh.refresh(currentPage)
            }else{
                "没有更多数据了".toast()
                smartRefresh.finishLoadMoreWithNoMoreData() //设置之后，将不会再触发加载事件
            }
        }


    }

    fun next():Int{
        currentPage+=1
        return currentPage
    }
    fun initDialog(data: MutableList<QutalityBean.Data.Datas>, total: Int, param: FileClick,param1:quRefresh) {
        totalPage = total
        quClick = param
        refresh = param1
        Log.e("拿到data数据",data.toString())
        recyclerQualityList.layoutManager = LinearLayoutManager(context)
        recyclerQualityList.adapter = spinnerAdapter
        spinnerAdapter.setList(data)
          show()

        spinnerAdapter.setOnItemClickListener { adapter, view, position ->
            quClick.fileClick(
                spinnerAdapter.data[position].test_report,
                spinnerAdapter.data[position].id
            )
            dismiss()
        }
    }
    fun addList(data: MutableList<QutalityBean.Data.Datas>){
        spinnerAdapter.addData(data)
    }



    interface quRefresh{
        fun refresh(page:Int)
    }

    interface FileClick{
        fun fileClick(reportName:String,id:Int)
    }

}