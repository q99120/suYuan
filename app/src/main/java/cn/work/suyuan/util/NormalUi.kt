package cn.work.suyuan.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.work.suyuan.R

object NormalUi {
    /**
     * 空布局占位
     */
    fun getEmptyDataView(
        mRecyclerView: ViewGroup, inflater: LayoutInflater
    ): View {
        val notDataView: View =
            inflater.inflate(R.layout.layout_empty_view, mRecyclerView, false)
        return notDataView
    }

}