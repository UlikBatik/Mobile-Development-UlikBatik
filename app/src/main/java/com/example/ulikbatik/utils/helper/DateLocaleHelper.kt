package com.example.ulikbatik.utils.helper

import java.text.SimpleDateFormat
import java.util.Locale

object DateLocaleHelper {
    fun formatDateString(dateString: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.getDefault())

        val dateParse = inputFormat.parse(dateString)
        val formattedDate = dateParse?.let { dateFormat.format(it) }
        return formattedDate
    }
}