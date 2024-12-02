package com.practicum.playlistmaker.utils.converters

import android.content.Context
import android.util.TypedValue

fun dimensionsFloatToIntConvert(dim: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dim,
        context.resources.displayMetrics
    ).toInt()
}