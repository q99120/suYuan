package cn.work.suyuan.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment

/**
 * 取消单个发货
 */

class CancelSingleFragment: BaseFragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_cancel_send, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        fun newInstance() = CancelSingleFragment()
    }


}