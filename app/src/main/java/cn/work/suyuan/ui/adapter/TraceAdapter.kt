package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.TokenData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.layoutadtitle.*

class TraceAdapter :
    BaseQuickAdapter<TokenData.Data.DataArrays, BaseViewHolder>(R.layout.adapter_normal_list) {
    override fun convert(holder: BaseViewHolder, item: TokenData.Data.DataArrays) {
        if (fmStatus == 1){
            holder.setText(R.id.tvTitle1,"ID")
            holder.setText(R.id.tvTitle2,"流程名称")
            holder.setText(R.id.tvTitle3,"产品条码")
            holder.setText(R.id.tvTitle4,"操作人")
            holder.setText(R.id.tvTitle5,"生产时间")
            holder.setText(R.id.tvLabel1, item.id.toString())
            holder.setText(R.id.tvLabel2, item.category_name)
            holder.setText(R.id.tvLabel3, item.product)
            holder.setText(R.id.tvLabel4, item.uname)
            holder.setText(R.id.tvLabel5, item.product_time)
            holder.setVisible(R.id.tvLabel4,true)
            holder.setVisible(R.id.tvLabel5,true)
        }else if (fmStatus == 2){
            holder.setText(R.id.tvTitle1,"操作员")
            holder.setText(R.id.tvTitle2,"条码")
            holder.setText(R.id.tvTitle3,"产品名称")
            holder.setText(R.id.tvTitle4,"经销商")
            holder.setText(R.id.tvTitle5,"日期")
            holder.setText(R.id.tvTitle6,"IP")
            holder.setText(R.id.tvLabel1, item.nickname.toString())
            holder.setText(R.id.tvLabel2, item.product)
            holder.setText(R.id.tvLabel3, item.name)
            holder.setText(R.id.tvLabel4, item.nickname)
            holder.setText(R.id.tvLabel5, item.product_time)
            holder.setText(R.id.tvLabel6, item.ip)
            holder.setVisible(R.id.tvLabel4,true)
            holder.setVisible(R.id.tvLabel5,true)
            holder.setVisible(R.id.tvLabel6,true)
        }else{
            holder.setText(R.id.tvTitle1,"ID")
            holder.setText(R.id.tvTitle2,"操作员")
            holder.setText(R.id.tvTitle3,"箱码")
            holder.setText(R.id.tvTitle4,"时间")
            holder.setText(R.id.tvTitle5,"IP")
            holder.setText(R.id.tvLabel1, item.id.toString())
            holder.setText(R.id.tvLabel2, item.nickname)
            holder.setText(R.id.tvLabel3, item.carton)
            holder.setText(R.id.tvLabel4, item.product_time)
            holder.setText(R.id.tvLabel5, item.ip)
            holder.setVisible(R.id.tvLabel4,true)
            holder.setVisible(R.id.tvLabel5,true)
        }





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

    fun setfmStatus(i: Int) {
       fmStatus = i
    }

    var fmStatus = 1
}