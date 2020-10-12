package cn.work.suyuan.logic.model

data class HomeData (val data:MutableList<Data>):Model(){
    data class Data(val id:Int, val title:String,val tab:String, val sort:Int, val img_url:String, var isCheck:Boolean)

}