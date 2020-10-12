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

import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.Model
import cn.work.suyuan.logic.network.api.MainPageService
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 管理所有网络请求。
 *
 * @author vipyinzhiwei
 * @since  2020/5/2
 */
class SuYuanNetwork {

    private val mainPageService = ServiceCreator.create(MainPageService::class.java)



    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

   suspend fun getHomeManageData(url: RequestBody) = mainPageService.getHomeManageData(url).await()
    suspend fun processManage(body: RequestBody) = mainPageService.processManage(body).await()
    suspend fun login(body: RequestBody) = mainPageService.login(body).await()
    suspend fun getUser(body: RequestBody) = mainPageService.getUser(body).await()
    suspend fun getDistributor(body: RequestBody) = mainPageService.getHomeManageData(body).await()


    companion object {

        private var network: SuYuanNetwork? = null

        fun getInstance(): SuYuanNetwork {
            if (network == null) {
                synchronized(SuYuanNetwork::class.java) {
                    if (network == null) {
                        network = SuYuanNetwork()
                    }
                }
            }
            return network!!
        }
    }
}