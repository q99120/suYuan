package cn.work.suyuan.ui.mine

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import cn.work.suyuan.R
import cn.work.suyuan.common.ui.BaseActivity
import cn.work.suyuan.widget.GlideEngine
import kotlinx.android.synthetic.main.activity_photoview.*

class PhotoViewActivity:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)
        initData()
    }

    private fun initData() {
        val urls = intent.getStringExtra("photoUrl")
        GlideEngine.getInstance().loadPhoto(this, Uri.parse(urls),iv_photo)

        ivBack.setOnClickListener { finish() }
        tvUpHead.setOnClickListener {  }

    }

    companion object{
        fun start(context: Context,url:String){
            val intent = Intent(context,PhotoViewActivity::class.java)
            intent.putExtra("photoUrl",url)
            context.startActivity(intent)
        }
    }
}