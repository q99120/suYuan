package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.logic.model.QutalityBean
import cn.work.suyuan.ui.adapter.QuListAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.dialog_qutality_list.*

class QutalityListDialog : Dialog {
    lateinit var quClick:FileClick
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_qutality_list)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        initView()
    }

    private val spinnerAdapter = QuListAdapter()
    var dialogPosition  = -1
    private fun initView() {
        btnCancel.setOnClickListener { dismiss() }
        blackBack.setOnClickListener { dismiss() }


    }


    fun initDialog(data: MutableList<QutalityBean.Datas>, param: FileClick) {
        quClick = param
        Log.e("拿到data数据",data.toString())
        recyclerQuality.layoutManager = LinearLayoutManager(context)
        recyclerQuality.adapter = spinnerAdapter
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

    interface FileClick{
        fun fileClick(reportName:String,id:Int)
    }

}