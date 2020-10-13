package cn.work.suyuan.logic.model

data class TokenData(val data: Data) : Model() {
    data class Data(
        val token: String,
        val total: Int,
        val per_page: Int,
        val current_page: Int,
        val last_page: Int,
        val data: MutableList<DataArrays>
    ) {
        data class DataArrays(
            var isCheck: Boolean,
            val id: Int,
            val uname: String,
            val category_id: Int,
            val product: String,
            val product_time: String,
            val category_name: String,
            val carton: String,
            val ip: String,
            val nickname: String,
            val name:String,
            val title:String
        )
    }
}


