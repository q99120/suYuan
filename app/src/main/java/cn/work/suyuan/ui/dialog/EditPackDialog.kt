package cn.work.suyuan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import kotlinx.android.synthetic.main.dialog_edit_pack.*
import kotlinx.android.synthetic.main.dialog_home_normal.*
import kotlinx.android.synthetic.main.dialog_home_normal.etMgName
import kotlinx.android.synthetic.main.dialog_home_normal.etSort
import kotlinx.android.synthetic.main.dialog_home_normal.tvHomeCancel
import kotlinx.android.synthetic.main.dialog_home_normal.tvHomeConfirm
import kotlinx.android.synthetic.main.dialog_home_normal.view.*

class EditPackDialog : Dialog {
    private lateinit var homeNormalClick: HomeNormalClick

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.dialog_edit_pack)
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }


    private fun initView() {
        setOnClickListener(tvHomeConfirm, tvHomeCancel,BigBoxActionQr) {
            when (this) {
                tvHomeCancel -> dismiss()
                tvHomeConfirm -> {
                    dialogClicks()
                    dismiss()
                }
                BigBoxActionQr->{

                }
            }
        }
    }

    private fun dialogClicks() {
    }


    fun actionBoxRecord(
        index: Int,
        id: Int,
        product: String,
        carton: String,
        homeNormalClick: HomeNormalClick
    ) {
        tvBigBoxCode.text = carton
        show()
    }


    interface HomeNormalClick {
        fun dialogClick(processName: String, sort: Int)
    }


}