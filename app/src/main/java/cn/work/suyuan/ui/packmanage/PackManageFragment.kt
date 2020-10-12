package cn.work.suyuan.ui.packmanage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseViewPagerFragment2
import cn.work.suyuan.ui.home.ManageFragment
import cn.work.suyuan.ui.home.TracingFragment
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.fragment_other_container.*
import kotlinx.android.synthetic.main.layout_other_page_title_bar.*

class PackManageFragment: BaseViewPagerFragment2(),OnTabSelectListener{
    override val pageTitles: Array<String> = arrayOf("单个装箱","大箱装小箱","外箱列表","装箱记录")

    override val createFragments: Array<Fragment> = arrayOf(SinglePackFragment.newInstance(),BoxInboxFragment.newInstance(), BoxListFragment.newInstance(),BoxRecordFragment.newInstance())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_other_container, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pagerAdapter = MyPagerAdapter(getActivity()!!.supportFragmentManager).apply { addFragments(createFragments) }
        viewPager1.adapter = pagerAdapter
        slidTabLayout.setViewPager(viewPager1, pageTitles)
        viewPager1?.currentItem = 0
    }

    companion object { fun newInstance() = PackManageFragment() }

    override fun onTabSelect(position: Int) {
    }

    override fun onTabReselect(position: Int) {
    }


}