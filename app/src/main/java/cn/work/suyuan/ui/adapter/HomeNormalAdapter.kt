package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class HomeNormalAdapter :
    BaseQuickAdapter<HomeData.Data, BaseViewHolder>(R.layout.adapter_normal_list) {
    override fun convert(holder: BaseViewHolder, item: HomeData.Data) {
            holder.setVisible(R.id.tvTitle1,false)
            holder.setVisible(R.id.tvTitle2,false)
            holder.setVisible(R.id.tvTitle3,false)
            holder.setVisible(R.id.tvTitle4,false)
            holder.setVisible(R.id.tvTitle5,false)
            holder.setVisible(R.id.tvTitle6,false)
            holder.setVisible(R.id.ivCheckTitle,false)
            holder.setVisible(R.id.tvTitleLine,false)

        if (fmStatus == 1){
            setisvi(holder,true)
        }else{
            setisvi(holder,false)
        }
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

    private fun setisvi(holder: BaseViewHolder, b: Boolean) {
        holder.setGone(R.id.tvTitle5,b)
        holder.setGone(R.id.tvTitle4,b)
        holder.setGone(R.id.tvTitle6,b)

        holder.setGone(R.id.tvLabel4,b)
        holder.setGone(R.id.tvLabel5,b)
        holder.setGone(R.id.tvLabel6,b)
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

    private var fmStatus = 1
    fun setFmStatus(flag:Int){
       fmStatus = flag
    }
}