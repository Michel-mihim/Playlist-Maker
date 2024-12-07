package com.practicum.playlistmaker.utils.converters

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Date

fun dimensionsFloatToIntConvert(dim: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dim,
        context.resources.displayMetrics
    ).toInt()
}

fun isoDateToYearConvert(isoDate: String): String {
    var year: String
    val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val targetDateFormat = SimpleDateFormat("yyyy")
    try {
        val date: Date = isoDateFormat.parse(isoDate)
        year = targetDateFormat.format(date)
    } catch (e: Exception) {
        year = ""
    }
    return year
}