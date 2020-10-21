package cn.work.suyuan.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import cn.work.suyuan.R
import cn.work.suyuan.ui.adapter.SingleSpinnerAdapter


class EditNoMorePop(context: Context,param:popClick) : PopupWindow(context) {

  var popClicks: popClick = param

    private lateinit var singleSpinnerAdapter: SingleSpinnerAdapter

    init {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val contentView = LayoutInflater.from(context).inflate(
            R.layout.dialog_edit_nomore,
            null, false
        )

        val btnNoMore:Button = contentView.findViewById(R.id.btnNoMore)
        btnNoMore.setOnClickListener {
            popClicks.clickPop()
        dismiss()}
        setContentView(contentView)

    }


    //定义一个回调接口
    interface popClick {
        fun clickPop()
    }
}