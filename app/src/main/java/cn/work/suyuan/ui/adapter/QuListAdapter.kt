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
    BaseQuickAdapter<QutalityBean.Datas, BaseViewHolder>(R.layout.adapter_single_spinner) {
    override fun convert(holder: BaseViewHolder, item: QutalityBean.Datas) {
        holder.setGone(R.id.ivPopItemCheck,true)
        holder.setText(R.id.tvPopItemTitle, item.test_report)
    }

    private var selectPosition = -1
    fun setCheck(position: Int) {
        selectPosition = position
        notifyDataSetChanged()
    }


}