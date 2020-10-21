package cn.work.suyuan.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.layout_tracing_fm.*
import org.json.JSONArray

object SuYuanUtil {
    /**
     * 处理产品编辑框的点击事件
     */
    fun getEditProduct(editResult: String):JSONArray {
        val gson = Gson()
        var arrayCode: Array<String?>? = null
        val builder = editResult.split("\n")
        val jsonArray = JSONArray()
        for (b in builder.indices) {
            if (builder[b].isNotEmpty()) {
                jsonArray.put(builder[b])
            }
        }
        return jsonArray
    }
}