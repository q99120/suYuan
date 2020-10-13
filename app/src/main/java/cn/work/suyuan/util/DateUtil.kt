/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.work.suyuan.util

import android.content.Context
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.configure.PickerOptions
import kotlinx.android.synthetic.main.fragment_home_child.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间和日期工具类。
 *
 * @author guolin
 * @since 17/6/30
 */
object DateUtil {

    const val MINUTE = (60 * 1000).toLong()

    const val HOUR = 60 * MINUTE

    const val DAY = 24 * HOUR

    const val WEEK = 7 * DAY

    const val MONTH = 4 * WEEK

    const val YEAR = 365 * DAY

    /**
     * 根据传入的Unix时间戳，获取转换过后更加易读的时间格式。
     * @param dateMillis
     * Unix时间戳
     * @return 转换过后的时间格式，如2分钟前，1小时前。
     */


    fun isBlockedForever(timeLeft: Long) = timeLeft > 5 * YEAR

    fun getDate(dateMillis: Long, pattern: String = "yyyy-MM-dd"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }

     fun getDateAndTime(dateMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }

    private fun getDateAndHourMinuteTime(dateMillis: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }

    lateinit var  dateClick :ChooseDate
    lateinit var  pickArray:BooleanArray
    fun showDate(context: Context,isHour:Boolean,param:ChooseDate){
        dateClick = param
        pickArray = if (isHour) booleanArrayOf(true,true,true,true,true,false) else booleanArrayOf(true,true,true,false,false,false)
        val pickerView =  TimePickerBuilder(context)
        { date, v ->
            if (isHour)  dateClick.getTime(getDateAndTime(date.time)) else  dateClick.getTime(getDate(date.time))
        }.isCenterLabel(true).setType(pickArray).build()
        pickerView.show()
    }

    interface ChooseDate{
           fun getTime(result:String)
    }

}
