package cn.work.suyuan.ui.packmanage

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
import cn.work.suyuan.ui.adapter.BoxListAdapter
import cn.work.suyuan.ui.adapter.BoxRecordAdapter
import cn.work.suyuan.ui.dialog.EditPackDialog
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.InjectorUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layout_choose_date.*
import kotlinx.android.synthetic.main.layout_pack_record_recly_title.*
import kotlinx.android.synthetic.main.layout_page_action.*
import kotlinx.android.synthetic.main.layout_search_view.*

/**
 * 装箱记录
 */
class BoxRecordFragment : BaseFragment() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
    }
    val boxRecordAdapter = BoxRecordAdapter()

    var selectPosition = -1
    var mapId: MutableMap<Int, Int> = mutableMapOf()
    private lateinit var arrayId: Array<Int?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_pack_record, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edit_search_bar.hint = "请输入条码"
        tvChooseDateLeft.text = "选择日期"
        llChooseDateRight.visibility = View.INVISIBLE


        iv_action2.setImageResource(R.mipmap.action_edit)
        tv_action2.text = "编辑数据"

        initViews()
        observer()
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        viewModel.getBoxRecord("", "2020-10-09", 0, 1)
    }


    private fun initViews() {
        recyclerPackRecord.layoutManager = LinearLayoutManager(activity)
        recyclerPackRecord.adapter = boxRecordAdapter
        boxRecordAdapter.addChildClickViewIds(R.id.ivCheckOut)
        boxRecordAdapter.setOnItemChildClickListener { adapters, view, position ->
            when (view.id) {
                R.id.ivCheckOut -> {
                    val data = boxRecordAdapter.data[position]
                    if (!data.isCheck) {
                        data.isCheck = true
                        mapId[position] = boxRecordAdapter.data[position].id
                    } else if (data.isCheck) {
                        data.isCheck = false
                        mapId.remove(position)
                    }
                    if (mapId.size == 1) selectPosition = position
                    boxRecordAdapter.notifyItemChanged(position)
                }
            }
        }


        val array: BooleanArray = booleanArrayOf(true, true, true, false, false, false)
        pvTime = TimePickerBuilder(activity)
        { date, v ->
            tvChooseDateLeft.text = DateUtil.getDate(date.time)
        }.isCenterLabel(true).setType(array).build()
        setOnClickListener(llChooseDateLeft, llAction1, llAction2, llAction3) {
            arrayId = arrayOfNulls(mapId.size)
            when (this) {
                llAction2 -> {
                    if (mapId.isNotEmpty()) {
                        if (mapId.size > 1) {
                            "满员了不能编辑了".toast()
                        } else {
                            dialogAction(3, boxRecordAdapter.data[selectPosition].id,
                                boxRecordAdapter.data[selectPosition].product,
                                boxRecordAdapter.data[selectPosition].carton)
                        }
                    } else "请先勾选".toast()
                }
                llAction3 -> {
                    val lists = mutableListOf<Int>()
                    if (mapId.isNotEmpty()) {
                        for (m in mapId) {
                            lists.add(m.value)
                        }
                        for (l in 0 until lists.size) {
                            arrayId[l] = lists[l]
                        }
                        viewModel.deleteBoxRecord(arrayId)
                    } else "请先勾选".toast()
                }
                llChooseDateLeft -> {
                    pvTime.show()
                }
            }
        }
    }

    private fun observer() {
        viewModel.boxRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                if (rp.data.data.isNotEmpty()) {
                    boxRecordAdapter.setList(rp.data.data)
//                    traceAdapter.setfmStatus(3)
                } else { rp.msg.toast() } } })
        viewModel.deleteRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
        })
        viewModel.editRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
        })
    }

    private fun dialogAction(index: Int, id: Int, product: String, carton: String) {
        editPackDialog.actionBoxRecord(index, id, product, carton,object : EditPackDialog.HomeNormalClick {
                override fun dialogClick(processName: String, sort: Int) {
                       viewModel.editBoxRecord(id,"1234",carton)
                }
            })
    }


    companion object {
        fun newInstance() = BoxRecordFragment()
    }

    lateinit var pvTime: TimePickerView
}