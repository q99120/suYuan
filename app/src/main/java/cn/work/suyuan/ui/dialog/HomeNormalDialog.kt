package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import kotlinx.android.synthetic.main.dialog_home_normal.*
import kotlinx.android.synthetic.main.dialog_home_normal.etMgName
import kotlinx.android.synthetic.main.dialog_home_normal.tvHomeCancel
import kotlinx.android.synthetic.main.dialog_home_normal.tvHomeConfirm
import kotlinx.android.synthetic.main.dialog_home_normal.view.*

class HomeNormalDialog : Dialog {
    private lateinit var homeNormalClick: HomeNormalClick

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_home_normal)
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }


    private fun initView() {
        setOnClickListener(tvHomeConfirm, tvHomeCancel) {
            when (this) {
                tvHomeCancel -> dismiss()
                tvHomeConfirm -> {
                    dialogClicks()
                    dismiss()
                }
            }
        }
    }

    private fun dialogClicks() {
        if (etMgName.text.isNotEmpty() )
            if (etMgName.text.isNotEmpty() && etSort.text.isNotEmpty()){
                homeNormalClick.dialogClick(etMgName.text.toString(), etSort.text.toString().toInt())
            }
    }

    fun action(index: Int, name: String, sort: Int, param: HomeNormalClick) {
        homeNormalClick = param
        when (index) {
            1 -> {
                llId.visibility = VISIBLE
                tvDialogTitle.text = "添加数据"
            }
            2 -> {
                llId.visibility = GONE
                etMgName.setText(name)
                etSort.setText(sort.toString())
                tvDialogTitle.text = "编辑数据"
            }
        }
        show()
    }





    interface HomeNormalClick {
        fun dialogClick(processName: String, sort: Int)
    }


}