package cn.work.suyuan.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.work.suyuan.Const.traceFlag
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.singleClick
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.event.RefreshEvent
import cn.work.suyuan.ui.MainActivity
import cn.work.suyuan.ui.QualityActivity
import cn.work.suyuan.ui.mine.SettingActivity
import kotlinx.android.synthetic.main.fragment_home_page.*
import org.greenrobot.eventbus.EventBus

class HomePageFragment : BaseFragment(){
    private var traceManageFragment: TraceManageFragment? = null


//    override val createTitles: ArrayList<CustomTabEntity> = ArrayList<CustomTabEntity>().apply {
//        add(TabEntity("流程管理"))
//        add(TabEntity("流程追溯"))
//        add(TabEntity("溯源信息"))
//    }
//
//    override val createFragments: Array<Fragment> = arrayOf(ManageFragment.newInstance(), TracingFragment.newInstance(), TraceabilityFragment.newInstance())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home_page, container, false))
    }

    override fun onInvisible() {
    }

    override fun initData() {
        llReport.singleClick { QualityActivity.start(requireContext()) }
        llActionTrace.singleClick {
            traceFlag = 0
            EventBus.getDefault()
                .post(RefreshEvent(TraceManageFragment::class.java))
            mainActivity.setTabSelection(3)
        }
//        llTraceInfo.singleClick {
//            traceFlag = 1
//            EventBus.getDefault()
//                .post(RefreshEvent(TraceManageFragment::class.java))
//            mainActivity.setTabSelection(3)
//        }
        llPackManage.singleClick {  mainActivity.setTabSelection(1) }
        llSetting.singleClick { SettingActivity.start(requireContext()) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewPager2?.currentItem = 0
    }

    lateinit var mainActivity:MainActivity
    override fun onAttach(context: Context) {
        mainActivity = getActivity() as MainActivity
        super.onAttach(context)
    }



//    override fun onMessageEvent(messageEvent: MessageEvent) {
//        super.onMessageEvent(messageEvent)
//        if (messageEvent is RefreshEvent && this::class.java == messageEvent.activityClass) {
//            when (viewPager2?.currentItem) {
//                0 -> EventBus.getDefault().post(RefreshEvent(ManageFragment::class.java))
//                1 -> EventBus.getDefault().post(RefreshEvent(TracingFragment::class.java))
//                2 -> EventBus.getDefault().post(RefreshEvent(TraceabilityFragment::class.java))
//                else -> {
//                }
//            }
//        } else if (messageEvent is SwitchPagesEvent) {
//            when (messageEvent.activityClass) {
//                ManageFragment::class.java -> viewPager2?.currentItem = 0
//                TracingFragment::class.java -> viewPager2?.currentItem = 1
//                TraceabilityFragment::class.java -> viewPager2?.currentItem = 2
//                else -> {
//                }
//            }
//        }
//    }



    companion object { fun newInstance() = HomePageFragment() }

}
