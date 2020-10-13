package cn.work.suyuan.ui.packmanage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.adapter.TraceAdapter
import cn.work.suyuan.util.DateUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.layoutadtitle.*


class BoxRecordFragment : BaseFragment() {

    val traceAdapter = TraceAdapter()

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
        homeMgRecycler.adapter = traceAdapter
        edit_search_bar.hint = "请输入条码"
        tvChooseStartTime.text = "选择日期"
        ll_date.visibility = View.VISIBLE
        rlEndDate.visibility = View.GONE
        viewEndLine.visibility = View.GONE
        initViews()
        observer()
    }

    private fun observer() {

    }

    private fun initViews() {
        val array:BooleanArray = booleanArrayOf(true,true,true,false,false,false)
        pvTime = TimePickerBuilder(activity)
        { date, v ->
            tvChooseStartTime.text = DateUtil.getDate(date.time)
        }.isCenterLabel(true).setType(array).build()
        setOnClickListener(rlStartTime) {
            when (this) {
                rlStartTime -> {
                    pvTime.show()
                }
            }
        }
    }


    companion object {
        fun newInstance() = BoxRecordFragment()
    }

    lateinit var pvTime: TimePickerView
}