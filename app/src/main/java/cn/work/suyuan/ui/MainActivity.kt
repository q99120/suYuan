package cn.work.suyuan.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraX
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.event.RefreshEvent
import cn.work.suyuan.ui.home.HomePageFragment
import cn.work.suyuan.ui.mine.MineFragment
import cn.work.suyuan.ui.packmanage.PackManageFragment
import cn.work.suyuan.ui.send.SendManageFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_bar.*
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity:BaseActivity() {
    private val fragmentManager: FragmentManager by lazy { supportFragmentManager }

    private var homePageFragment: HomePageFragment? = null
    private var packFragmentManager: PackManageFragment? = null
    private var sendManageFragment: SendManageFragment? = null
    private var mineFragment: MineFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun setupViews() {
        super.setupViews()
        setTabSelection(0)
        setOnClickListener(ll_home, ll_pack, ll_send, ll_mine){
            when(this){
                ll_home -> {
                    notificationUiRefresh(0)
                    setTabSelection(0)
                }
                ll_pack -> {
                    notificationUiRefresh(1)
                    setTabSelection(1)
                }
                ll_send -> {
                    notificationUiRefresh(2)
                    setTabSelection(2)
                }
                ll_mine -> {
                    notificationUiRefresh(3)
                    setTabSelection(3)
                }
            }
        }
    }

    private fun notificationUiRefresh(selectionIndex: Int) {
        when (selectionIndex) {
            0 -> {
                if (ivHomePage.isSelected) EventBus.getDefault()
                    .post(RefreshEvent(HomePageFragment::class.java))
            }
            1 -> {
                if (ivPack.isSelected) EventBus.getDefault()
                    .post(RefreshEvent(PackManageFragment::class.java))
            }
            2 -> {
                if (ivSend.isSelected) EventBus.getDefault()
                    .post(RefreshEvent(SendManageFragment::class.java))
            }
            3 -> {
                if (ivMine.isSelected) EventBus.getDefault()
                    .post(RefreshEvent(MineFragment::class.java))
            }
        }
    }

    private fun setTabSelection(index: Int) {

        /**
         *  when (index) {
        0 -> {
        ivHomePage.isSelected = true
        tvHomePage.isSelected = true
        if (homePageFragment == null) {
        homePageFragment = HomePageFragment.newInstance()
        add(R.id.homeActivityFragContainer, homePageFragment!!)
        } else {
        show(homePageFragment!!)
        }
        }
        1 -> {
        ivCommunity.isSelected = true
        tvCommunity.isSelected = true
        if (communityFragment == null) {
        communityFragment = CommunityFragment()
        add(R.id.homeActivityFragContainer, communityFragment!!)
        } else {
        show(communityFragment!!)
        }
        }
        2 -> {
        ivNotification.isSelected = true
        tvNotification.isSelected = true
        if (notificationFragment == null) {
        notificationFragment = NotificationFragment()
        add(R.id.homeActivityFragContainer, notificationFragment!!)
        } else {
        show(notificationFragment!!)
        }
        }
        3 -> {
        ivMine.isSelected = true
        tvMine.isSelected = true
        if (mineFragment == null) {
        mineFragment = MineFragment.newInstance()
        add(R.id.homeActivityFragContainer, mineFragment!!)
        } else {
        show(mineFragment!!)
        }
        }
        else -> {
        ivHomePage.isSelected = true
        tvHomePage.isSelected = true
        if (homePageFragment == null) {
        homePageFragment = HomePageFragment.newInstance()
        add(R.id.homeActivityFragContainer, homePageFragment!!)
        } else {
        show(homePageFragment!!)
        }
        }
        }
        }.commitAllowingStateLoss()
         */
        fragmentManager.beginTransaction().apply {
            clearAllSelected()
            hideFragments(this)
            when(index){
                0 -> {
                    rl_sel_title.visibility = View.VISIBLE
                    tv_sel_title.text = "首页"
                    ivHomePage.isSelected = true
                    if (homePageFragment == null) {
                        homePageFragment = HomePageFragment.newInstance()
                        add(R.id.homeContain, homePageFragment!!)
                    } else {
                        show(homePageFragment!!)
                    }
                }
                1 -> {
                    rl_sel_title.visibility = View.VISIBLE
                    tv_sel_title.text = "装箱管理"
                    ivPack.isSelected = true
                    if (packFragmentManager == null) {
                        packFragmentManager = PackManageFragment.newInstance()
                        add(R.id.homeContain, packFragmentManager!!)
                    } else {
                        show(packFragmentManager!!)
                    }
                }
                2 -> {
                    rl_sel_title.visibility = View.VISIBLE
                    tv_sel_title.text = "发货管理"
                    ivSend.isSelected = true
                    if (sendManageFragment == null) {
                        sendManageFragment = SendManageFragment.newInstance()
                        add(R.id.homeContain, sendManageFragment!!)
                    } else {
                        show(sendManageFragment!!)
                    }
                }
                3 -> {
                    rl_sel_title.visibility = View.GONE
                    ivMine.isSelected = true
                    if (mineFragment == null) {
                        mineFragment = MineFragment.newInstance()
                        add(R.id.homeContain, mineFragment!!)
                    } else {
                        show(mineFragment!!)
                    }
                }
            }
        }.commitAllowingStateLoss()

    }

    private fun hideFragments(transaction: FragmentTransaction) {
        transaction.run {
            if (homePageFragment != null) hide(homePageFragment!!)
            if (packFragmentManager != null) hide(packFragmentManager!!)
            if (sendManageFragment != null) hide(sendManageFragment!!)
            if (mineFragment != null) hide(mineFragment!!)
        }
    }


    private fun clearAllSelected() {
        ivHomePage.isSelected = false
//        tvHomePage.isSelected = false
        ivPack.isSelected = false
//        tvCommunity.isSelected = false
        ivSend.isSelected = false
//        tvNotification.isSelected = false
        ivMine.isSelected = false
//        tvMine.isSelected = false
    }


    override fun onStart() {
        super.onStart()
        requestCodeQRCodePermissions()
    }


    private val REQUEST_CODE_QRCODE_PERMISSIONS = 1


    val perms:Array<String> = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @AfterPermissionGranted(1)
    private fun requestCodeQRCodePermissions() {
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            EasyPermissions.requestPermissions(
                this@MainActivity,
                "扫描二维码需要打开相机和散光灯的权限",
                REQUEST_CODE_QRCODE_PERMISSIONS, *perms
            )
        }
    }

    companion object{
        fun start(context:Context){
            context.startActivity(Intent(context,MainActivity::class.java))
        }
    }
}