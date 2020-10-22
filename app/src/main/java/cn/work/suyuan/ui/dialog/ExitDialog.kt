package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import kotlinx.android.synthetic.main.dialog_exit_app.*
import kotlinx.android.synthetic.main.dialog_home_normal.*
import kotlinx.android.synthetic.main.dialog_home_normal.etMgName
import kotlinx.android.synthetic.main.dialog_home_normal.tvHomeCancel
import kotlinx.android.synthetic.main.dialog_home_normal.tvHomeConfirm
import kotlinx.android.synthetic.main.dialog_home_normal.view.*

class ExitDialog : Dialog {
    private lateinit var homeNormalClick: HomeNormalClick

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_exit_app)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }

    fun setClick(param:HomeNormalClick){
        homeNormalClick = param
        show()
    }

    private fun initView() {
        setOnClickListener(btnCancel, btnConfirm) {
            when (this) {
                btnCancel -> dismiss()
                btnConfirm -> {
                    homeNormalClick.dialogClick()
                    dismiss()
                }
            }
        }
    }

    fun setClick(title: String, content: String, homeNormalClick: ExitDialog.HomeNormalClick) {
        tvTitle.text = title
        tvContent.text = content
        setOnClickListener(btnCancel, btnConfirm) {
            when (this) {
                btnCancel -> dismiss()
                btnConfirm -> {
                    homeNormalClick.dialogClick()
                    dismiss()
                }
            }
        }
        show()
    }


    interface HomeNormalClick {
        fun dialogClick()
    }


}