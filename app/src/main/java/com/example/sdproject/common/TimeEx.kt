package com.example.sdproject.common

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

const val DATE_FORMAT_DATE_TIME_FULL_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
const val BIRTHDAY_FORMAT = "yyyy-MM-dd"

@SuppressLint("SimpleDateFormat")
fun String.convertToTime() : Long {
    val format: DateFormat = SimpleDateFormat(DATE_FORMAT_DATE_TIME_FULL_UTC)
    val date = format.parse(this)
    return date?.time ?: -1
}

@SuppressLint("SimpleDateFormat")
fun Long.convertToTime(formatStr: String = DATE_FORMAT_DATE_TIME_FULL_UTC) : String {
    val format: DateFormat = SimpleDateFormat(formatStr)
    return format.format(Date(this))
}