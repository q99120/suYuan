package cn.work.suyuan.logic.model

data class HomeData(val data: MutableList<Data>) : Model() {
    data class Data(
        val id: Int,
        val name: String,
        val title: String,
        val tab: String,
        val sort: Int,
        val img_url: String,
        val product:String,
        val product_time:String,
        val ip:String,
        val nickname:String,
        var isCheck: Boolean
    )

}