package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.QutalityBean
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.util.FileUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 下拉单选列表
 */
class QuListAdapter :
    BaseQuickAdapter<QutalityBean.Data.Datas, BaseViewHolder>(R.layout.adapter_qulist) {
    override fun convert(holder: BaseViewHolder, item: QutalityBean.Data.Datas) {
        holder.setText(R.id.tvPopItemTitle, item.test_report)
        holder.setText(R.id.tvId,item.id.toString()+".")
    }

    private var selectPosition = -1
    fun setCheck(position: Int) {
        selectPosition = position
        notifyDataSetChanged()
    }


}