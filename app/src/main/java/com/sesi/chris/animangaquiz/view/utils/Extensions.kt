package com.sesi.chris.animangaquiz.view.utils

import java.math.BigDecimal
import java.text.DecimalFormat

class Extensions {
    private val decimalFormat = DecimalFormat("'$'###,###,##0")//DecimalFormat("'$'###,###,##0.00")
    fun BigDecimal.toFormat(): String {
        return decimalFormat.format(BigDecimal(this.toPlainString()))
    }
}