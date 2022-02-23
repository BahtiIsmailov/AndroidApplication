package com.example.englishapp.model.data.grammartraining.endgrammartraining

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopPlayerDto(
    val avatar: String,
    val avatar_url: String,
    val date: String,
    val lang: String,
    val nickname: String,
    val place: Int,
    val time: Double,
    val type: String
): Parcelable