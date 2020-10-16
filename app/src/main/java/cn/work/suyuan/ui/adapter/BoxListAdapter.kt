package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.TokenData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BoxListAdapter :
    BaseQuickAdapter<TokenData.Data.DataArrays, BaseViewHolder>(R.layout.adapter_box_list) {
    override fun convert(holder: BaseViewHolder, item: TokenData.Data.DataArrays) {
        if (fmStatus == 1) setTextTitle(holder,"ID","小箱条码","箱内产品条码","产品数量","归属大箱条码","发货时间")
        else setTextTitle(holder,"ID","外箱条码","内箱条码","产品数量","归属大箱条码","发货时间")

        holder.setText(R.id.tvLabel1, item.id.toString())
        holder.setText(R.id.tvLabel2, item.carton)
        holder.setText(R.id.tvLabel3, item.product)
        holder.setText(R.id.tvLabel4, item.nickname)
        holder.setText(R.id.tvLabel5, item.ip)
        holder.setText(R.id.tvLabel6, item.product_time)

//        if (!isInVisible)  holder.setImageResource(R.id.ivCheckOut,R.mipmap.uncheck)
//        if (item.isCheck ){
//            holder.setImageResource(R.id.ivCheckOut,R.mipmap.checkin)
//        }else{
//           holder.setImageResource(R.id.ivCheckOut,R.mipmap.uncheck)
//        }
    }

    private fun setTextTitle(
        holder: BaseViewHolder,
        s: String,
        s1: String,
        s2: String,
        s3: String,
        s4: String,
        s5: String
    ) {
        holder.setText(R.id.tvTitle1,s)
        holder.setText(R.id.tvTitle2,s1)
        holder.setText(R.id.tvTitle3,s2)
        holder.setText(R.id.tvTitle4,s3)
        holder.setText(R.id.tvTitle5,s4)
        holder.setText(R.id.tvTitle6,s5)
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