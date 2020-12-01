package cn.work.suyuan.ui

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.event.RefreshEvent
import cn.work.suyuan.event.StringEvent
import cn.work.suyuan.ui.dialog.ExitDialog
import cn.work.suyuan.ui.home.HomePageFragment
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.ui.mine.MineFragment
import cn.work.suyuan.ui.packmanage.PackManageFragment
import cn.work.suyuan.ui.send.SendManageFragment
import cn.work.suyuan.util.APUtils
import cn.work.suyuan.util.ActivityCollector.removeAll
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.util.NormalUi
import cn.work.suyuan.widget.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_bar.*
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList


class MainActivity : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    private val fragmentManager: FragmentManager by lazy { supportFragmentManager }

    private var homePageFragment: HomePageFragment? = null
    private var packFragmentManager: PackManageFragment? = null
    private var sendManageFragment: SendManageFragment? = null
    private var mineFragment: MineFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (APUtils.getInt("agentLevel", 0) != 0) {
            layoutBottomView.visibility = View.GONE
        }
        val flag = intent.getIntExtra("intentFlag", 0)
        APUtils.putInt("loginFlag", flag)
//        if (flag == 0) {
//        } else if (flag == 1) {
//            val nickName = intent.getStringExtra("userName")
//            val headUrl = intent.getStringExtra("userHead")
//            tvUserName.text = nickName
//            GlideEngine.getInstance().loadPhoto(this, Uri.parse(headUrl), ivUserHead)
//        }
        viewModel.getUser()

        observer()
    }

    private fun observer() {
        viewModel.userLiveData.observe(this, Observer {
            val rp = it.getOrNull() ?: return@Observer
            val response = rp.data
            Log.e("获取个人用户信息",response.toString())
            tvUserName.text = response.nickname
            GlideEngine.getInstance().loadPhoto(this, Uri.parse(response.cover), ivUserHead)
            APUtils.putInt("trace_process_id",response.trace_process_id)
            APUtils.putString("trace_process_title",response.trace_process_title)
        })

    }


    override fun setupViews() {
        super.setupViews()
        setOnClickListener(ll_home, ll_pack, ll_send, ll_mine, llHead,ivAddZhijian) {
            when (this) {
                ll_home -> {
                    notificationUiRefresh(0)
                    setTabSelection(0)
                }
                ll_pack -> {
                    notificationUiRefresh(1)
                    setTabSelection(1)
                }
//                ll_send -> {
//                    notificationUiRefresh(2)
//                    setTabSelection(2)
//                }
                ll_mine -> {
                    notificationUiRefresh(3)
                    setTabSelection(2)
                }
                llHead -> {
                    if (APUtils.getInt("agentLevel", 0) != 0) {
                        exitDialog.setClick(object : ExitDialog.HomeNormalClick {
                            override fun dialogClick() {
                                APUtils.remove("tokens")
                                LoginActivity.start(this@MainActivity)
                                finish()
                            }
                        })
                    }
                }
                ivAddZhijian->{
                        QualityActivity.start(this@MainActivity)
                }

            }
        }
//        if (APUtils.getInt("agentLevel", 0) != 0) {
//            setTabSelection(2)
//            return
//        }
        setTabSelection(0)
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
//            2 -> {
//                if (ivSend.isSelected) EventBus.getDefault()
//                    .post(RefreshEvent(SendManageFragment::class.java))
//            }
            2 -> {
                if (ivMine.isSelected) EventBus.getDefault()
                    .post(RefreshEvent(MineFragment::class.java))
            }
        }
    }

    private fun setTabSelection(index: Int) {

        fragmentManager.beginTransaction().apply {
            clearAllSelected()
            hideFragments(this)
            when (index) {
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
//                2 -> {
//                    rl_sel_title.visibility = View.VISIBLE
//                    tv_sel_title.text = "发货管理"
//                    ivSend.isSelected = true
//                    if (sendManageFragment == null) {
//                        sendManageFragment = SendManageFragment.newInstance()
//                        add(R.id.homeContain, sendManageFragment!!)
//                    } else {
//                        show(sendManageFragment!!)
//                    }
//                }
                2 -> {
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
//            if (sendManageFragment != null) hide(sendManageFragment!!)
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


    private val perms: Array<String> = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
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

    companion object {
        fun start(flag: Int, context: Context, userName: String, userHead: String) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("intentFlag", flag)
            when (flag) {
                0 -> {
                    context.startActivity(intent)
                }
                1 -> {
                    intent.putExtra("userName", userName)
                    intent.putExtra("userHead", userHead)
                    context.startActivity(intent)
                }
                2 -> {
                    //预留微信
                }
                3 -> context.startActivity(intent)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1007) {
                Log.e("12121", data.toString())
                val uri: Uri = data.data!!
                val path = getFilePathFromContentUri(uri, contentResolver)
            }
            if (requestCode == 103) {

                val resultPhotos: ArrayList<Photo>? =
                    data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)
                for (f in resultPhotos!!) {
                    EventBus.getDefault().post(StringEvent(103, f.uri.toString(), f.path))
                }
            }
        }
    }

    private fun getFilePathFromContentUri(
        selectedVideoUri: Uri?,
        contentResolver: ContentResolver
    ): String? {
        val filePath: String
        val filePathColumn = arrayOf(MediaColumns.DATA)
        val cursor = contentResolver.query(selectedVideoUri!!, filePathColumn, null, null, null)
        //      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            processBackPressed()
        }
    }

    private var backPressTime = 0L

    private fun processBackPressed() {
        val now = System.currentTimeMillis()
        if (now - backPressTime > 2000) {
            String.format(getString(R.string.press_again_to_exit), NormalUi.appName).toast()
            backPressTime = now
        } else {
            removeAll()
            super.onBackPressed()
        }
    }

    private val exitDialog by lazy {
        ExitDialog(this)
    }


}