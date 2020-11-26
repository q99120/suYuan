package cn.work.suyuan.logic.model

data class QutalityBean(val data:MutableList<Datas>):Model(){
    data class Datas(val id:Int,val test_report:String,val test_report_img:String
    ,val addtime:String)
}