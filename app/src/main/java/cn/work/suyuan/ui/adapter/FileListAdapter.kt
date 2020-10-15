package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.util.FileUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 下拉单选列表
 */
class FileListAdapter :
    BaseQuickAdapter<FileUtils.FileParam, BaseViewHolder>(R.layout.adapter_single_spinner) {
    override fun convert(holder: BaseViewHolder, item: FileUtils.FileParam) {
        if (selectPosition == holder.bindingAdapterPosition){
            holder.setImageResource(R.id.ivPopItemCheck,R.mipmap.circlein)
        }else{
            holder.setImageResource(R.id.ivPopItemCheck,R.mipmap.circleout)
        }
        holder.setText(R.id.tvPopItemTitle, item.fileName)
    }

    private var selectPosition = -1
    fun setCheck(position: Int) {
        selectPosition = position
        notifyDataSetChanged()
    }


}