package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class HomeThreeItemAdapter :
    BaseQuickAdapter<HomeData.Data, BaseViewHolder>(R.layout.adapter_three_item) {
    override fun convert(holder: BaseViewHolder, item: HomeData.Data) {
        holder.setText(R.id.tvLabel1, item.id.toString())
        holder.setText(R.id.tvLabel2, item.tab)
        holder.setText(R.id.tvLabel3, item.sort.toString())
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



    /**
     * 重写position获取事件解决复用问题
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }

    private var fmStatus = 1
    fun setFmStatus(flag:Int){
       fmStatus = flag
    }
}