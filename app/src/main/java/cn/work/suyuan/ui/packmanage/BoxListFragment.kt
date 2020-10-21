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
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import kotlinx.android.synthetic.main.fragment_pack_list.*
import kotlinx.android.synthetic.main.layout_pack_list_recly_title.*
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.smart_refresh_recly.*

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
        getBoxList("","2020-10-09",boxFlag,1)

        setTextTitle("ID","小箱条码","箱内产品条码","产品数量","归属大箱条码","发货时间")
        homeMgRecycler.layoutManager = LinearLayoutManager(activity)
        homeMgRecycler.adapter = adapter
        setOnClickListener(tvSmallBox, tvBigBox,tv_search) {
            when (this) {
                tvSmallBox -> {
                    currentPage = 1
                    setTextTitle("ID","小箱条码","箱内产品条码","产品数量","归属大箱条码","发货时间")
                    boxFlag = 1
                    tvSmallBox.setBackgroundResource(R.mipmap.packlistleft)
                    tvBigBox.setBackgroundResource(R.color.transparent)
                    refreshData()
                }
                tvBigBox -> {
                    currentPage = 1
                    setTextTitle("ID","外箱条码","内箱条码","产品数量","归属大箱条码","发货时间")
                    boxFlag = 2
                    tvBigBox.setBackgroundResource(R.mipmap.packlistright)
                    tvSmallBox.setBackgroundResource(R.color.transparent)
                    refreshData()
                }
                tv_search->{
                    refreshData()
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
                totalPage = rp.data.total
                if ((rp.data.data).isNotEmpty()){
                    layoutRecyclerView.visibility = View.VISIBLE
                    layoutEmptyView.visibility = View.INVISIBLE
                    if (currentPage == 1){
                        adapter.setList(rp.data.data)
                    }else{
                        adapter.addData(rp.data.data)
                    }
                }else {
                    layoutRecyclerView.visibility = View.INVISIBLE
                    layoutEmptyView.visibility = View.VISIBLE
                }
            }
        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
        initRefresh()
    }

    private fun getBoxList(product: String, time: String, flag: Int, page: Int) {
        viewModel.getBoxRecord(product,time,flag,page)
    }

    companion object {
        fun newInstance() = BoxListFragment()
    }

    private var currentPage = 1
    private var totalPage = 1

    fun refreshData(){
        getBoxList(edit_search_bar.text.toString(),"",boxFlag,currentPage)
    }

    fun next(): Int {
        currentPage += 1
        return currentPage
    }

    fun initRefresh(){
        smartRefresh.setOnLoadMoreListener(object :OnLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                smartRefresh.finishLoadMore(2000)
                if (currentPage<totalPage){
                    next()
                    refreshData()
                }else{
                    smartRefresh.finishLoadMore(true)
                    "没有更多数据了".toast()
                }
            }
        })
    }
}