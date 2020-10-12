package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class HomeNormalAdapter :
    BaseQuickAdapter<HomeData.Data, BaseViewHolder>(R.layout.adapter_home_normal) {
    override fun convert(holder: BaseViewHolder, item: HomeData.Data) {
        holder.setText(R.id.tvHomeTitle, item.tab)
        holder.setText(R.id.tvID, item.id.toString())
        holder.setText(R.id.tvSort, item.sort.toString())
        if (!isInVisible)  holder.setImageResource(R.id.ivCheckOut,R.mipmap.uncheck)
        if (item.isCheck ){
            holder.setImageResource(R.id.ivCheckOut,R.mipmap.checkin)
        }else{
           holder.setImageResource(R.id.ivCheckOut,R.mipmap.uncheck)
        }
    }

    private var isInVisible = false
    //是否多选
    private var isMu = false
    fun setCheckVis(isVisible: Boolean) {
        isInVisible = isVisible
        notifyDataSetChanged()
    }

    private var selectCheck = -1
    fun changeCheck(position: Int) {
        selectCheck = position
        notifyDataSetChanged()
    }

    /**
     * 重写position获取事件解决复用问题
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }
}