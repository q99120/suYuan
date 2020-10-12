package cn.work.suyuan.ui.packmanage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import kotlinx.android.synthetic.main.layout_send_manage_fm.*

class BoxInboxFragment :BaseFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_pack_manage, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    companion object {
        fun newInstance() = BoxInboxFragment()
    }
}