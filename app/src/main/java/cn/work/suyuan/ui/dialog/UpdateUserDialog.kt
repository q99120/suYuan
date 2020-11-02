package cn.work.suyuan.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.work.suyuan.R
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

    fun updateData(flag: Int, param: UpUserCallBack){
        upUserCallBack = param
        when (flag) {
            1 -> {
                editAgeName.text.clear()
                textSize.text = "1/"+14
                editAgeName.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(14))
                editAgeName.inputType = InputType.TYPE_CLASS_TEXT
                llNameAge.visibility = View.VISIBLE
            }
            2 -> {
                llDescribe.visibility = View.VISIBLE
            }
            3 -> {
                editAgeName.text.clear()
                textSize.text = "1/"+3
                editAgeName.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
                editAgeName.inputType = InputType.TYPE_CLASS_NUMBER
                llNameAge.visibility = View.VISIBLE
            }
        }
        editAgeName.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                if (flag == 1) textSize.text = s.toString().length.toString()+"/"+14
                else   if (flag == 1) textSize.text = s.toString().length.toString()+"/"+3
            }


        })
        show()
    }

    interface UpUserCallBack{
        fun upUser(result: String)
    }


}