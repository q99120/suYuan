package cn.work.suyuan.logic.model

data class UserInfo (val data: UserData):Model(){
     data class UserData(val user_id:Int,val account:String,val nickname:String,val cover:String
     ,val sex:Int,val descs:String,val age:Int)
}