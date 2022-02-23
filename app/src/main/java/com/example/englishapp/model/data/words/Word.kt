package com.example.englishapp.model.data.words

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Word(
    val alterWords: List<String>,
    val audio: String,
    val id: Int,
    val level: Int,
    val pictures: List<Pictures>,
    val repeat_timeout: Long,
    var repeated_count: Int,
    val started: Boolean,
    val translate: String,
    val word: String,
    var alpha: Float = 0f
):Parcelable{
    fun isKnown(): Boolean{
        return repeated_count >= REPEATED_COUNT_TO_KNOW_WORD
    }

    fun needRepeat(): Boolean {
        return started
    }

    val repeatTimeoutHour get() = repeat_timeout / 3600

    companion object{
        const val REPEATED_COUNT_TO_KNOW_WORD = 3
    }
}