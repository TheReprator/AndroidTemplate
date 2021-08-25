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

package reprator.androidtemplate.implementation

import app.template.base.util.date.DateUtils
import app.template.base.util.date.DateUtils.Companion.EPOCH
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtilsImpl @Inject constructor() : DateUtils {

    companion object {
        private const val EMPTY_TIME = ""
        private const val TIMEZONE_UTC_STRING = "UTC"
    }

    override fun getTimeZone(offset: Int): TimeZone {
        val utcTimeZone = TimeZone.getTimeZone(TIMEZONE_UTC_STRING)
        val ids = TimeZone.getAvailableIDs(offset)
        if (ids.isNullOrEmpty())
            return utcTimeZone

        val matchingZoneId = ids[0]
        return TimeZone.getTimeZone(matchingZoneId)
    }

    private fun getStringFormat(time: Date?, @DateUtils.DateFormat format: String): String {
        if (time == null) {
            return EMPTY_TIME
        }
        val dateFormat = getSimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(time)
    }

    private fun parseDate(date: String, @DateUtils.DateFormat format: String): Date? {
        val simpleDateFormat = getSimpleDateFormat(format, Locale.getDefault())
        try {
            return simpleDateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getSimpleDateFormat(
        @DateUtils.DateFormat dateFormat: String,
        locale: Locale = Locale.getDefault()
    ):
        SimpleDateFormat {
            return SimpleDateFormat(dateFormat, locale)
        }

    override fun convertToEpoch(time: Long): Long = time * EPOCH

    override fun format(time: Long, @DateUtils.DateFormat dateFormat: String): String {
        return getStringFormat(Date(time), dateFormat)
    }

    override fun format(
        time: Long,
        @DateUtils.DateFormat dateFormat: String,
        timeZone: TimeZone
    ): String {
        val df = getSimpleDateFormat(dateFormat)
        df.timeZone = timeZone

        return df.format(Date(time))
    }

    override fun format(date: Date, @DateUtils.DateFormat dateFormat: String): String {
        return getStringFormat(date, dateFormat)
    }

    override fun format(
        date: String,
        @DateUtils.DateFormat fromDateFormat: String,
        @DateUtils.DateFormat toDateFormat: String
    ): String {
        val dateParsed = parse(date, fromDateFormat)
        return getStringFormat(dateParsed, toDateFormat)
    }

    override fun format(
        stringDate: String,
        @DateUtils.DateFormat fromDateFormat: String,
        @DateUtils.DateFormat toDateFormat: String,
        timeZone: String
    ): String {
        val date = parse(stringDate, fromDateFormat)

        val tm = TimeZone.getTimeZone(timeZone)
        val offset = tm.rawOffset + tm.dstSavings
        val nTime = date!!.time + offset

        return getStringFormat(Date(nTime), toDateFormat)
    }

    override fun parse(date: String, @DateUtils.DateFormat dateFormat: String): Date? {
        return parseDate(date, dateFormat)
    }

    override fun parse(date: String, dateFormat: String, timeZone: TimeZone): Date? {
        val df = getSimpleDateFormat(dateFormat)
        df.timeZone = timeZone

        try {
            return df.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    override fun getDifferenceDays(d1: Date, d2: Date): Long {
        val diff = d2.time - d1.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    override fun getCalendar(): Calendar {
        return Calendar.getInstance()
    }

    override fun getFieldFromCalendar(date: Date, fieldName: Int): Int {
        val cal = getCalendar()
        cal.time = date
        return cal[fieldName]
    }
}
