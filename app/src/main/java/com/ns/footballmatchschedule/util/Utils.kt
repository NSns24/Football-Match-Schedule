package com.ns.footballmatchschedule.util

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun toDate(format: String, date: String?) : Date {
    return SimpleDateFormat(format, Locale.getDefault()).parse(date)
}