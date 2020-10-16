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

    private val spinnerAdapter = FileListAdapter()
    var dialogPosition  = -1
    private fun initView() {



    }

   fun setData(flag:Int,queryFiles: MutableList<FileUtils.FileParam>, param: FileClick) {
       if (flag == 1) tvFileTitle.text = "文件格式选择" else tvFileTitle.text = "文件选择"
       fileClicks = param
       recyclerFileList.layoutManager = LinearLayoutManager(context)
       recyclerFileList.adapter = spinnerAdapter
       spinnerAdapter.setList(queryFiles)
       show()

       spinnerAdapter.setOnItemClickListener { adapter, view, position ->
           dialogPosition = position
           spinnerAdapter.setCheck(position)
       }

       setOnClickListener(btnCancel,btnConfirm){
           when(this){
               btnCancel->dismiss()
               btnConfirm->{
                   fileClicks.fileClick(spinnerAdapter.data[dialogPosition].fileName,spinnerAdapter.data[dialogPosition].filePath)
                   if (flag == 2){
                       dismiss()
                   }
               }
           }
       }


   }

    interface FileClick{
        fun fileClick(fileName:String,filePath:String)
    }

}