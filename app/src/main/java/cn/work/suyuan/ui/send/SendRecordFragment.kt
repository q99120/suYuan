package cn.work.suyuan.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.adapter.TraceAdapter
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layoutadtitle.*

class SendRecordFragment: BaseFragment(){

    val traceAdapter = TraceAdapter()
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_child, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeMgRecycler.layoutManager = LinearLayoutManager(requireContext())
        homeMgRecycler.adapter = traceAdapter
        traceAdapter.setfmStatus(2)
        tvTitle1.text = "操作员"
        tvTitle2.text = "条码"
        tvTitle3.text = "产品名称"
        tvTitle4.text = "经销商"
        tvTitle5.text = "日期"
        tvTitle6.text = "IP"
        ll_date.visibility = View.VISIBLE
        observer()
    }

    private fun observer() {
        viewModel.sendRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            val data = rp.data.data
            if (data.isNotEmpty()){
                traceAdapter.setList(data)
            }
        })
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        viewModel.sendRecord("","","",1)
    }

    companion object {
        fun newInstance() = SendRecordFragment()
    }


}