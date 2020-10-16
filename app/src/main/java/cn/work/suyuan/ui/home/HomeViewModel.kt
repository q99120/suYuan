package cn.work.suyuan.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cn.work.suyuan.logic.model.HomeData
import cn.work.suyuan.logic.model.NormalData
import cn.work.suyuan.logic.model.TokenData
import cn.work.suyuan.logic.model.UserInfo
import cn.work.suyuan.logic.network.HomePageRepository
import cn.work.suyuan.logic.network.api.MainPageService
import cn.work.suyuan.util.FileUtils
import okhttp3.MediaType.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class HomeViewModel(private val repository: HomePageRepository) : ViewModel() {


    private var requestParamLiveData = MutableLiveData<String>()


    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.getHome(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<HomeData>(e)
            }
            emit(result)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = MainPageService.manageService()
    }

    private var addProcessRequest = MutableLiveData<String>()
    private var editProcessRequest = MutableLiveData<String>()
    //添加流程
    fun addProcess(processName: String, sort: Int) {
        addProcessRequest.value =  MainPageService.addProcessService(processName,sort)
    }

    val addProcessLiveData = Transformations.switchMap(addProcessRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.addProcess(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }



    //编辑流程
    fun editProcess(id: Int, processName: String, sort: Int) {
        editProcessRequest.value =  MainPageService.editProcessService(id,processName,sort)
    }

    val editProcessLiveData = Transformations.switchMap(editProcessRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.editProcess(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }

    private var deleteProcessRequest = MutableLiveData<String>()
    fun deleteProcess(arrayId: Array<Int?>) {
        deleteProcessRequest.value = MainPageService.deleteProcess(arrayId)
    }
    val deleteProcessLiveData = Transformations.switchMap(deleteProcessRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.deleteProcess(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("返回错误企管科",e.toString())
                Result.failure<NormalData>(e)
            }
            emit(result)
        }
    }

    /**
     * 获取个人用户信息
     */
    private var getUserRequest = MutableLiveData<String>()
    fun getUser(){
        getUserRequest.value = MainPageService.getUser()
    }
    val userLiveData  = Transformations.switchMap(getUserRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.getUser(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<UserInfo>(e)
            }
            emit(result)
        }
    }

    //登录
    private var loginRequest = MutableLiveData<String>()
    fun login(account: String, password: String) {
        loginRequest.value = MainPageService.login(account,password) }
    val loginLiveData = Transformations.switchMap(loginRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.loginAndTrace(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<TokenData>(e)
            }
            emit(result)
        }
    }


    //溯源信息
    private var traceRequest = MutableLiveData<String>()
    fun getTrace(startTime: String, endTime: String, productName: String,page:Int) {
        traceRequest.value = MainPageService.getTrace(startTime,endTime,productName,page)
    }
    val traceLiveData = Transformations.switchMap(traceRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.loginAndTrace(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<TokenData>(e)
            }
            emit(result)
        }
    }


    //删除溯源信息
    fun deleteTrace(arrayId: Array<Int?>) {
        deleteProcessRequest.value = MainPageService.deleteTrace(arrayId)
    }

    /**
     * 流程追溯
     */
    fun refreshTrace() {
    }

    //获取生产流程
    val array1 = arrayOf("原料入库","组装","质检","包装","出库","发货")
    val array2 = arrayOf("1","2","3","4","5","6")

    fun getCate(): MutableList<FileUtils.FileParam> {
        val list = mutableListOf<FileUtils.FileParam>()
         for (a in array1.indices){
             val bean = FileUtils.FileParam(array1[a],array2[a])
             list.add(bean)
         }
        return list
    }

    private val uploadHeadRequest = MutableLiveData<String>()
    fun uploadHead(string: String) {
        uploadHeadRequest.value = MainPageService.upLoadHead(string)
    }
    val uploadHeadLiveData  = Transformations.switchMap(uploadHeadRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.getUser(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误信息",e.toString())
                Result.failure<UserInfo>(e)
            }
            emit(result)
        }
    }


    /**
     * 修改用户信息
     */
    private val updateUserRequest = MutableLiveData<String>()
    fun updateUser(type: Int, cover: Int, nickName: String, sex: Int, descs: String, age: Int) {
        updateUserRequest.value = MainPageService.updateUserInfo(type,cover,nickName,sex,descs,age)
    }
    val updateUserLiveData  = Transformations.switchMap(updateUserRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.editProcess(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误信息",e.toString())
                Result.failure<NormalData>(e)
            }
            emit(result)
        }}

    /**
     * 流程追溯
     */
    val setTracingRequest = MutableLiveData<String>()
    fun setTracing(category_id:Int,uname:String,product:String,product_time:String,file:String) {
        setTracingRequest.value = MainPageService.manageTrace(category_id,uname,product,product_time,file)
    }
    val setTracingLiveData  = Transformations.switchMap(setTracingRequest) {
        liveData {
            val result = try {
                val body: RequestBody = RequestBody.create(parse("application/json; charset=utf-8"), it)
                val recommend = repository.editProcess(body)
                Result.success(recommend)
            } catch (e: Exception) {
                Log.e("获取错误信息",e.toString())
                Result.failure<NormalData>(e)
            }
            emit(result)
        }}

































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


}