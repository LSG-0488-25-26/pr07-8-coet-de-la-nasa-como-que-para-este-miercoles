package com.example.umafacts.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.graphics.toColorInt

fun formatBirthday(day: Int?, month: Int?): String {
    // Check if values are null or invalid
    if (day == null || month == null || day <= 0 || month <= 0 || month > 12) {
        return "Unknown"
    }

    // Validate day based on month
    val maxDays = when (month) {
        2 -> 29 // February - considering leap years
        4, 6, 9, 11 -> 30
        else -> 31
    }

    if (day > maxDays) {
        return "Unknown"
    }

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, day)

    if (calendar.get(Calendar.DAY_OF_MONTH) % 10 == 1 && calendar.get(Calendar.DAY_OF_MONTH) != 11 ) {
        val dateFormat = SimpleDateFormat("d'st of' MMMM", Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    } else if (calendar.get(Calendar.DAY_OF_MONTH) % 10 == 2 && calendar.get(Calendar.DAY_OF_MONTH) != 12) {
        val dateFormat = SimpleDateFormat("d'nd of' MMMM", Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    } else if (calendar.get(Calendar.DAY_OF_MONTH) % 10 == 3 && calendar.get(Calendar.DAY_OF_MONTH) != 13) {
        val dateFormat = SimpleDateFormat("d'rd of' MMMM", Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    }
    val dateFormat = SimpleDateFormat("d'th of' MMMM", Locale.ENGLISH)
    return dateFormat.format(calendar.time)
}

fun parseColor(colorString: String): androidx.compose.ui.graphics.Color {
    return try {
        // FIX: Check if string is null or empty before parsing
        if (colorString.isNullOrBlank()) {
            androidx.compose.ui.graphics.Color.Gray
        } else {
            androidx.compose.ui.graphics.Color(colorString.toColorInt())
        }
    } catch (e: Exception) {
        androidx.compose.ui.graphics.Color.Gray
    }
}

/**
 * Calculates whether Black or White text is more legible on the given background.
 */
@Composable
fun textColorFor(backgroundColor: Color): Color {
    // 0.5 is the standard threshold.
    // If luminance is high (bright), use black text.
    return if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White
}