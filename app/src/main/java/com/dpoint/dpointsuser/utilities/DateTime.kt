package com.dpoints.dpointsmerchant.utilities

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*


class DateTime {

    companion object {
        const val REGEX_MM_YY = "(0?[1-9]|[12][0-9]|3[01])[/.-](\\d\\d)"
        //const val REGEX_DD_MM_YYYY = "(0?[1-9]|1[012])[/.-](0?[1-9]|[12][0-9]|3[01])[/.-]((19|20)\\d\\d)"

        @JvmOverloads
        fun parse(
            text: String?,
            inputFormat: String = "yyyy-MM-dd",
            outputFormat: String = "dd/MM/yyyy"
        ): String {
            return try {
                text?.let {
                    val iFormatter = DateTimeFormat.forPattern(inputFormat)
                    val oFormatter = DateTimeFormat.forPattern(outputFormat)
                    oFormatter.print(iFormatter.parseDateTime(it))
                } ?: ""
            } catch (e: IllegalArgumentException) {
                ""
            }
        }

        @JvmOverloads
        fun getMonth(
            text: String,
            inputFormat: String = "yyyy-MM-dd"
        ): String {
            val date = DateTimeFormat.forPattern(inputFormat).parseDateTime(text)
            return date.toString("MMM")
        }

        fun date(
            day: Int,
            month: Int,
            year: Int,
            outputFormat: String = "dd/MM/yyyy"
        ): String {
            return try {
                val date = DateTime().withDate(year, month, day)
                val formatter = DateTimeFormat.forPattern(outputFormat)
                formatter.print(date)
            } catch (e: IllegalArgumentException) {
                ""
            }
        }

        fun stringDate(
            date: DateTime,
            outputFormat: String = "yyyy-MM-dd"
        ): String {
            return try {
                val formatter = DateTimeFormat.forPattern(outputFormat)
                formatter.print(date)
            } catch (e: IllegalArgumentException) {
                ""
            }
        }

        @JvmOverloads
        fun checkDateRange(fromDate: String?, toDate: String?, format: String = "yyyy-MM-dd"): Boolean {
            return try {
                DateTimeFormat.forPattern(format).parseDateTime(fromDate) > DateTimeFormat.forPattern(format).parseDateTime(
                    toDate
                )

            } catch (e: Exception) {
                false
            }
        }

        @JvmOverloads
        fun previousMonth(text: String?, format: String = "yyyy-MM-dd"): String {
           val date =  try {
                when {
                    text.isNullOrEmpty() -> DateTime.now()
                    else -> DateTimeFormat.forPattern(format).parseDateTime(text)
                }
            } catch (e: Exception) {
                DateTime.now()
            }
            val formatter = DateTimeFormat.forPattern(format)
            val previousMonth = date.minusMonths(1)
            return formatter.print(previousMonth)
        }
        @JvmOverloads
        fun nextMonth(text: String?, format: String = "yyyy-MM-dd"): String {
            val date =  try {
                when {
                    text.isNullOrEmpty() -> DateTime.now()
                    else -> DateTimeFormat.forPattern(format).parseDateTime(text)
                }
            } catch (e: Exception) {
                DateTime.now()
            }
            val formatter = DateTimeFormat.forPattern(format)
            val previousMonth = date.plusMonths(1)
            return formatter.print(previousMonth)
        }

        @JvmOverloads
        fun nextDate(text: String?, format: String = "yyyy-MM-dd"): String{
            val date =  try {
                when {
                    text.isNullOrEmpty() -> DateTime.now()
                    else -> DateTimeFormat.forPattern(format).parseDateTime(text)
                }
            } catch (e: Exception) {
                DateTime.now()
            }
            val formatter = DateTimeFormat.forPattern(format)
            val nextDate = date.plusDays(1)
            return formatter.print(nextDate)
        }

        @JvmOverloads
        fun maximumDays(text: String?, format: String = "yyyy-MM-dd"): Int{
            val date =  try {
                when {
                    text.isNullOrEmpty() -> DateTime.now()
                    else -> DateTimeFormat.forPattern(format).parseDateTime(text)
                }
            } catch (e: Exception) {
                DateTime.now()
            }
            return date.dayOfMonth().maximumValue;
        }

        fun getCalendar(text: String?, format: String = "yyyy-MM-dd'T'HH:mm:ss.000Z"): Calendar? {
            return try {
                val formatter = DateTimeFormat.forPattern(format)
                val dateTime = formatter.parseDateTime(text)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dateTime.millis
                return calendar
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        @JvmOverloads
        fun isValid(text: String?, format: String = "yyyy-MM-dd") = try {
            DateTimeFormat.forPattern(format).parseDateTime(text)
            true
        } catch (e: Exception) {
            false
        }

        @JvmOverloads
        fun currentDate(format: String = "dd/MM/yyyy"): String {
            val now = DateTime.now()
            val formatter = DateTimeFormat.forPattern(format)
            return formatter.print(now)
        }

        @JvmOverloads
        fun firstDayOfCurrentMonth(format: String = "dd/MM/yyyy"): String {
            val firstDay = DateTime.now().dayOfMonth().withMinimumValue()
            val formatter = DateTimeFormat.forPattern(format)
            return formatter.print(firstDay)
        }

        @JvmOverloads
        fun firstDayOfLastSixMonths(format: String = "dd/MM/yyyy"): String {
            val now = DateTime.now()
            val formatter = DateTimeFormat.forPattern(format)
            val firstDayOfLastTwelveMonths = now.minusMonths(5).dayOfMonth().withMinimumValue()
            return formatter.print(firstDayOfLastTwelveMonths)
        }

        @JvmOverloads
        fun firstDayOfMonth(str: String, format: String = "yyyy-MM-dd"): String {
            val formatter = DateTimeFormat.forPattern(format)

            val date = DateTime.parse(str, DateTimeFormat.forPattern("MM/yyyy")).dayOfMonth().withMinimumValue()
            return formatter.print(date)
        }

        @JvmOverloads
        fun lastDayOfMonth(str: String, format: String = "yyyy-MM-dd"): String {
            return try {
                val formatter = DateTimeFormat.forPattern(format)
                val date = DateTime.parse(str, DateTimeFormat.forPattern(format)).dayOfMonth().withMaximumValue()
                formatter.print(date)
            } catch (e: Exception) {
                ""
            }

        }

        @JvmOverloads
        fun lastDayOfCurrentMonth(format: String = "dd/MM/yyyy"): String {
            val lastDay = DateTime.now().dayOfMonth().withMaximumValue()
            val formatter = DateTimeFormat.forPattern(format)
            return formatter.print(lastDay)
        }

        @JvmOverloads
        fun isAfter(
            text: String?,
            format: String = "dd/MM/yyyy",
            dateTime: DateTime = DateTime.now()
        ) = try {
            val date = DateTimeFormat.forPattern(format).parseDateTime(text)
            date.isAfter(dateTime)
        } catch (e: Exception) {
            false
        }

        @JvmOverloads
        fun isBefore(
            text: String?,
            format: String = "dd/MM/yyyy",
            dateTime: DateTime = DateTime.now()
        ) = try {
            val date = DateTimeFormat.forPattern(format).parseDateTime(text)
            date.isBefore(dateTime)
        } catch (e: Exception) {
            false
        }

        @JvmOverloads
        fun date(text: String?, format: String = "yyyy-MM-dd") = try {
            when {
                text.isNullOrEmpty() -> DateTime.now()
                else -> DateTimeFormat.forPattern(format).parseDateTime(text)
            }
        } catch (e: Exception) {
            DateTime.now()
        }

        @JvmOverloads
        fun dayName(text: String?, format: String = "yyyy-MM-dd") = try {
            when {
                text.isNullOrEmpty() -> ""
                else -> DateTimeFormat.forPattern("EEEE").print(DateTimeFormat.forPattern(format).parseLocalDate(text))
            }
        } catch (e: Exception) {
            ""
        }
    }

}