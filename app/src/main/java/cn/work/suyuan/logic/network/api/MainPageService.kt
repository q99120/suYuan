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

import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.NormalData
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.logic.model.UserInfo
import cn.work.suyuan.util.APUtils
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

/**
 * 主页界面，主要包含：（首页，社区，通知，我的）对应的 API 接口。
 *
 * @author vipyinzhiwei
 * @since  2020/5/15
 */
interface MainPageService {
    @POST("api/v1/")
    fun getHomeManageData(@Body body: RequestBody):Call<HomeData>
    @POST("api/v1/")
    fun processManage(@Body body: RequestBody):Call<NormalData>

    @POST("api/v1/")
    fun login(@Body body: RequestBody):Call<TokenData>

    @POST("api/v1/")
    fun getUser(@Body body: RequestBody):Call<UserInfo>




    companion object {
        private fun getServiceHead(manageServiceJson: JSONObject,interfaceData:String) {
            manageServiceJson.put("requestId",2)
            manageServiceJson.put("timestamp","1592816311")
            manageServiceJson.put("token",APUtils.getString("tokens"))
            manageServiceJson.put("interface",interfaceData)
        }


        fun manageService(): String {
            val manageServiceJson = JSONObject()
           getServiceHead(manageServiceJson,"shop.goodsCate")
            val param = JSONObject()
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }


        fun addProcessService(processName: String, sort: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.goodsCateAdd")
            val param = JSONObject()
            param.put("name",processName)
            param.put("sort",sort)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }

        fun editProcessService(id:Int,processName: String, sort: Int): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.goodsCateSave")
            val param = JSONObject()
            param.put("id",id)
            param.put("name",processName)
            param.put("sort",sort)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }

        fun deleteProcess(arrayId: Array<Int?>): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.goodsCateDel")
            val param = JSONObject()
            param.put("id",arrayId)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }

        fun deleteTrace(arrayId: Array<Int?>): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.goodsDel")
            val param = JSONObject()
            param.put("id",arrayId)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }


        fun getUser(): String {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.getUserInfo")
            val param = JSONObject()
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }






        fun login(account: String, password: String): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.RegisterPhone")
            val param = JSONObject()
            param.put("phone",account)
            param.put("pwd",password)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }

        /**
         * 溯源信息
         */
        fun getTrace(startTime: String, endTime: String, productName: String, page: Int): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.goodsInfoCate")
            val param = JSONObject()
            param.put("start_time",startTime)
            param.put("end_time",endTime)
            param.put("product",productName)
            param.put("page",page)
            param.put("limit",10)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }

        /**
         * 获取经销商
         */
        fun getDistributor(): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.getAgent")
            val param = JSONObject()
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }

        //发货
        fun sendProduct(level: Int, productId: Int, distributorId: Int, productCode: String, productTime: String, productFile: String): String? {
            val manageServiceJson = JSONObject()
            getServiceHead(manageServiceJson,"shop.consignmentAdd")
            val param = JSONObject()
            param.put("product_id",productId)
            param.put("agent_id",distributorId)
            param.put("product",productCode)
            param.put("product_time",productTime)
            param.put("file",productFile)
            param.put("level",level)
            manageServiceJson.put("param",param)
            return manageServiceJson.toString()
        }


    }

}