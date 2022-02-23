package com.example.englishapp.model.data.grammartraining

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val phrase: String,
    val translation: String,
    val matchWrongWords: String,
    val excessWords: String,
    val excessWordsLevel1: String,
    val excessWordsLevel2: String,
    val excessWordsLevel3: String,
    val type: String
):Parcelable