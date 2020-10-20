package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.ui.adapter.FileListAdapter
import cn.work.suyuan.util.FileUtils
import kotlinx.android.synthetic.main.dialog_choose_file.*
import kotlinx.android.synthetic.main.dialog_up_user.*

class UpdateUserDialog : Dialog {
    lateinit var upUserCallBack:UpUserCallBack
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_up_user)
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }

    private fun initView() {
        tvFinish.setOnClickListener {
            tvFinish.setOnClickListener {
                upUserCallBack.upUser(editAgeName.text.toString())
            dismiss()}
            llFinish2.setOnClickListener { upUserCallBack.upUser(editDescribe.text.toString())
            dismiss()}
        }

    }

    fun updateData(flag:Int,param:UpUserCallBack){
        upUserCallBack = param
        when (flag) {
            1 -> {
                editAgeName.inputType = InputType.TYPE_CLASS_TEXT
                llNameAge.visibility  = View.VISIBLE
            }
            2 -> {
                llDescribe.visibility = View.VISIBLE
            }
            3->{
                editAgeName.inputType = InputType.TYPE_CLASS_NUMBER
                llNameAge.visibility  = View.VISIBLE
            }
        }
        show()
    }

    interface UpUserCallBack{
        fun upUser(result:String)
    }


}