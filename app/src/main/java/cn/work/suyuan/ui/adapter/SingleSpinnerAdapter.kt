package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.TokenData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 下拉单选列表
 */
class SingleSpinnerAdapter :
    BaseQuickAdapter<HomeData.Data, BaseViewHolder>(R.layout.adapter_single_spinner) {
    override fun convert(holder: BaseViewHolder, item:HomeData.Data) {
        if (selectPosition == holder.bindingAdapterPosition){
            holder.setImageResource(R.id.ivPopItemCheck,R.mipmap.circlein)
        }else{
            holder.setImageResource(R.id.ivPopItemCheck,R.mipmap.circleout)
        }
        if (flags == 1) holder.setText(R.id.tvPopItemTitle,item.title)
        else holder.setText(R.id.tvPopItemTitle,item.name) }

    private var flags = -1
    fun setType(flag: Int) {
            flags = flag
    }


    var selectPosition = -1
    fun setCheck(position:Int){
        selectPosition = position
        notifyItemChanged(selectPosition)
    }

}