package cn.work.suyuan.ui.home

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
import cn.work.suyuan.ui.adapter.HomeNormalAdapter
import cn.work.suyuan.ui.adapter.TraceAdapter
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.InjectorUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layout_choose_date.*
import kotlinx.android.synthetic.main.layout_page_action.*
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.layoutadtitle.*

/**
 * 朔源信息
 */
class TraceabilityFragment : BaseFragment() {

    lateinit var pickerView1: TimePickerView
    lateinit var pickerView2: TimePickerView
    val pickArray:BooleanArray = booleanArrayOf(true,true,true,true,true,false)
    private val traceAdapter = TraceAdapter()
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    val homeNormalAdapter = HomeNormalAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_child, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        homeMgRecycler.layoutManager = LinearLayoutManager(requireContext())
        homeMgRecycler.adapter = traceAdapter
        tvTitle6.visibility = View.GONE
        tvTitle1.text = "ID"
        tvTitle2.text = "流程名称"
        tvTitle3.text = "产品条码"
        tvTitle4.text = "操作人"
        tvTitle5.text = "生产时间"

        layout_choose_date.visibility = View.VISIBLE
       observer()
    }

    private var startTime = ""
    private var endTime = ""
    private val productName = ""
    private var page = 1
    private var mapId: MutableMap<Int, Int> = mutableMapOf()
    private lateinit var arrayId: Array<Int?>
    private fun initViews() {
        pickerView1 = TimePickerBuilder(activity)
        { date, v ->
            startTime =  DateUtil.getDateAndTime(date.time)
            tvChooseDateLeft.text = DateUtil.getDateAndTime(date.time)
        }.isCenterLabel(true).setType(pickArray).build()
        pickerView2 = TimePickerBuilder(activity)
        { date, v ->
            endTime =  DateUtil.getDateAndTime(date.time)
            tvChooseDateRight.text = DateUtil.getDateAndTime(date.time)
        }.isCenterLabel(true).setType(pickArray).build()

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
                    traceAdapter.notifyItemChanged(position)
                }
            }
        }


        setOnClickListener(llAction1, llAction2, llAction3,llChooseDateLeft,llChooseDateRight,tv_search) {
            arrayId = arrayOfNulls(mapId.size)
            when (this) {
                llAction1 -> { traceRefresh()}
                llAction2 -> { }
                llAction3 -> {
                    val lists = mutableListOf<Int>()
                    if (mapId.isNotEmpty()) {
                        for (m in mapId) { lists.add(m.value) }
                        for (l in 0 until lists.size){ arrayId[l] = lists[l] }
                        viewModel.deleteTrace(arrayId)
                    } else "请先勾选".toast()
                }
                llChooseDateLeft->{ pickerView1.show() }
                llChooseDateRight->{pickerView2.show()}
                tv_search->{
                    page = 1
                    viewModel.getTrace(startTime,endTime,edit_search_bar.text.toString(),page)
                }
            }
        }
    }

    private fun traceRefresh() {
        viewModel.getTrace(startTime,endTime,"",page)
    }

    private fun observer() {
        viewModel.traceLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            val response = rp.data.data
            if (response.isNotEmpty()) {
                traceAdapter.setList(response)
            }else{
//                homeMgRecycler.visibility = View.INVISIBLE
            }
        })

        //删除溯源信息
        viewModel.deleteProcessLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            if (rp.code == 200) {
                rp.msg.toast()
                traceAdapter.setCheckVis(false)
                mapId.clear()
                traceRefresh()
            }
        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
        //第一次进入页面,刷新数据
        traceRefresh()
    }

    companion object { fun newInstance() = TraceabilityFragment()
    }



}