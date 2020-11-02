package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.SendRecord
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.util.DateUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BoxRecordAdapter :
    BaseQuickAdapter<SendRecord.Data.DataArrays, BaseViewHolder>(R.layout.adapter_box_record) {
    override fun convert(holder: BaseViewHolder, item: SendRecord.Data.DataArrays) {

        holder.setText(R.id.tvLabel1, item.id.toString())
        holder.setText(R.id.tvLabel2, item.nickname)
        holder.setText(R.id.tvLabel4, DateUtil.getDateAndTimeMil(item.addtime.toLong()))
        holder.setText(R.id.tvLabel5, item.ip)
        when {
            item.count==1 -> {
                holder.setText(R.id.tvLabel3, item.product[0])
            }
            item.count>1 -> {
                holder.setText(R.id.tvLabel3, item.product[0]+"...")
            }
            item.count==0 -> {
                holder.setText(R.id.tvLabel3, "")
            }
        }


//        if (!isInVisible)  holder.setImageResource(R.id.ivCheckOut,R.mipmap.uncheck)
        if (item.isCheck ){
            holder.setImageResource(R.id.ivCheckTitle,R.mipmap.checkin)
        }else{
           holder.setImageResource(R.id.ivCheckTitle,R.mipmap.uncheck)
        }
    }


    private var isInVisible = false
    //是否多选
    private var isMu = false
    fun setCheckVis(isVisible: Boolean) {
        isInVisible = isVisible
        notifyDataSetChanged()
    }

    private var fmStatus = 1
    fun setFmStatus(flag:Int){
        fmStatus = flag
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