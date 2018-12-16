package com.cryptopia.android.pPeterle.domain

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*

class XFormater: IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"))
        calendar.time = Date((value * 1000).toLong())
        val hour = calendar.get(Calendar.HOUR)
        return hour.toString()
    }
}