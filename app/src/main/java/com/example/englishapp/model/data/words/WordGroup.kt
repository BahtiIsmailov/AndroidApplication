package com.example.englishapp.model.data.words

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WordGroup(
    val id: Int,
    val order: Int,
    val title: String,
    val wordsets: List<Wordset>?
):Parcelable