package cn.work.suyuan.ui.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.Model
import cn.work.suyuan.logic.model.NormalData
import cn.work.suyuan.logic.network.HomePageRepository
import cn.work.suyuan.logic.network.SendPackRepository
import cn.work.suyuan.logic.network.api.MainPageService
import okhttp3.MediaType
import okhttp3.RequestBody

class SendPackViewModel(private val repository: SendPackRepository):ViewModel() {
    /**
     * 获取经销商
     */
    private val distributorRequest = MutableLiveData<String>()
    fun getDistributor() { distributorRequest.value = MainPageService.getDistributor() }
    val distributorLiveData =  Transformations.switchMap(distributorRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getDistributor(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<HomeData>(e)
            }
            emit(result)
        }
    }

    /**
     * 单/整箱发货
     */
    private val sendProductRequest = MutableLiveData<String>()
    fun sendProduct(level: Int, productId: Int, distributorId: Int, productCode: String, productTime: String, productFile: String) {
        sendProductRequest.value = MainPageService.sendProduct(level,productId,distributorId,productCode,productTime,productFile)
    }
    val sendProductLiveData =  Transformations.switchMap(distributorRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getDistributor(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }


}