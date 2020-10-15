package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.ui.adapter.FileListAdapter
import cn.work.suyuan.util.FileUtils
import kotlinx.android.synthetic.main.dialog_choose_file.*

class FileChooseDialog : Dialog {
    lateinit var fileClicks:FileClick
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_choose_file)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }


    private fun initView() {

    }

   fun setData(queryFiles: MutableList<FileUtils.FileParam>, param: FileClick) {
       fileClicks = param
       val spinnerAdapter = FileListAdapter()
       recyclerFileList.layoutManager = LinearLayoutManager(context)
       recyclerFileList.adapter = spinnerAdapter
       spinnerAdapter.setList(queryFiles)
       show()

       spinnerAdapter.setOnItemClickListener { adapter, view, position ->
           spinnerAdapter.setCheck(position)
           fileClicks.fileClick(spinnerAdapter.data[position].fileName,spinnerAdapter.data[position].filePath)
       }


   }

    interface FileClick{
        fun fileClick(fileName:String,filePath:String)
    }

}