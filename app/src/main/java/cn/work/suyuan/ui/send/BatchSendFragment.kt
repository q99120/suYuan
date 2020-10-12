package cn.work.suyuan.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import kotlinx.android.synthetic.main.layout_send_manage_fm.*

/**
 * 批量发货
 */

class BatchSendFragment: BaseFragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_send_manage, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvSendQrTitle.visibility = View.GONE
        tvActionQr.visibility = GONE
    }

    companion object {
        fun newInstance() = BatchSendFragment()
    }


}