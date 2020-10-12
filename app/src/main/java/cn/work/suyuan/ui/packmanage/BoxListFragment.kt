package cn.work.suyuan.ui.packmanage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*

class BoxListFragment :BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_child, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edit_search_bar.hint = "请输入条码"
        ll_date.visibility = View.VISIBLE
        rlEndDate.visibility = View.GONE
    }

    companion object {
        fun newInstance() = BoxListFragment()
    }
}