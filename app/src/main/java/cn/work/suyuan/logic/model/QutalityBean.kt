package cn.work.suyuan.logic.model

data class QutalityBean(val data: Data) : Model() {
    data class Data(
        val total: Int,
        val per_page: Int,
        val current_page: Int,
        val last_page: Int,
        val data: MutableList<Datas>
    ) {
        data class Datas(
            val id: Int,
            val test_report: String,
            val test_report_img: String,
            val addtime: String
        )
    }
}