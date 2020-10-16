package cn.work.suyuan.ui.mine

import android.graphics.BitmapFactory
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
import cn.work.suyuan.ui.home.HomeViewModel
import cn.work.suyuan.util.FileUtils
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.widget.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment: BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_mine, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getUser()
        initView()
        observer()
    }

    private fun initView() {

        setOnClickListener(llEditHead){
            when(this){
                llEditHead->{
                    EasyPhotos.createAlbum(activity,true,GlideEngine.getInstance()).
                    setFileProviderAuthority("cn.work.suyuan.fileprovider")
                        .start(103)
                }
            }
        }
    }

    private fun observer() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
              val rp = it.getOrNull()?:return@Observer
               val userData = rp.data
              tvNickName.text = userData.nickname
            tvDecs.text = userData.descs
            tvAge.text = userData.age.toString()
            tvSex.text = userData.sex.toString()
            GlideEngine.getInstance().loadPhoto(requireContext(),
                Uri.parse(userData.cover),ivMineHead)
        })

        viewModel.uploadHeadLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            Log.e("获取id11",rp.msg.toString())
            rp.msg.toast()
            if (rp.code == 200){
                updateUser(1,rp.data.lid.toInt(),"",0,"",0) }
        })
        viewModel.updateUserLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            rp.msg.toast()
        })


    }

    private fun updateUser(type: Int,cover:Int,nickName:String,sex:Int,descs:String,age:Int) {
         viewModel.updateUser(type,cover,nickName,sex,descs,age)
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is StringEvent )
        if (messageEvent.code == 103) {
            GlideEngine.getInstance().loadPhoto(activity,Uri.parse(messageEvent.message),ivMineHead)
            Log.e("获取路径1",messageEvent.path)
            val base64Result = FileUtils.imageToBase64(messageEvent.path)
//            xinghao.setImageBitmap(FileUtils.base64ToBitmap(base64Result))
            viewModel.uploadHead(base64Result!!)
        }
    }


    companion object {
        fun newInstance() = MineFragment()
    }
}