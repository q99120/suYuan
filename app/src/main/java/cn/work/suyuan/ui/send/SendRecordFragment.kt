package cn.work.suyuan.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_home_child.*

class SendRecordFragment: BaseFragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_child, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ll_date.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = SendRecordFragment()
    }


}