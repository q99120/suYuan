package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.ui.adapter.FileListAdapter
import cn.work.suyuan.util.FileUtils
import kotlinx.android.synthetic.main.dialog_single_spinner.*

class SingleSpinnerDialog : Dialog {
    private lateinit var homeNormalClick: HomeNormalClick

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_single_spinner)
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }


    private fun initView() {

        setOnClickListener(tvCancel) {
            when (this) {
                tvCancel -> dismiss()
            }
        }
    }

    fun initSpinner(cate: MutableList<FileUtils.FileParam>, param: HomeNormalClick) {
        homeNormalClick = param
        val adapters = FileListAdapter()
        recyclerSpinner.layoutManager = LinearLayoutManager(context)
        recyclerSpinner.adapter = adapters
        adapters.setList(cate)
        show()

        adapters.setOnItemClickListener { adapter, view, position ->
            adapters.setCheck(position)
            homeNormalClick.dialogClick(adapters.data[position].fileName,adapters.data[position].filePath.toInt())
            dismiss()
       }
    }



    interface HomeNormalClick {
        fun dialogClick(processName: String, sort: Int)
    }


}