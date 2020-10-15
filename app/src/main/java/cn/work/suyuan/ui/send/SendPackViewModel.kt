package cn.work.suyuan.ui.send

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.NormalData
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.logic.network.SendPackRepository
import cn.work.suyuan.logic.network.api.MainPageService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SendPackViewModel(private val repository: SendPackRepository) : ViewModel() {
    /**
     * 获取经销商
     */
    private val distributorRequest = MutableLiveData<String>()
    fun getDistributor() {
        distributorRequest.value = MainPageService.getDistributor()
    }

    val distributorLiveData = Transformations.switchMap(distributorRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getDistributor(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<HomeData>(e)
            }
            emit(result)
        }
    }

    /**
     * 获取产品
     */
    private val productRequest = MutableLiveData<String>()
    fun getProduct() {
        productRequest.value = MainPageService.getProduct()
    }

    val productLiveData = Transformations.switchMap(productRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
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
    fun sendProduct(
        level: Int,
        productId: Int,
        distributorId: Int,
        productCode: String,
        productTime: String,
        productFile: String
    ) {
        sendProductRequest.value = MainPageService.sendProduct(
            level,
            productId,
            distributorId,
            productCode,
            productTime,
            productFile
        )
    }

    val sendProductLiveData = Transformations.switchMap(sendProductRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getDistributor(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<HomeData>(e)
            }
            emit(result)
        }
    }

    /**
     * 发货记录
     */
    private val sendRecordRequest = MutableLiveData<String>()
    fun sendRecord(startTime: String, endTime: String, productCode: String, page: Int) {
        sendRecordRequest.value =
            MainPageService.sendProductRecord(startTime, endTime, productCode, page)
    }

    val sendRecordLiveData = Transformations.switchMap(sendRecordRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getSendRecord(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<TokenData>(e)
            }
            emit(result)
        }
    }


    /**
     * 上传文件
     */
    private val upLoadFileRequest = MutableLiveData<MultipartBody.Part>()
    fun upLoadFile(file: File) {
        upLoadFileRequest.value = MainPageService.upLoadFile(file)
    }

    /**
     * 装箱单独
     */
    private val doPackSingBoxRequest = MutableLiveData<String>()
    fun doPackSingBox(
        product: String,
        carton: String,
        product_time: String,
        file: String,
        level: Int,
        ip: String
    ) {
        doPackSingBoxRequest.value =
            MainPageService.packIng(product, carton, product_time, file, level, ip)
    }

    val doPackSingBoxLiveData = Transformations.switchMap(doPackSingBoxRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.doPackOne(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }

    val upLoadFileLiveData = Transformations.switchMap(upLoadFileRequest) {
        liveData {
            val result = try {
                val recommend = repository.upLoadFile(it)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }

    /*
  *外箱列表
  */
    val getBoxListRequest = MutableLiveData<String>()
    fun getBoxList() {
//        getBoxListRequest.value = MainPageService.get
    }


    /*
 *装箱记录
 */
    private val getBoxRecordRequest = MutableLiveData<String>()
    fun getBoxRecord(product: String, time: String, level: Int, page: Int) {
        getBoxRecordRequest.value = MainPageService.packRecord(product,time,level,page)
    }
    val boxRecordLiveData = Transformations.switchMap(getBoxRecordRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getSendRecord(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<TokenData>(e)
            }
            emit(result)
        }
    }


    /**
     * 编辑装箱记录
     */
    val editRecordRequest = MutableLiveData<String>()
    fun editBoxRecord(id: Int, s: String, carton: String) {
        editRecordRequest.value = MainPageService.editPackRecord(id,s,carton)
    }
    val editRecordLiveData = Transformations.switchMap(editRecordRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.doPackOne(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }

    /**
     * 删除装箱记录
     */
    val deleteRequest = MutableLiveData<String>()
    fun deleteBoxRecord(arrayId: Array<Int?>) {
        deleteRequest.value = MainPageService.deletePackRecord(arrayId)
    }
    val deleteRecordLiveData = Transformations.switchMap(deleteRequest) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.doPackOne(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<TokenData>(e)
            }
            emit(result)
        }
    }

}