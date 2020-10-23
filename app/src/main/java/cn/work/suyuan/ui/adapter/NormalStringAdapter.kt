package cn.work.suyuan.ui.adapter

import cn.work.suyuan.R
import cn.work.suyuan.logic.model.SendRecord
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.util.DateUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class NormalStringAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_string) {
    override fun convert(holder: BaseViewHolder, item: String) {

        holder.setText(R.id.numString, item)

    }
}