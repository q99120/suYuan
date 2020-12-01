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

package cn.work.suyuan.logic.network.api

import cn.work.suyuan.logic.model.*
import cn.work.suyuan.util.APUtils
import cn.work.suyuan.util.FileUtils
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

/**
 * 主页界面，主要包含：（首页，社区，通知，我的）对应的 API 接口。
 *
 * @author vipyinzhiwei
 * @since  2020/5/15
 */
interface MainPageService {
    @POST("api/v1/")
    fun getHomeManageData(@Body body: RequestBody): Call<HomeData>

    @POST("api/v1/")
    fun processManage(@Body body: RequestBody): Call<NormalData>

    @POST("api/v1/")
    fun login(@Body body: RequestBody): Call<TokenData>

    @POST("api/v1/")
    fun getUser(@Body body: RequestBody): Call<UserInfo>

    /**
     * 上传文件
     */
    @Multipart
    @POST("api/v1/")
    fun upLoadFile(@Part part1: MultipartBody.Part): Call<NormalData>


    /**
     * zhuangxiangjilu
     */
    @POST("api/v1/")
    fun sendRecord2(@Body body: RequestBody): Call<SendRecord>


    @POST("api/v1/")
    fun setTracing(@Body body: RequestBody): Call<HomeData>

    @POST("api/v1/")
    fun getQutalityList(@Body body: RequestBody): Call<QutalityBean>

    companion object {
        val gson = Gson()
        private fun getServiceHead(manageServiceJson: JSONObject, interfaceData: String) {
            manageServiceJson.put("requestId", 2)
            manageServiceJson.put("timestamp", System.currentTimeMillis()/1000)
            manageServiceJson.put("token", APUtils.getString("tokens"))
            manageServiceJson.put("interface", interfaceData)
        }


        fun manageService(): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.goodsCate")
            val param = JSONObject()
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }


        fun addProcessService(processName: String, sort: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.goodsCateAdd")
            val param = JSONObject()
            param.put("name", processName)
            param.put("sort", sort)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        fun editProcessService(id: Int, processName: String, sort: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.goodsCateSave")
            val param = JSONObject()
            param.put("id", id)
            param.put("name", processName)
            param.put("sort", sort)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        fun deleteProcess(arrayId: Array<Int?>): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.goodsCateDel")
            val param = JSONObject()
            val jsonArray = JSONArray()
            for (a in arrayId){
                jsonArray.put(a)
            }
            param.put("id",jsonArray)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }


        fun getUser(): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.getUserInfo")
            val param = JSONObject()
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }


        fun login(account: String, password: String): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.RegisterPhone")
            val param = JSONObject()
            param.put("phone", account)
            param.put("pwd", password)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        /**
         * 流程追溯
         */
        fun manageTrace(
            category_id: Int,
            uname: String,
            product: JSONArray,
            product_time: String,
            file: String,
            content: String,
            batch: String,
            orderNum: String,zhijianID:String
        ): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.goodsAdd")
            val param = JSONObject()
            param.put("category_id", category_id)
            param.put("uname", uname)
            param.put("product", product)
            param.put("product_time", product_time)
            param.put("file", file)
            param.put("content", content)
            param.put("batch", batch)
            param.put("order_num", orderNum)
            param.put("report_id", zhijianID)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        /**
         * 溯源信息
         */
        fun getTrace(startTime: String, endTime: String, productName: String, page: Int): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.goodsInfoCate")
            val param = JSONObject()
            param.put("start_time", startTime)
            param.put("end_time", endTime)
            param.put("product", productName)
            param.put("page", page)
            param.put("limit", 10)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        /**
         * 溯源信息删除
         */
        fun deleteTrace(arrayId: Array<Int?>): String {
            val manageServiceJson = JSONObject()
            val param = JSONObject()
            val jsonArray = JSONArray()
            for (a in arrayId){
                jsonArray.put(a)
            }
            param.put("id",jsonArray)
            if (APUtils.getInt("agentLevel",0)==0){
                getServiceHead(manageServiceJson, "shop.goodsDel")
            } else{
                getServiceHead(manageServiceJson, "shop.consignmentInfoDelLower")
                param.put("agent_level",APUtils.getInt("agentLevel",1))
            }
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }


