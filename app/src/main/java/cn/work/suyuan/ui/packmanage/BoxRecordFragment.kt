package cn.work.suyuan.ui.packmanage

import android.annotation.SuppressLint
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
import cn.work.suyuan.ui.adapter.BoxRecordAdapter
import cn.work.suyuan.ui.dialog.ExitDialog
import cn.work.suyuan.ui.dialog.NormalStringDialog
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.DateUtil
import cn.work.suyuan.util.InjectorUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import kotlinx.android.synthetic.main.fragment_pack_record.*
import kotlinx.android.synthetic.main.layout_choose_date.*
import kotlinx.android.synthetic.main.layout_page_action.*
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.smart_refresh_recly.*

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
        tvChooseDateLeft.hint = "选择日期"
        llChooseDateRight.visibility = View.INVISIBLE


        iv_action2.setImageResource(R.mipmap.action_edit)
        tv_action2.text = "编辑数据"

        initViews()
        observer()
    }

    override fun onInvisible() {

    }

    override fun initData() {

    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        refreshPack()
    }

    private fun refreshPack() {
        viewModel.getBoxRecord0(edit_search_bar.text.toString(), tvChooseDateLeft.text.toString(), 0, currentPage)
    }


    private fun initViews() {
        smartRefresh.setOnLoadMoreListener {
            smartRefresh.finishLoadMore(2000)
            if (currentPage < totalPage) {
                next()
                refreshPack()
            } else {
                smartRefresh.finishLoadMore(true)
                "没有更多数据了".toast()
            }
        }


        homeMgRecycler.layoutManager = LinearLayoutManager(activity)
        homeMgRecycler.adapter = boxRecordAdapter
        boxRecordAdapter.addChildClickViewIds(R.id.ivCheckTitle,R.id.tvLabel3)
        boxRecordAdapter.setOnItemChildClickListener { adapters, view, position ->
            when (view.id) {
                R.id.ivCheckTitle -> {
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
                R.id.tvLabel3->{
                    val data = boxRecordAdapter.data[position].product
                    normalStringDialog.setDatas(data)
                }
            }
        }


        val array: BooleanArray = booleanArrayOf(true, true, true, false, false, false)
        pvTime = TimePickerBuilder(activity)
        { date, v ->
            tvChooseDateLeft.text = DateUtil.getDate(date.time)
        }.isCenterLabel(true).setType(array).build()
        setOnClickListener(llChooseDateLeft, llAction1, llAction2, llAction3,tv_search) {
            arrayId = arrayOfNulls(mapId.size)
            when (this) {
                llAction1->{
                    currentPage = 1
                    refreshPack()
                }
                llAction2 -> {
                    if (mapId.isNotEmpty()) {
                        if (mapId.size > 1) {
                            "满员了不能编辑了".toast()
                        } else {
                            dialogAction(3, boxRecordAdapter.data[selectPosition].id,
                                boxRecordAdapter.data[selectPosition].product[0],
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
                        for (i in 0 until lists.size){
                            arrayId[i] = lists[i]
                        }
                        exitDialog.setClick("确认删除","确定删除选中的内容吗",object : ExitDialog.HomeNormalClick{
                            override fun dialogClick() {
                                viewModel.deleteBoxRecord(arrayId,0)
                            }
                        })
                    } else "请先选择要删除的内容".toast()
                }
                llChooseDateLeft -> {
                    pvTime.show()
                }
                    tv_search->refreshPack()
            }
        }
    }

    private fun observer() {
        viewModel.boxRecordLiveData0.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                totalPage = rp.data.last_page
                if (rp.data.data.isNotEmpty()) {
                    layoutReclyTitle.visibility = View.VISIBLE
                    layoutEmptyView.visibility = View.INVISIBLE
                    if (currentPage == 1){
                       boxRecordAdapter.setList(rp.data.data)
                    }else{
                        boxRecordAdapter.addData(rp.data.data)
                    }
                } else {  layoutReclyTitle.visibility = View.INVISIBLE
                    layoutEmptyView.visibility = View.VISIBLE }
                upPage()
            } })
        viewModel.deleteRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
            refreshPack()
            mapId.clear()
        })
        viewModel.editRecordLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun upPage() {
        smartRefresh.isEnabled = totalPage >= 2
        tvRecordSize.text = "第" + currentPage+ "/"+ totalPage+"页,共"+boxRecordAdapter.data.size+"条数据"

    }

    private fun dialogAction(index: Int, id: Int, product: String, carton: String) {
        editPackDialog.actionBoxRecord(index, id, product, carton)
    }


    companion object {
        fun newInstance() = BoxRecordFragment()
    }

    lateinit var pvTime: TimePickerView

    private var currentPage = 1
    private var totalPage = 1
    fun next(): Int {
        currentPage += 1
        return currentPage
    }

    private val normalStringDialog by lazy {
        NormalStringDialog(requireContext())
    }



}