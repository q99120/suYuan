package cn.work.suyuan.ui.adapter

import android.net.Uri
import android.widget.ImageView
import cn.work.suyuan.R
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.widget.GlideEngine
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine.*

class AddPicAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_add_pic) {
    override fun convert(holder: BaseViewHolder, item:String) {
        if (holder.bindingAdapterPosition == pI){
            val ivImage:ImageView = holder.getView(R.id.ivPic)
            GlideEngine.getInstance()
                .loadPhotoNoCircle(context, path!!, ivImage)
        }

    }

    var pI = -1
    var path: Uri? = null
    fun setPic(currentPosition: Int, uriPath: Uri) {
        pI = currentPosition
        path = uriPath
        notifyItemChanged(currentPosition)
    }
}