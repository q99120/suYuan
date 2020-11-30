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
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import kotlinx.android.synthetic.main.layout_zhijian.*
import kotlinx.android.synthetic.main.layout_zhijian.ivBack

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
                   "点击了修改".toast()
                }
                1 -> {
                    "点击了删除".toast()
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
        viewModel.getQutalityList()
        viewModel.qutalityListLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            if (rp.code == 200) {
                Log.e("获取质检报告", rp.data.toString())
                spinnerAdapter.setList(rp.data)
//                        "获取质检报告成功".toast()
//                        zhijianID = id.toString()
//                        checkQuality.text = reportName
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initView() {

        ivBack.setOnClickListener {
            finish()
        }
        ivQutality.setOnClickListener {
            AddQualityActivity.start(this)
        }
        smartRefresh.setOnRefreshListener{
            viewModel.getQutalityList()
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
            deleteItem.setImage(R.mipmap.action_delete)
            val updateItem = SwipeMenuItem(this@QualityActivity)
            updateItem.height = 80
            updateItem.setImage(R.mipmap.action_reset)
            rightMenu.addMenuItem(updateItem)
            rightMenu.addMenuItem(deleteItem)
        }

}