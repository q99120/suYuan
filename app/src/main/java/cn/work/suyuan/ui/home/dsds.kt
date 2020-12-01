package cn.work.suyuan.ui.home

import cn.work.suyuan.ui.dialog.QutalityListDialog
import cn.work.suyuan.ui.dialog.QutalityListDialog.quRefresh

class dsds : QutalityListDialog.FileClick, quRefresh {
    override fun refresh(page: Int) {}
    override fun fileClick(reportName: String, id: Int) {}
}