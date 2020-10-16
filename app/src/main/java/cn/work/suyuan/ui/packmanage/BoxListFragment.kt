package cn.work.suyuan.ui.packmanage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.adapter.BoxListAdapter
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_pack_list.*
import kotlinx.android.synthetic.main.layout_pack_list_recly_title.*

/**
 * 外箱列表(包含大箱和小箱子）
 */
class BoxListFragment : BaseFragment() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_pack_list, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observer()
    }


    var boxFlag = 1
    val adapter = BoxListAdapter()
    private fun initView() {
        setTextTitle("ID","小箱条码","箱内产品条码","产品数量","归属大箱条码","发货时间")
        recyclerPackList.layoutManager = LinearLayoutManager(activity)
        recyclerPackList.adapter = adapter
        setOnClickListener(tvSmallBox, tvBigBox) {
            when (this) {
                tvSmallBox -> {
                    setTextTitle("ID","小箱条码","箱内产品条码","产品数量","归属大箱条码","发货时间")
                    boxFlag = 1
                    tvSmallBox.setBackgroundResource(R.mipmap.packlistleft)
                    tvBigBox.setBackgroundResource(R.color.transparent)
                    getBoxList("","2020-10-09",boxFlag,1)
                }
                tvBigBox -> {
                    setTextTitle("ID","外箱条码","内箱条码","产品数量","归属大箱条码","发货时间")
                    boxFlag = 2
                    tvBigBox.setBackgroundResource(R.mipmap.packlistright)
                    tvSmallBox.setBackgroundResource(R.color.transparent)
                    getBoxList("","2020-10-09",boxFlag,1)
                }
            }
        }
    }

    private fun setTextTitle(s: String, s1: String, s2: String, s3: String, s4: String, s5: String) {
         tvTitle1.text = s
        tvTitle2.text = s1
        tvTitle3.text = s2
        tvTitle4.text = s3
        tvTitle5.text = s4
        tvTitle6.text = s5
    }

    private fun observer() {
        viewModel.boxRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            if (rp.code == 200){
                if ((rp.data.data).isNotEmpty()){
                    adapter.setList(rp.data.data)
                }else "没有数据".toast()
            }
        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
        getBoxList("","2020-10-09",boxFlag,1)
    }

    private fun getBoxList(product: String, time: String, flag: Int, page: Int) {
        viewModel.getBoxRecord(product,time,flag,page)
    }

    companion object {
        fun newInstance() = BoxListFragment()
    }
}