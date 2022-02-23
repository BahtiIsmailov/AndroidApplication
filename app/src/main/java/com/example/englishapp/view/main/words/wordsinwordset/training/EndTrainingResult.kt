package com.example.englishapp.view.main.words.wordsinwordset.training

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

@Parcelize
data class EndTrainingResult(
    val wordsIds: String,
    val total: String,
    val error: String
):Parcelable