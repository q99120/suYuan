package cn.work.suyuan.ui.send

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
import cn.work.suyuan.ui.adapter.TraceAdapter
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layout_choose_date.*
import kotlinx.android.synthetic.main.layout_page_action.*
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.layoutadtitle.*
import kotlinx.android.synthetic.main.smart_refresh_recly.*

/**
 * 发货记录
 */
class SendRecordFragment : BaseFragment() {

    val traceAdapter = TraceAdapter()
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
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_child, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edit_search_bar.hint = "请输入条码"
        homeMgRecycler.layoutManager = LinearLayoutManager(requireContext())
        homeMgRecycler.adapter = traceAdapter
        traceAdapter.setfmStatus(2)
        tvTitle1.text = "操作员"
        tvTitle2.text = "条码"
        tvTitle3.text = "产品名称"
        tvTitle4.text = "经销商"
        tvTitle5.text = "日期"
        tvTitle6.text = "IP"
        layout_choose_date.visibility = View.VISIBLE
        initView()
        observer()
    }

    var selectPosition = -1
    var mapId: MutableMap<Int, Int> = mutableMapOf()
    private lateinit var arrayId: Array<Int?>
    var startTime = ""
    var endTime = ""
    var productCode = ""
    private fun initView() {
        traceAdapter.addChildClickViewIds(R.id.ivCheckOut)
        traceAdapter.setOnItemChildClickListener { adapters, view, position ->
            when (view.id) {
                R.id.ivCheckOut -> {
                    val data = traceAdapter.data[position]
                    if (!data.isCheck) {
                        data.isCheck = true
                        mapId[position] = traceAdapter.data[position].id
                    } else if (data.isCheck) {
                        data.isCheck = false
                        mapId.remove(position)
                    }
                    if (mapId.size == 1) selectPosition = position
                    traceAdapter.notifyItemChanged(position)
                }
            }
        }

        setOnClickListener(
            llAction1,
            llAction2,
            llAction3,
            llChooseDateLeft,
            llChooseDateRight,
            tv_search
        ) {
            arrayId = arrayOfNulls(mapId.size)
            when (this) {
                llAction3 -> {
                    val lists = mutableListOf<Int>()
                    if (mapId.isNotEmpty()) {
                        for (m in mapId) {
                            lists.add(m.value)
                        }
                        for (l in 0 until lists.size) {
                            arrayId[l] = lists[l]
                        }
                        viewModel.deleteSendRecord(arrayId)
                    } else "请先勾选".toast()
                }
                llChooseDateLeft -> {
                    DateUtil.showDate(activity, true, object : DateUtil.ChooseDate {
                        override fun getTime(result: String) {
                            startTime = result
                            tvChooseDateLeft.text = result
                        }

                    })
                }
                llChooseDateRight -> {
                    DateUtil.showDate(activity, true, object : DateUtil.ChooseDate {
                        override fun getTime(result: String) {
                            endTime = result
                            tvChooseDateRight.text = result
                        }

                    })
                }
                tv_search -> refreshRecord(startTime, endTime, productCode, 1)

            }
        }
    }

    private fun observer() {
        viewModel.sendRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            val data = rp.data.data
            totalPage = rp.data.total
            if (data.isNotEmpty()) {
                layoutVisBle(true)
                if (currentPage == 1) {
                    traceAdapter.setList(data)
                } else {
                    traceAdapter.addData(data)
                }
            } else layoutVisBle(false)

        })
        viewModel.deleteSendRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            refreshRecord(startTime, endTime, productCode, 1)
        })
    }

    private fun layoutVisBle(b: Boolean) {
        if (b) {
            layoutAdTitle.visibility = View.VISIBLE
            layoutEmptyView.visibility = View.INVISIBLE
        } else {
            layoutAdTitle.visibility = View.INVISIBLE
            layoutEmptyView.visibility = View.VISIBLE
        }

    }

    private fun refreshRecord(startTime: String, endTime: String, productCode: String, page: Int) {
        viewModel.sendRecord(startTime, endTime, productCode, page)
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        refreshRecord(startTime, endTime, productCode, 1)
    }

    companion object {
        fun newInstance() = SendRecordFragment()
    }

    private var currentPage = 1
    private var totalPage = 1
    fun next(): Int {
        currentPage += 1
        return currentPage
    }

}