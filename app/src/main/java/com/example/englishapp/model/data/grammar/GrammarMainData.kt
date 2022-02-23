package com.example.englishapp.model.data.grammar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GrammarMainData(
    val available: Boolean,
    val code: String,
    val google: Boolean,
    val grammarLink: String,
    val header: String,
    val isNew: Boolean,
    val name: String,
    val open_after: Int,
    val stars: Int,
    val tags: List<String>
):Parcelable