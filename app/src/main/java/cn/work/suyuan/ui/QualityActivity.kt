package cn.work.suyuan.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.ui.adapter.QuListAdapter
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.InjectorUtil
import com.yanzhenjie.recyclerview.*
import kotlinx.android.synthetic.main.layout_import_file.*
import kotlinx.android.synthetic.main.layout_qutality_list.*
import kotlinx.android.synthetic.main.layout_qutality_list.smartRefresh
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import kotlinx.android.synthetic.main.layout_zhijian.*
import kotlinx.android.synthetic.main.layout_zhijian.ivBack
import kotlinx.android.synthetic.main.smart_refresh_recly.*

class QualityActivity:BaseActivity() {
    private val spinnerAdapter = QuListAdapter()


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_qutality_list)
        recyclerQualityList.layoutManager = LinearLayoutManager(this)
        recyclerQualityList.setSwipeMenuCreator(mSwipeMenuCreator)
        recyclerQualityList.setOnItemMenuClickListener { menuBridge, adapterPosition ->
            menuBridge.closeMenu()
            // 左侧还是右侧菜单：
            val direction = menuBridge.direction
            // 菜单在Item中的Position：
            val menuPosition = menuBridge.position
            Log.e("菜单1",direction.toString())
            Log.e("菜单2",menuPosition.toString())
            Log.e("菜单3",adapterPosition.toString())
            when (menuPosition) {
                0 -> {
                   "暂时只是展示".toast()
                }
                1 -> {
                    "暂时只是展示".toast()
                }
            }
        }
        recyclerQualityList.adapter =spinnerAdapter
        initView()
        initObserver()

    }

    var zhijianID = ""
    var productFile = ""
    private fun initObserver() {
        Requfresh()
        viewModel.qutalityListLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            totalPage = rp.data.last_page
            if (rp.code == 200) {
                Log.e("获取质检报告", rp.data.toString())
                if (currentPage == 1){
                    spinnerAdapter.setList(rp.data.data)
                }else{
                    spinnerAdapter.addData(rp.data.data)
                }
//                        "获取质检报告成功".toast()
//                        zhijianID = id.toString()
//                        checkQuality.text = reportName
            }
        })
        smartRefresh.setOnLoadMoreListener {
            smartRefresh.finishLoadMore(1000)
            if (currentPage<totalPage){
                next()
              Requfresh()
            }else{
                "没有更多数据了".toast()
                smartRefresh.finishLoadMoreWithNoMoreData() //设置之后，将不会再触发加载事件
            }
        }
    }

    private fun Requfresh() {
        viewModel.getQutalityList(currentPage)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initView() {
        smartRefresh.setEnableFooterFollowWhenNoMoreData(true)

        ivBack.setOnClickListener {
            finish()
        }
        ivQutality.setOnClickListener {
            AddQualityActivity.start(this)
        }
        smartRefresh.setOnRefreshListener{
            currentPage = 1
            Requfresh()
            smartRefresh.finishRefresh(1500)
        }

            smartRefresh.setOnLoadMoreListener {
                smartRefresh.finishLoadMore(2000)
//                if (currentPage < totalPage) {
//                    next()
//                    refreshPack()
//                } else {
//                    smartRefresh.finishLoadMore(true)
//                    "没有更多数据了".toast()
//                }
            }

    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, QualityActivity::class.java))
        }
    }

    private val mSwipeMenuCreator =
        SwipeMenuCreator { leftMenu, rightMenu, position ->
            val deleteItem = SwipeMenuItem(this@QualityActivity)
            deleteItem.height = 80
            deleteItem.width = 120
            deleteItem.text = "删除"
            deleteItem.textSize = 20
            deleteItem.setTextColor(resources.getColor(R.color.white))
            deleteItem.setBackground( R.drawable.d_red)
            val updateItem = SwipeMenuItem(this@QualityActivity)
            updateItem.height = 80
            updateItem.width = 120
            updateItem.textSize = 20
            updateItem.setTextColor(resources.getColor(R.color.white))
            updateItem.text = "修改"
            updateItem.setBackground( R.drawable.d_blue)
            rightMenu.addMenuItem(updateItem)
            rightMenu.addMenuItem(deleteItem)
        }

    var currentPage = 1
    var totalPage = 1


    fun next():Int{
        currentPage+=1
        return currentPage
    }

}