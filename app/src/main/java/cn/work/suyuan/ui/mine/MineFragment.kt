package cn.work.suyuan.ui.mine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.event.MessageEvent
import cn.work.suyuan.event.StringEvent
import cn.work.suyuan.ui.LoginActivity
import cn.work.suyuan.ui.dialog.ExitDialog
import cn.work.suyuan.ui.dialog.FileChooseDialog
import cn.work.suyuan.ui.dialog.UpdateUserDialog
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.APUtils
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.widget.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_mine, container, false))
    }

    val intents = Intent()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (APUtils.getInt("loginFlag", 0) == 0) {
            viewModel.getUser()
        }
        initView()
        observer()
    }

    override fun onInvisible() {
    }

    override fun initData() {
    }

    private fun initView() {

        setOnClickListener(
            tvNickName,
            ivSetting,
            btnExit,
            ivMineHead,
            xinghao,
            editNickeName,
            editSex,
            editAge,
            editDescribe
        ) {
            when (this) {
                ivMineHead -> {
                    PhotoViewActivity.start(activity, userCover)
                }
                editSex -> {
                    fileChooseDialog.setData(
                        3,
                        viewModel.getSex(),
                        object : FileChooseDialog.FileClick {
                            override fun fileClick(fileName: String, filePath: String) {
                                tvSex.text = fileName
                                viewModel.updateUser(3, 0, "", filePath.toInt(), "", 1)
                            }

                        })
                }
                xinghao -> {
                    EasyPhotos.createAlbum(activity, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("cn.work.suyuan.fileprovider")
                        .start(103)
                }
                editNickeName -> {
                    updateUserDialog.updateData(1, object : UpdateUserDialog.UpUserCallBack {
                        override fun upUser(result: String) {
                            tvNickName.text = result
                            viewModel.updateUser(2, 0, result, 1, "", 1)
                        }
                    })
                }
                editDescribe -> {
                    updateUserDialog.updateData(2, object : UpdateUserDialog.UpUserCallBack {
                        override fun upUser(result: String) {
                            tvDecs.text = result
                            viewModel.updateUser(4, 0, "", 1, result, 1)
                        }
                    })
                }
                editAge -> {
                    updateUserDialog.updateData(3, object : UpdateUserDialog.UpUserCallBack {
                        override fun upUser(result: String) {
                            tvAge.text = result
                            viewModel.updateUser(7, 0, result, 1, "", result.toInt())
                        }
                    })
                }
                ivSetting -> SettingActivity.start(activity)
                btnExit -> {
                    exitDialog.setClick(object : ExitDialog.HomeNormalClick {
                        override fun dialogClick() {
                            APUtils.remove("tokens")
                            LoginActivity.start(activity)
                            activity.finish()
                        }

                    })
                }
            }
        }
    }

    var userCover = ""
    private fun observer() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            val userData = rp.data
            userCover = userData.cover
            tvNickName.text = userData.nickname
            tvDecs.text = userData.descs
            tvAge.text = userData.age.toString()
            when (userData.sex) {
                0 -> {
                    tvSex.text = "男"
                }
                1 -> {
                    tvSex.text = "女"
                }
                else -> {
                    tvSex.text = "保密"
                }
            }
            tvSex.text = userData.sex.toString()
            GlideEngine.getInstance().loadPhoto(
                requireContext(),
                Uri.parse(userData.cover), ivMineHead
            )
        })

        viewModel.uploadHeadLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            Log.e("获取id11", rp.msg.toString())
            rp.msg.toast()
            if (rp.code == 200) {
                updateUser(1, rp.data.lid.toInt(), "", 0, "", 0)
            }
        })
        viewModel.updateUserLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull() ?: return@Observer
            rp.msg.toast()
        })


    }

    private fun updateUser(
        type: Int,
        cover: Int,
        nickName: String,
        sex: Int,
        descs: String,
        age: Int
    ) {
        viewModel.updateUser(type, cover, nickName, sex, descs, age)
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is StringEvent)
            if (messageEvent.code == 103) {
                GlideEngine.getInstance()
                    .loadPhoto(activity, Uri.parse(messageEvent.message), ivMineHead)
                Log.e("获取路径1", messageEvent.path)
                val base64Result = FileUtils.imageToBase64(messageEvent.path)
                viewModel.uploadHead(base64Result!!)
            }
    }


    companion object {
        fun newInstance() = MineFragment()
    }

    private val updateUserDialog by lazy {
        UpdateUserDialog(requireContext())
    }
}