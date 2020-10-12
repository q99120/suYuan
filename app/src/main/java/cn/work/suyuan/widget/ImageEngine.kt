package cn.work.suyuan.widget

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

interface ImageEngine {

    /**
     * 加载图片到ImageView
     *
     * @param context   上下文
     * @param uri 图片Uri
     * @param imageView 加载到的ImageView
     */
    //安卓10推荐uri，并且path的方式不再可用
    fun loadPhoto(context: Context, uri: Uri, imageView: ImageView)

    /**
     * 加载gif动图图片到ImageView，gif动图不动
     *
     * @param context   上下文
     * @param gifUri  gif动图路径Uri
     * @param imageView 加载到的ImageView
     *
     *
     * 备注：不支持动图显示的情况下可以不写
     */
    //安卓10推荐uri，并且path的方式不再可用
    fun loadGifAsBitmap(context: Context, gifUri: Uri, imageView: ImageView)

    /**
     * 加载gif动图到ImageView，gif动图动
     *
     * @param context   上下文
     * @param gifUri   gif动图路径Uri
     * @param imageView 加载动图的ImageView
     *
     *
     * 备注：不支持动图显示的情况下可以不写
     */
    //安卓10推荐uri，并且path的方式不再可用
    fun loadGif(context: Context, gifUri: Uri, imageView: ImageView)

    /**
     * 获取图片加载框架中的缓存Bitmap
     *
     * @param context 上下文
     * @param uri    图片路径
     * @param width   图片宽度
     * @param height  图片高度
     * @return Bitmap
     * @throws Exception 异常直接抛出，EasyPhotos内部处理
     */
    //安卓10推荐uri，并且path的方式不再可用
    @Throws(Exception::class)
    fun getCacheBitmap(context: Context, uri: Uri, width: Int, height: Int): Bitmap?
}