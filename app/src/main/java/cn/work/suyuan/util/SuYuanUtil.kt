package cn.work.suyuan.util

import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_tracing_fm.*

object SuYuanUtil {
    /**
     * 处理产品编辑框的点击事件
     */
    fun getEditProduct(editResult: String):String {
        val gson = Gson()
        var arrayCode: Array<String?>? = null
        val builder = editResult.split("\n")
        val list = mutableListOf<String>()
        for (b in builder.indices) {
            if (builder[b].isNotEmpty()) {
                list.add(builder[b])
            }
        }
        arrayCode = arrayOfNulls(list.size)
        for (l in 0 until list.size) {
            arrayCode[l] = list[l]
        }
        return gson.toJson(arrayCode)
    }
}