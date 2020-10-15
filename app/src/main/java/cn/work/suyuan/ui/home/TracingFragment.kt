package cn.work.suyuan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.dialog.SingleSpinnerDialog
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_home_tracing.*
import kotlinx.android.synthetic.main.layout_tracing_fm.*

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

    var categoryId = 1
    private fun initViews() {
        setOnClickListener(btnConfirm,tvChooseCate){
            when(this){
                btnConfirm->{viewModel.setTracing()}
                tvChooseCate->{spinnerDialog.initSpinner(viewModel.getCate(),object :SingleSpinnerDialog.HomeNormalClick{
                    override fun dialogClick(processName: String, sort: Int) {
                        tvChooseCate.text = processName
                        categoryId = sort
                    }

                })}
            }
        }

    }


    companion object { fun newInstance() = TracingFragment()



    }

    override fun loadDataOnce() {
        super.loadDataOnce()
    }

    private val spinnerDialog by lazy {
        SingleSpinnerDialog(requireContext())
    }

}