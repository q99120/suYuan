package cn.work.suyuan.ui.send

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.work.suyuan.Const
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseViewPagerFragment2
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.fragment_three_container.*
import kotlinx.android.synthetic.main.layout_other_page_title_bar.*

class SendManageFragment: BaseViewPagerFragment2() , OnTabSelectListener {
    override val pageTitles: Array<String> = arrayOf("单个发货","整箱发货","批量发货","发货记录","取消整箱发货","取消单个发货")

    override val createFragments: Array<Fragment> =
        arrayOf(SingleSendFragment.newInstance(),PackSendFragment.newInstance(),BatchSendFragment.newInstance()
            , SendRecordFragment.newInstance(),CancelPackFragment.newInstance(),CancelSingleFragment.newInstance())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_three_container, container, false))
    }

    override fun onInvisible() {

    }

    override fun initData() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pagerAdapter = MyPagerAdapter(getActivity()!!.supportFragmentManager).apply { addFragments(createFragments) }
        viewPager3.adapter = pagerAdapter
        slidTabLayout.setViewPager(viewPager3, pageTitles)
        slidTabLayout.setOnTabSelectListener(this)
        viewPager3.offscreenPageLimit = 0
        viewPager3?.currentItem = 0


        viewPager3.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                Log.e("onTabSelect",position.toString())
                Const.sendViewFlag = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

    }




    override fun onTabSelect(position: Int) {

    }

    override fun onTabReselect(position: Int) {
    }


    companion object {
        fun newInstance() = SendManageFragment()
    }
}