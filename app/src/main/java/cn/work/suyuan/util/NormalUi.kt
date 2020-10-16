package cn.work.suyuan.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.work.suyuan.R
import cn.work.suyuan.SuYuanApplication

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

    /**
     * 获取当前应用程序的名称。
     * @return 当前应用程序的名称。
     */
    val appName: String
        get() = SuYuanApplication.context.resources.getString(SuYuanApplication.context.applicationInfo.labelRes)

}