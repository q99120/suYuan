package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.ui.adapter.FileListAdapter
import cn.work.suyuan.ui.adapter.NormalStringAdapter
import cn.work.suyuan.util.FileUtils
import kotlinx.android.synthetic.main.dialog_choose_file.*
import kotlinx.android.synthetic.main.dialog_normal_string.*

class NormalStringDialog : Dialog {
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_normal_string)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ivBack.setOnClickListener { dismiss() }
    }

    fun setDatas(data:MutableList<String>){
        tvUpHead.text = "产品数据条目(共"+data.size+"条)"
        normalStringList.layoutManager = LinearLayoutManager(context)
        val adapter = NormalStringAdapter()
        normalStringList.adapter = adapter
        adapter.setList(data)
        show()
    }



}