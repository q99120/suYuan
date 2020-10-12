/*
 * Copyright (c) 2020. vipyinzhiwei <vipyinzhiwei@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.work.suyuan.common.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.work.suyuan.R
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener

/**
 * Fragment基类，适用场景：页面含有ViewPager+TabLayout的界面。
 *
 * @author vipyinzhiwei
 * @since  2020/5/1
 */
abstract class BaseViewPagerFragment : BaseFragment() {


    protected var viewPager2: ViewPager2? = null

    protected var tabLayout: CommonTabLayout? = null

    protected var pageChangeCallback: PageChangeCallback? = null

    protected val adapter: VpAdapter by lazy { VpAdapter(getActivity()!!).apply { addFragments(createFragments) } }


        protected var offscreenPageLimit = 1

        abstract val createTitles: ArrayList<CustomTabEntity>



        abstract val createFragments: Array<Fragment>

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setupViews()
        }

        override fun onDestroy() {
            super.onDestroy()
            pageChangeCallback?.run { viewPager2?.unregisterOnPageChangeCallback(this) }
            pageChangeCallback = null
        }

        open fun setupViews() {
            initViewPager()
//        setOnClickListener(ivCalendar, ivSearch) {
//            if (this == ivCalendar) {
//                R.string.currently_not_supported.showToast()
//            } else if (this == ivSearch) {
//                SearchFragment.switchFragment(activity)
//            }
//        }
        }

        protected fun initViewPager() {
            viewPager2 = rootView?.findViewById(R.id.viewPager2)
            tabLayout = rootView?.findViewById(R.id.tabLayouts)

            viewPager2?.offscreenPageLimit = offscreenPageLimit
            viewPager2?.adapter = adapter
            tabLayout?.setTabData(createTitles)
            tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {

                override fun onTabSelect(position: Int) {
                    viewPager2?.currentItem = position
                }

                override fun onTabReselect(position: Int) {

                }
            })
            pageChangeCallback = PageChangeCallback()
            viewPager2?.registerOnPageChangeCallback(pageChangeCallback!!)


        }

        inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout?.currentTab = position
            }
        }

        inner class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(
            fragmentActivity
        ) {

            private val fragments = mutableListOf<Fragment>()

            fun addFragments(fragment: Array<Fragment>) {
                fragments.addAll(fragment)
            }

            override fun getItemCount() = fragments.size

            override fun createFragment(position: Int) = fragments[position]
        }


    }
