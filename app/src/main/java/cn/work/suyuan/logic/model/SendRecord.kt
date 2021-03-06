package cn.work.suyuan.logic.model

data class SendRecord(val data: Data) : Model() {
    data class Data(
        val token: String,
        val agent_info:AgentInfo,
        val total: Int,
        val per_page: Int,
        val current_page: Int,
        val last_page: Int,
        val data: MutableList<DataArrays>
    ) {
        data class DataArrays(
            var isCheck: Boolean,
            val id: Int,
            val category_id: Int,
            val product: MutableList<String>,
            val product_time: String,
            val addtime:String,
            val category_name: String,
            val carton: String,
            val ip: String,
            val nickname: String,
            val name:String,
            val title:String,
            val count:Int
        )
        data class AgentInfo(val agent_id:Int,val agent_level:Int)
    }
}


