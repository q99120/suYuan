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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.logD
import cn.work.suyuan.event.MessageEvent
import cn.work.suyuan.util.ActivityCollector
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * 应用程序中所有Activity的基类。
 *
 * @author vipyinzhiwei
 * @since  2020/4/29
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    /**
     * 判断当前Activity是否在前台。
     */
    protected var isActive: Boolean = false

    /**
     * 当前Activity的实例。
     */
    protected var activity: Activity? = null

    /** 当前Activity的弱引用，防止内存泄露  */
    private var activityWR: WeakReference<Activity>? = null

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(TAG, "BaseActivity-->onCreate()")

        activity = this
        activityWR = WeakReference(activity!!)
        ActivityCollector.pushTask(activityWR)

        EventBus.getDefault().register(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(TAG, "BaseActivity-->onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD(TAG, "BaseActivity-->onRestoreInstanceState()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logD(TAG, "BaseActivity-->onNewIntent()")
    }

    override fun onRestart() {
        super.onRestart()
        logD(TAG, "BaseActivity-->onRestart()")
    }

    override fun onStart() {
        super.onStart()
        logD(TAG, "BaseActivity-->onStart()")
    }

    override fun onResume() {
        super.onResume()
        logD(TAG, "BaseActivity-->onResume()")
        isActive = true
    }

    override fun onPause() {
        super.onPause()
        logD(TAG, "BaseActivity-->onPause()")
        isActive = false
    }

    override fun onStop() {
        super.onStop()
        logD(TAG, "BaseActivity-->onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD(TAG, "BaseActivity-->onDestroy()")

        activity = null
        ActivityCollector.removeTask(activityWR)
        EventBus.getDefault().unregister(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setStatusBarBackground(R.color.statusbar)
        setupViews()
    }

    override fun setContentView(layoutView: View) {
        super.setContentView(layoutView)
        setStatusBarBackground(R.color.statusbar)
        setupViews()
    }

    protected open fun setupViews() {
//        val navigateBefore = findViewById<ImageView>(R.id.ivNavigateBefore)
//        val tvTitle = findViewById<TextView>(R.id.tvTitle)
//        navigateBefore?.setOnClickListener { finish() }
//        tvTitle?.isSelected = true  //获取焦点，实现跑马灯效果。

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {

    }

    /**
     * 设置状态栏背景色
     */
    open fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f).statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }
}
