package cn.work.suyuan.ui.packmanage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import cn.work.suyuan.ui.adapter.BoxRecordAdapter
import cn.work.suyuan.ui.dialog.ExitDialog
import cn.work.suyuan.ui.dialog.NormalStringDialog
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_pack_list.*
import kotlinx.android.synthetic.main.fragment_pack_list.layoutEmptyView
import kotlinx.android.synthetic.main.fragment_pack_list.tvRecordSize
import kotlinx.android.synthetic.main.layout_pack_list_recly_title.*
import kotlinx.android.synthetic.main.layout_page_action.*
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.layout_search_view.edit_search_bar
import kotlinx.android.synthetic.main.layout_search_view.tv_search
import kotlinx.android.synthetic.main.layout_search_view.view.*
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

    override fun onInvisible() {

    }

    override fun initData() {

    }


    var boxFlag = 1
    private lateinit var arrayId: Array<Int?>
    var mapId: MutableMap<Int, Int> = mutableMapOf()
    val adapter = BoxRecordAdapter()
    private fun initView() {
        refreshData()

        setTextTitle("ID","小箱条码","箱内产品条码","产品数量","归属大箱条码","发货时间")
        homeMgRecycler.layoutManager = LinearLayoutManager(activity)
        homeMgRecycler.adapter = adapter
        setOnClickListener(tvSmallBox, tvBigBox,tv_search,llAction1,llAction3,llAction2) {
            arrayId = arrayOfNulls(mapId.size)
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
                llAction1->refreshData()
                llAction2->edit_search_bar.text.clear()
                llAction3 -> {
                    val lists = mutableListOf<Int>()
                    if (mapId.isNotEmpty()) {
                        for (m in mapId) {
                            lists.add(m.value)
                        }
                        for (i in 0 until lists.size){
                            arrayId[i] = lists[i]
                        }
                        exitDialog.setClick("确认删除","确定删除选中的内容吗",object : ExitDialog.HomeNormalClick{
                            override fun dialogClick() {
                                viewModel.deleteBoxRecord(arrayId,boxFlag)
                            }
                        })
                    } else "请先选择要删除的内容".toast()
                }
            }
        }


        adapter.addChildClickViewIds(R.id.ivCheckTitle,R.id.tvLabel3)
        adapter.setOnItemChildClickListener { adapters, view, position ->
            when (view.id) {
                R.id.ivCheckTitle -> {
                    val data = adapter.data[position]
                    if (!data.isCheck) {
                        data.isCheck = true
                        mapId[position] = adapter.data[position].id
                    } else if (data.isCheck) {
                        data.isCheck = false
                        mapId.remove(position)
                    }
                    adapter.notifyItemChanged(position)
                }
                R.id.tvLabel3->{
                    val data = adapter.data[position].product
                    normalStringDialog.setDatas(data)
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
        viewModel.boxRecordLiveData0.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            if (rp.code == 200){
                totalPage = rp.data.last_page
                Log.e("获取lastpage",totalPage.toString())
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
                    layoutEmptyView
                        .visibility = View.VISIBLE
                }
                upPage(rp.data.total,rp.data.last_page)
            }
        })
        viewModel.deleteRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            refreshData()
            mapId.clear()
        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
        initRefresh()
    }

    private fun getBoxList(product: String, time: String, flag: Int, page: Int) {
        viewModel.getBoxRecord0(product,time,flag,page)
    }

    companion object {
        fun newInstance() = BoxListFragment()
    }

    private var currentPage = 1
    private var totalPage = 1

    private fun refreshData(){
        getBoxList(edit_search_bar.text.toString(),"",boxFlag,currentPage)
    }

    fun next(): Int {
        currentPage += 1
        return currentPage
    }

    private fun initRefresh(){
        smartRefresh.setOnLoadMoreListener {
            smartRefresh.finishLoadMore(2000)
            if (currentPage < totalPage) {
                next()
                refreshData()
            } else {
                smartRefresh.finishLoadMore(true)
                "没有更多数据了".toast()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun upPage(total: Int, lastPage: Int) {
        smartRefresh.isEnabled = totalPage >= 2
        tvRecordSize.text = "第$currentPage/${lastPage}页,共${total}条数据"
    }

    private val normalStringDialog by lazy {
        NormalStringDialog(requireContext())
    }
}