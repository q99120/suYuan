package cn.work.suyuan.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import cn.work.suyuan.logic.network.api.MainPageService
import cn.work.suyuan.ui.MainActivity
import cn.work.suyuan.ui.dialog.FileChooseDialog
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream


object FileUtils {
    fun searchFile(context: Context) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//任意类型文件
//任意类型文件
        intent.type = "*/*"
        val bundle = Bundle()
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(context as MainActivity, intent, 1007, bundle)
    }

    val fileList = mutableListOf<FileParam>()


    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DATA,
        MediaStore.Files.FileColumns.SIZE
    )

    fun queryFiles(context: Context, fileName: String): MutableList<FileParam> {
        fileList.clear()
        val contentResolver = context.contentResolver
        queryCsv(contentResolver, "%.$fileName")
        return fileList
    }

    private fun queryCsv(contentResolver: ContentResolver, type: String) {
        val cursor: Cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection, MediaStore.Files.FileColumns.DATA + " like ?", arrayOf(type), null
        )!!

        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor
                .getColumnIndex(MediaStore.Files.FileColumns._ID)
            val dataIndex: Int = cursor
                .getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val sizeIndex: Int = cursor
                .getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            do {
                val id: String = cursor.getString(idIndex)
                val path: String = cursor.getString(dataIndex)
                val size: String = cursor.getString(sizeIndex)
                val sizes = size.toLong() / 1000
                if (sizes in 101..3072) {
                    val dot = path.lastIndexOf("/")
                    val name = path.substring(dot + 1)
                    Log.e("test", name)
                    val bean = FileParam(name, path)
                    fileList.add(bean)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    data class FileParam(val fileName: String, val filePath: String)


    /**
     * 把文件转换为base64
     */
    fun imageToBase64(path: String): String? {
        if (TextUtils.isEmpty(path)) {
            return null!!
        }
        var `is`: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            `is` = FileInputStream(path)
            data = ByteArray(`is`.available())
            `is`.read(data)
            result = Base64.encodeToString(data, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
        return "data:image/png;base64,$result"
    }

    fun base64ToBitmap(base64Data: String?): Bitmap? {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private var fileBack: CallBackFile? = null
    fun upLoadFiles(
        context: Context,
        fileChooseDialog: FileChooseDialog,
        callBackFile: FileUtils.CallBackFile
    ) {
        fileBack = callBackFile
        fileChooseDialog.setData(1, MainPageService.getFileType(),
            object : FileChooseDialog.FileClick {
                override fun fileClick(fileName: String, filePath: String) {
                    val list = queryFiles(context, fileName)
                    fileChooseDialog.setData(2, list, object : FileChooseDialog.FileClick {
                        override fun fileClick(fileName: String, filePath: String) {
                            Log.e("选择了文件", fileName)
                            val file = File(filePath)
                            callBackFile.backFile(file)
                        }

                    })
                }

            })
    }

    interface CallBackFile {
        fun backFile(file: File)
    }


}