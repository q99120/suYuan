package cn.work.suyuan.ui.send

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.NormalData
import cn.work.suyuan.logic.model.SendRecord
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.logic.network.SendPackRepository
import cn.work.suyuan.logic.network.api.MainPageService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.File

class SendPackViewModel(private val repository: SendPackRepository) : ViewModel() {
    /**
     * 获取经销商
     */
    private val distributorRequest = MutableLiveData<String>()
    fun getDistributor(flag:Int) {
        distributorRequest.value = MainPageService.getDistributor(flag)
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
    var i = 1
    fun getProduct() {
        i=i++
        Log.e("getproduct",i.toString())
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
        productCode: JSONArray,
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

    /**
     * 装箱单独
     */
    private val doPackSingBoxRequest = MutableLiveData<String>()
    fun doPackSingBox(
        product: JSONArray,
        carton: JSONArray,
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
        Log.e("获取装箱记录",MainPageService.packRecord(product,time,level,page))
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


    /*
*装箱记录
*/
    private val getBoxRecordRequest0 = MutableLiveData<String>()
    fun getBoxRecord0(product: String, time: String, level: Int, page: Int) {
        Log.e("获取装箱记录",MainPageService.packRecord(product,time,level,page))
        getBoxRecordRequest0.value = MainPageService.packRecord(product,time,level,page)
    }
    val boxRecordLiveData0 = Transformations.switchMap(getBoxRecordRequest0) {
        liveData {
            val result = try {
                val body: RequestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), it)
                val recommend = repository.getSendRecord2(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误", e.toString())
                Result.failure<SendRecord>(e)
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

    /**
     * 取消发货记录
     */
    private val cancelSendRequest = MutableLiveData<String>()
    fun cancelSendPack(level: Int, product: JSONArray, productTime: String, productFile: String) {
        cancelSendRequest.value = MainPageService.sendCancel(level,product,productTime,productFile)
    }
    val cancelSendLiveData = Transformations.switchMap(cancelSendRequest) {
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
        }}

    /**
     * 删除发货记录
     */
    val deleteSendRequest = MutableLiveData<String>()
    fun deleteSendRecord(arrayId: Array<Int?>) {
        deleteSendRequest.value = MainPageService.deleteDeliveryRecord(arrayId)
    }

    val deleteSendRecordLiveData = Transformations.switchMap(deleteSendRequest) {
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
     * 批量发货
     */
    private val batchSendRequest = MutableLiveData<String>()
    fun batchSend(product_id:Int,agent_id:Int,product:Array<String>,product_time:String,file:String,level:Int) {
        batchSendRequest.value = MainPageService.batchSend(product_id,agent_id,product,product_time,file,level)
    }

    val batchSendLiveData = Transformations.switchMap(batchSendRequest) {
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
     * 获取一二级经销商
     */


}