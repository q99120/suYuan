package cn.work.suyuan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseViewPagerFragment
import cn.work.suyuan.event.MessageEvent
import cn.work.suyuan.event.RefreshEvent
import cn.work.suyuan.event.SwitchPagesEvent
import cn.work.suyuan.logic.model.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import org.greenrobot.eventbus.EventBus

class HomePageFragment : BaseViewPagerFragment(){


    override val createTitles: ArrayList<CustomTabEntity> = ArrayList<CustomTabEntity>().apply {
        add(TabEntity("流程管理"))
        add(TabEntity("流程追溯"))
        add(TabEntity("溯源信息"))
    }

    override val createFragments: Array<Fragment> = arrayOf(ManageFragment.newInstance(), TracingFragment.newInstance(), TraceabilityFragment.newInstance())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_main_container, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager2?.currentItem = 0
    }



    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && this::class.java == messageEvent.activityClass) {
            when (viewPager2?.currentItem) {
                0 -> EventBus.getDefault().post(RefreshEvent(ManageFragment::class.java))
                1 -> EventBus.getDefault().post(RefreshEvent(TracingFragment::class.java))
                2 -> EventBus.getDefault().post(RefreshEvent(TraceabilityFragment::class.java))
                else -> {
                }
            }
        } else if (messageEvent is SwitchPagesEvent) {
            when (messageEvent.activityClass) {
                ManageFragment::class.java -> viewPager2?.currentItem = 0
                TracingFragment::class.java -> viewPager2?.currentItem = 1
                TraceabilityFragment::class.java -> viewPager2?.currentItem = 2
                else -> {
                }
            }
        }
    }



    companion object { fun newInstance() = HomePageFragment() }

}