        /**
         * 获取经销商
         */
        fun getDistributor(): String? {
            val manageServiceJson = JSONObject()
            val param = JSONObject()
            if (APUtils.getInt("agentLevel",0)==0){
                getServiceHead(manageServiceJson, "shop.getAgent")
            }else{
                getServiceHead(manageServiceJson, "shop.getAgentLower")
                param.put("level",APUtils.getInt("agentLevel",0))
                param.put("id",APUtils.getInt("agentId",0))
            }
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        /**
         * 获取产品
         */
        fun getProduct(): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.getProduct")
            val param = JSONObject()
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //单个整箱发货
        fun sendProduct(
            level: Int,
            productId: Int,
            distributorId: Int,
            productCode: JSONArray,
            productTime: String,
            productFile: String
        ): String? {
            val manageServiceJson = JSONObject()
            val param = JSONObject()
            param.put("product_id", productId)
            param.put("agent_id", distributorId)
            param.put("product", productCode)
            param.put("product_time", productTime)
            param.put("file", productFile)
            param.put("level", level)
            if (APUtils.getInt("agentLevel",0)==0){
                getServiceHead(manageServiceJson, "shop.consignmentAdd")
            } else{
                getServiceHead(manageServiceJson, "shop.consignmentAddLower")
                param.put("agent_level",APUtils.getInt("agentLevel",1))
            }
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //发货记录
        fun sendProductRecord(
            startTime: String,
            endTime: String,
            productCode: String,
            page: Int
        ): String? {
            val manageServiceJson = JSONObject()
            val param = JSONObject()
            param.put("start_time", startTime)
            param.put("end_time", endTime)
            param.put("product", productCode)
            param.put("page", page)
            param.put("limit", 10)
            if (APUtils.getInt("agentLevel",0)==0){
                getServiceHead(manageServiceJson, "shop.consignmentInfo")
            } else{
                getServiceHead(manageServiceJson, "shop.consignmentInfoLower")
                param.put("agent_level",APUtils.getInt("agentLevel",1))
                param.put("agent_id",APUtils.getInt("agentId",1))
            }
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //取消发货
        fun sendCancel(level: Int, product: JSONArray, product_time: String, file: String): String {
            val manageServiceJson = JSONObject()
            val param = JSONObject()
            param.put("level", level)
            param.put("product", product)
            param.put("product_time", product_time)
            param.put("file", file)
            if (APUtils.getInt("agentLevel",0)==0){
                getServiceHead(manageServiceJson, "shop.ConsignmentCancelAdd")
            } else{
                getServiceHead(manageServiceJson, "shop.ConsignmentCancelAddLower")
                param.put("agent_level",APUtils.getInt("agentLevel",1))
                param.put("agent_id",APUtils.getInt("agentId",1))
            }
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //删除发货记录
        fun deleteDeliveryRecord(arrayId: Array<Int?>): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.consignmentInfoDel")
            val param = JSONObject()
           val jsonArray = JSONArray()
            for (a in arrayId){
                jsonArray.put(a)
            }
            param.put("id",jsonArray)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //修改用户信息
        fun updateUserInfo(
            type: Int,
            cover: Int,
            nickname: String,
            sex: Int,
            descs: String,
            age: Int
        ): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.editUserInfo")
            val param = JSONObject()
            param.put("type", type)
            param.put("cover", cover)
            param.put("nickname", nickname)
            param.put("sex", sex)
            param.put("descs", descs)
            param.put("age", age)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //装箱
        fun packIng(
            product: JSONArray,
            carton: String,
            product_time: String,
            file: String,
            level: Int,
            ip: String
        ): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.encasementAdd")
            val param = JSONObject()
            param.put("product", product)
            param.put("carton", carton)
            param.put("product_time", product_time)
            param.put("file", file)
            param.put("level", level)
            param.put("ip", ip)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //装箱记录
        fun packRecord(product: String, time: String, level: Int, page: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.encasementInfo")
            val param = JSONObject()
            param.put("product", product)
            param.put("time", time)
            param.put("level", level)
            param.put("page", page)
            param.put("limit", 10)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //装箱记录编辑
        fun editPackRecord(id: Int, product: String, carton: String): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.encasementInfoSave")
            val param = JSONObject()
            param.put("product", product)
            param.put("id", id)
            param.put("carton", carton)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        //装箱记录删除
        fun deletePackRecord(arrayId: Array<Int?>, level: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.encasementInfoDel")
            val param = JSONObject()
           val jsonArray = JSONArray()
            for (a in arrayId){
                jsonArray.put(a)
            }
            param.put("id",jsonArray)
            param.put("level",level)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        /**
         * 上传文件
         */
        fun upLoadFile(file: File): MultipartBody.Part {
            val body: RequestBody = RequestBody.create(MediaType.get("multipart/form-data"), file)
            val part = MultipartBody.Part.createFormData("file", file.name, body)
            return part
        }

        /**
         * 上传头像
         */
        fun upLoadHead(string: String): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.comFile")
            val param = JSONObject()
            param.put("file", string)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }


        //获取生产流程
        val array1 = arrayOf("txt","xls","xlsx","csv")
        val array2 = arrayOf("1","2","3","4")
        fun getFileType(): MutableList<FileUtils.FileParam> {
            val list = mutableListOf<FileUtils.FileParam>()
            for (a in array1.indices){
                val bean = FileUtils.FileParam(array1[a], array2[a])
                list.add(bean)
            }
            return list
        }

        /**
         * 批量发货
         */
        fun batchSend(
            product_id: Int,
            agent_id: Int,
            product: Array<String>,
            product_time: String,
            file: String,
            level: Int
        ): String {
            val manageServiceJson = JSONObject()
            val param = JSONObject()
            param.put("product_id", product_id)
            param.put("agent_id", agent_id)
            val jsonArray = JSONArray()
            for (a in product){
                jsonArray.put(a)
            }
            param.put("product", jsonArray)
            param.put("product_time", product_time)
            param.put("file", file)
            param.put("level", level)
            if (APUtils.getInt("agentLevel",0)==0){
                getServiceHead(manageServiceJson, "shop.consignmentAddAll")
            } else{
                getServiceHead(manageServiceJson, "shop.consignmentAddAllLower")
                param.put("agent_level",APUtils.getInt("agentLevel",1))
            }
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        fun sendReport(edittext: String, productFile: String): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.reportAdd")
            val param = JSONObject()
            param.put("test_report", edittext)
            param.put("test_report_img", productFile)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }

        fun getQutalityList(page: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson, "shop.reportInfo")
            val param = JSONObject()
            param.put("page", page)
            param.put("limit", 10)
            manageServiceJson.put("param", param)
            return manageServiceJson.toString()
        }


    }

}