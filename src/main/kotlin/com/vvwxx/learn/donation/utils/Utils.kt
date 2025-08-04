package com.vvwxx.learn.donation.utils

import java.text.NumberFormat
import java.util.Locale

object Utils {

    fun formatRupiah(amount: Long?): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(amount)
    }
}