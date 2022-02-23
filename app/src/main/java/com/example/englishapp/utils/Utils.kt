package com.example.englishapp.utils

import android.graphics.Color
import java.lang.RuntimeException

class Utils {

    companion object {
        fun getColorFromUrl(url: String): Int? {
            val urlPart = url.substringBeforeLast(".")
            val color = urlPart.substring(IntRange(urlPart.length - 6, urlPart.length - 1))

            return try {
                Color.parseColor("#${color}")
            } catch (e: RuntimeException) {
                null
            }
        }
    }

}