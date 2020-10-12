package cn.work.suyuan.ui.mine

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.common.ui.BaseViewPagerFragment
import cn.work.suyuan.event.MessageEvent
import cn.work.suyuan.event.RefreshEvent
import cn.work.suyuan.event.SwitchPagesEvent
import cn.work.suyuan.logic.model.TabEntity
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.ui.home.ManageFragment
import cn.work.suyuan.ui.home.TraceabilityFragment
import cn.work.suyuan.ui.home.TracingFragment
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.widget.GlideEngine
import com.flyco.tablayout.listener.CustomTabEntity
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.EventBus

class MineFragment: BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_mine, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getUser()
        observer()
    }

    private fun observer() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
              val rp = it.getOrNull()?:return@Observer
               val userData = rp.data
              tvNickName.text = userData.nickname
            tvDecs.text = userData.descs
            tvAge.text = userData.age.toString()
            tvSex.text = userData.sex.toString()
            GlideEngine.getInstance().loadPhoto(requireContext(),
                Uri.parse(userData.cover),ivMineHead)
        })
    }


    companion object {
        fun newInstance() = MineFragment()
    }
}