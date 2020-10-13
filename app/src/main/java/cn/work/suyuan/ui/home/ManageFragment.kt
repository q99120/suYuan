package cn.work.suyuan.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.event.MessageEvent
import cn.work.suyuan.event.RefreshEvent
import cn.work.suyuan.ui.adapter.HomeNormalAdapter
import cn.work.suyuan.ui.dialog.HomeNormalDialog
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layout_page_action.*
import kotlinx.android.synthetic.main.layoutadtitle.*

/**
 * 流程管理
 */
class ManageFragment : BaseFragment() {

    private val homeAdapter: HomeNormalAdapter = HomeNormalAdapter()
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_child, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeMgRecycler.layoutManager = LinearLayoutManager(requireContext())
        homeMgRecycler.adapter = homeAdapter
        ll_date.visibility = View.GONE
        iv_action1.setImageResource(R.mipmap.action_add)
        iv_action2.setImageResource(R.mipmap.action_edit)
        iv_action3.setImageResource(R.mipmap.action_delete)
        tvTitle4.visibility = GONE
        tvTitle5.visibility = GONE
        tvTitle6.visibility = GONE
        tv_action1.text = "添加数据"
        tv_action2.text = "编辑数据"
        tv_action3.text = "删除数据"
        initViews()
        observe()
    }

    var selectPosition = -1
    var mapId: MutableMap<Int, Int> = mutableMapOf()
    private lateinit var arrayId: Array<Int?>
    private fun initViews() {
        homeAdapter.addChildClickViewIds(R.id.ivCheckOut)
        homeAdapter.setOnItemChildClickListener { adapters, view, position ->
            when (view.id) {
                R.id.ivCheckOut -> {
                    val data = homeAdapter.data[position]
                    if (!data.isCheck) {
                        data.isCheck = true
                        mapId[position] = homeAdapter.data[position].id
                    } else if (data.isCheck) {
                        data.isCheck = false
                        mapId.remove(position)
                    }
                    if (mapId.size == 1) selectPosition = position
                    homeAdapter.notifyItemChanged(position)
                }
            }
        }

        setOnClickListener(llAction1, llAction2, llAction3) {
            arrayId = arrayOfNulls(mapId.size)
            when (this) {
                llAction1 -> {
                    dialogAction(1, "", 1)
                }
                llAction2 -> {
                    if (mapId.isNotEmpty()) {
                        if (mapId.size > 1) {
                            "满员了不能编辑了".toast()
                        } else {
                            dialogAction(
                                2,
                                homeAdapter.data[selectPosition].tab,
                                homeAdapter.data[selectPosition].sort
                            )
                        }
                    } else "请先勾选".toast()
                }
                llAction3 -> {
                    val lists = mutableListOf<Int>()
                    if (mapId.isNotEmpty()) {
                        for (m in mapId) {
                            lists.add(m.value)
                        }
                        for (l in 0 until lists.size){
                            arrayId[l] = lists[l]
                        }
                        viewModel.deleteProcess(arrayId)
                    } else "请先勾选".toast()
                }
            }
        }
    }

    private fun dialogAction(index: Int, name: String, sort: Int) {
        homeNormalDialog.action(index, name, sort, object : HomeNormalDialog.HomeNormalClick {
            override fun dialogClick(processName: String, sort: Int) {
                if (index == 1) viewModel.addProcess(processName, sort)
                else if (index == 2) viewModel.editProcess(
                    homeAdapter.data[selectPosition].id,
                    processName,
                    sort
                )
            }

        })
    }

    private fun observe() {
        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            val response = rp.data
            if (response.isNotEmpty()) homeAdapter.setList(response)
            Log.e("获取发挥的结果", it.toString())
        })

        viewModel.addProcessLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                "添加成功,添加后ID为" + rp.data
                viewModel.onRefresh()
            }
        })

        viewModel.editProcessLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            mapId.clear()
            viewModel.onRefresh()
        })

        //删除流程
        viewModel.deleteProcessLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            homeAdapter.setCheckVis(false)
            rp.msg.toast()
            mapId.clear()
            viewModel.onRefresh()
        })
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        //第一次进入页面,刷新数据
        viewModel.onRefresh()
    }


    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && javaClass == messageEvent.activityClass) {
//            refreshLayout.autoRefresh()
//            if (recyclerView.adapter?.itemCount ?: 0 > 0) recyclerView.scrollToPosition(0)
        }
    }

    companion object {
        fun newInstance() = ManageFragment()
    }

}