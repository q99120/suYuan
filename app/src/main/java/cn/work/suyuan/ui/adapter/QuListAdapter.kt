package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.QutalityBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 下拉单选列表
 */
class QuListAdapter(val flag: Int) :
    BaseQuickAdapter<QutalityBean.Data.Datas, BaseViewHolder>(R.layout.adapter_qulist) {
    override fun convert(holder: BaseViewHolder, item: QutalityBean.Data.Datas) {
        holder.setText(R.id.tvPopItemTitle, item.test_report)
        holder.setText(R.id.tvId,item.id.toString()+".")
        if (flag == 2) {
            holder.setVisible(R.id.ivSeePic,false)
            holder.setVisible(R.id.ivUpdate,false)
            holder.setVisible(R.id.ivDelete,false)
            holder.setVisible(R.id.ivTrace,false)
        }
    }

    private var selectPosition = -1
    fun setCheck(position: Int) {
        selectPosition = position
        notifyDataSetChanged()
    }


}