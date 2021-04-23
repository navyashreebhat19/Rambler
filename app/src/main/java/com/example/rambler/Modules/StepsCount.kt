package com.example.rambler.Modules

import com.example.rambler.Modules.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

data class StepCount(val date: Date, val steps: Int) {
    private val sdf = SimpleDateFormat(DATE_FORMAT)

    fun getDateFormatted() = sdf.format(date)
}