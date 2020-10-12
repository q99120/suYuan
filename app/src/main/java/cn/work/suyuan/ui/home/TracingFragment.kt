package cn.work.suyuan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.util.InjectorUtil

/**
 * 流程追朔
 */
class TracingFragment  : BaseFragment(){

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_tracing, container, false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

    }


    companion object { fun newInstance() = TracingFragment()



    }

    override fun loadDataOnce() {
        super.loadDataOnce()
    }

}