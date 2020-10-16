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

package cn.work.suyuan.logic.network

import android.util.Log
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * 主页界面，主要包含：（首页，社区，通知，我的），对应的仓库数据管理。
 *
 * @author vipyinzhiwei
 * @since  2020/5/2
 */
class HomePageRepository private constructor(private val network: SuYuanNetwork) {

    suspend fun upLoadFile(part: MultipartBody.Part)= withContext(Dispatchers.IO){
        val response = network.upLoadFile(part)
        response
    }



   suspend fun getHome(body: RequestBody) = withContext(Dispatchers.IO){
       val response = network.getHomeManageData(body)
       response
   }

    suspend fun addProcess(body: RequestBody) = withContext(Dispatchers.IO){
        val response = network.processManage(body)
        Log.e("添加结果",response.toString())
        response
    }

    suspend fun editProcess(body: RequestBody) = withContext(Dispatchers.IO){
        val response = network.processManage(body)
        response
    }

    suspend fun deleteProcess(body: RequestBody) = withContext(Dispatchers.IO){
        val response = network.processManage(body)
        response
    }

    suspend fun loginAndTrace(body: RequestBody)= withContext(Dispatchers.IO){
        val response = network.login(body)
        response
    }

    suspend fun getUser(body: RequestBody)= withContext(Dispatchers.IO){
        val response = network.getUser(body)
        response
    }



    companion object {

        private var repository: HomePageRepository? = null

        fun getInstance( network: SuYuanNetwork): HomePageRepository {
            if (repository == null) {
                synchronized(HomePageRepository::class.java) {
                    if (repository == null) {
                        repository = HomePageRepository(network)
                    }
                }
            }

            return repository!!
        }
    }
}