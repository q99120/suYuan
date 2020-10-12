package cn.work.suyuan.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.ui.adapter.SingleSpinnerAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

class SinglePopUtil(context: Context, param: popClick) : PopupWindow(context) {

    var callbacks: popClick = param

    private lateinit var singleSpinnerAdapter: SingleSpinnerAdapter

    init {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val contentView = LayoutInflater.from(context).inflate(
            R.layout.pop_single_spinner,
            null, false
        )
        singleSpinnerAdapter = SingleSpinnerAdapter()
        val rcl: RecyclerView = contentView.findViewById(R.id.recyclerSinglePop)
        val btnCancel:TextView = contentView.findViewById(R.id.tvPopCancel)
        rcl.layoutManager = LinearLayoutManager(context)
        rcl.adapter = singleSpinnerAdapter

        btnCancel.setOnClickListener { dismiss() }

        singleSpinnerAdapter.setOnItemClickListener { adapter, view, position ->
            callbacks.clickPop(singleSpinnerAdapter.data[position].title,singleSpinnerAdapter.data[position].id)
        }
        setContentView(contentView)
    }

    fun setData(data: MutableList<HomeData.Data>) {
          singleSpinnerAdapter.setList(data)
    }

    //定义一个回调接口
    interface popClick {
        fun clickPop(type: String, pi: Int)
    }
}