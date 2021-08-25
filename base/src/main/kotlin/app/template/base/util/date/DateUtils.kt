/*
 * Copyright 2021 Vikram LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.template.base.util.date

import androidx.annotation.StringDef
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

interface DateUtils {

    companion object {
        const val DAY = "EEE"
        const val HOUR_24 = "HH"
        const val HOUR = "hh"
        const val SECONDS = "ss"
        const val MINUTES = "mm"
        const val DAY_IN_MONTH = "dd"
        const val MONTH_NUMBER = "MM"
        const val YEAR = "yy"
        const val YEAR_FULL = "yyy"
        const val MONTH_3CHAR = "MMM"
        const val AM_PM = "a"

        const val HOUR_MINUTE = "hh:mm a"
        const val HOUR_MINUTE_SECONDS = "hh:mm:ss a"
        const val HOUR_MINUTE_SECONDS_24 = "HH:mm:ss"
        const val DD_MMM_YYYY = "dd-MMM-yyyy"
        const val DD_MMM_YYYY_HOUR_MINUTE_SECONDS = "dd-MMM-yyyy hh:mm:ss a"
        const val DD_H_MMM_H_YYYY_HOUR_MINUTE_SECONDS = "dd/MMM/yyyy hh:mm:ss a"

        const val EPOCH = 1000
    }

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(HOUR_MINUTE, HOUR_MINUTE_SECONDS, HOUR_MINUTE_SECONDS_24, DD_MMM_YYYY)
    annotation class DateFormat

    fun convertToEpoch(time: Long): Long

    fun getTimeZone(offset: Int): TimeZone

    fun format(time: Long, @DateFormat dateFormat: String): String

    fun format(time: Long, @DateFormat dateFormat: String, timeZone: TimeZone): String

    fun format(date: Date, @DateFormat dateFormat: String): String

    fun format(date: String, @DateFormat fromDateFormat: String, @DateFormat toDateFormat: String): String

    fun format(
        stringDate: String,
        @DateFormat fromDateFormat: String,
        @DateFormat toDateFormat: String,
        timeZone: String
    ): String

    fun parse(date: String, @DateFormat dateFormat: String): Date?
    fun parse(date: String, @DateFormat dateFormat: String, timeZone: TimeZone): Date?

    fun getDifferenceDays(d1: Date, d2: Date): Long

    fun getCalendar(): Calendar

    fun getFieldFromCalendar(date: Date, fieldName: Int): Int
}
