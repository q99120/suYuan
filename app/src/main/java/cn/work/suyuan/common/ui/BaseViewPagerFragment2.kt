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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Fragment基类，适用场景：页面含有ViewPager+TabLayout的界面。
 *
 * @author vipyinzhiwei
 * @since  2020/5/1
 */
abstract class BaseViewPagerFragment2 : BaseFragment() {





        protected var offscreenPageLimit = 1


        abstract val pageTitles:Array<String>


        abstract val createFragments: Array<Fragment>

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setupViews()
        }

        override fun onDestroy() {
            super.onDestroy()
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

        }





    /**
         * 带滑动的tablayout
         */
        inner class MyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        private val fragments = ArrayList<Fragment>()


            fun addFragments(fragment: Array<Fragment>) {
                fragments.clear()
                fragments.addAll(fragment)
            }


            override fun getCount(): Int {
                return fragments.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return  pageTitles[position]
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }
        }
    }
