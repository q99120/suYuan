package cn.work.suyuan.ui.packmanage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.ui.send.SendPackViewModel
import cn.work.suyuan.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_home_child.*
import kotlinx.android.synthetic.main.fragment_pack_list.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*

/**
 * 外箱列表(包含大箱和小箱子）
 */
class BoxListFragment : BaseFragment() {
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
        return super.onCreateView(inflater.inflate(R.layout.fragment_pack_list, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        setOnClickListener(tvSmallBox, tvBigBox) {
            when (this) {
                tvSmallBox -> {
                    tvSmallBox.setBackgroundResource(R.mipmap.packlistleft)
                    tvBigBox.setBackgroundResource(R.color.transparent)
                }
                tvBigBox -> {
                    tvBigBox.setBackgroundResource(R.mipmap.packlistright)
                    tvSmallBox.setBackgroundResource(R.color.transparent)
                }
            }
        }
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
    }

    companion object {
        fun newInstance() = BoxListFragment()
    }
}