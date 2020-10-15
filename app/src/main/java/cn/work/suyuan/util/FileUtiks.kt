package cn.work.suyuan.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import cn.work.suyuan.SuYuanApplication
import cn.work.suyuan.ui.MainActivity


object FileUtils {
    fun searchFile(context:Context) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//任意类型文件
//任意类型文件
        intent.type = "*/*"
        val bundle = Bundle()
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(context as MainActivity,intent, 1007,bundle)
    }
    val fileList = mutableListOf<FileParam>()

    fun queryFiles():MutableList<FileParam>{
        fileList.clear()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE
        )
        val cursor: Cursor = SuYuanApplication.context.contentResolver.query(
            Uri.parse("content://media/external/file/***"),
            projection,
            MediaStore.Files.FileColumns.DATA + " like ?", arrayOf("%.csv"),
            null
        )!!

        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor
                .getColumnIndex(MediaStore.Files.FileColumns._ID)
            val dataindex: Int = cursor
                .getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val sizeindex: Int = cursor
                .getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            do {
                val id: String = cursor.getString(idIndex)
                val path: String = cursor.getString(dataindex)
                val size: String = cursor.getString(sizeindex)
                val sizes = size.toLong()/1000
                if (sizes<=3072){
                    Log.e("金卡看得见啊可是", sizes.toString())
                    Log.e("及快速导航手机导航键", path.toString())
                    val dot = path.lastIndexOf("/")
                    val name = path.substring(dot + 1)
                    Log.e("test", name)
                    val bean = FileParam(name, path)
                    fileList.add(bean)
                }



            } while (cursor.moveToNext())
        }
        cursor.close()

        val cursor1: Cursor = SuYuanApplication.context.contentResolver.query(
            Uri.parse("content://media/external/file/***"),
            projection,
            MediaStore.Files.FileColumns.DATA + " like ?", arrayOf("%.txt"),
            null
        )!!

        if (cursor1.moveToFirst()) {
            val idIndex: Int = cursor1
                .getColumnIndex(MediaStore.Files.FileColumns._ID)
            val dataindex: Int = cursor1
                .getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val sizeindex: Int = cursor1
                .getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            do {
                val id: String = cursor1.getString(idIndex)
                val path: String = cursor1.getString(dataindex)
                val size: String = cursor1.getString(sizeindex)
                val sizes = size.toLong()/1000
                if (sizes in 500..3072){
                    val dot = path.lastIndexOf("/")
                    val name = path.substring(dot + 1)
                    Log.e("test", name)
                    val bean = FileParam(name, path)
                    fileList.add(bean)
                }
            } while (cursor1.moveToNext())
        }
        cursor1.close()
        val cursor2: Cursor = SuYuanApplication.context.contentResolver.query(
            Uri.parse("content://media/external/file/***"),
            projection,
            MediaStore.Files.FileColumns.DATA + " like ?", arrayOf("%.cvs"),
            null
        )!!

        if (cursor2.moveToFirst()) {
            val idIndex: Int = cursor2
                .getColumnIndex(MediaStore.Files.FileColumns._ID)
            val dataindex: Int = cursor2
                .getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val sizeindex: Int = cursor2
                .getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            do {
                val id: String = cursor2.getString(idIndex)
                val path: String = cursor2.getString(dataindex)
                val size: String = cursor2.getString(sizeindex)
                val sizes = size.toLong()/1000
                if (sizes in 500..3072){
                    val dot = path.lastIndexOf("/")
                    val name = path.substring(dot + 1)
                    Log.e("test", name)
                    val bean = FileParam(name, path)
                    fileList.add(bean)
                }
            } while (cursor2.moveToNext())
        }
        cursor2.close()
        val cursor3: Cursor = SuYuanApplication.context.contentResolver.query(
            Uri.parse("content://media/external/file/***"),
            projection,
            MediaStore.Files.FileColumns.DATA + " like ?", arrayOf("%.xls"),
            null
        )!!

        if (cursor3.moveToFirst()) {
            val idIndex: Int = cursor3
                .getColumnIndex(MediaStore.Files.FileColumns._ID)
            val dataindex: Int = cursor3
                .getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val sizeindex: Int = cursor3
                .getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            do {
                val id: String = cursor3.getString(idIndex)
                val path: String = cursor3.getString(dataindex)
                val size: String = cursor3.getString(sizeindex)
                val sizes = size.toLong()/1000
                    val dot = path.lastIndexOf("/")
                    val name = path.substring(dot + 1)
                    val bean = FileParam(name, path)
                    fileList.add(bean)




            } while (cursor3.moveToNext())
        }
        cursor3.close()
        return fileList
    }

    data class FileParam(val fileName: String, val filePath: String)



}