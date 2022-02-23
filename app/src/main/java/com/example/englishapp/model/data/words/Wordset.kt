package com.example.englishapp.model.data.words

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wordset(
    val collection_id: Int,
    val id: Int,
    val is_active: Boolean,
    val last_time_repeated: Int,
    val link_pic: String,
    val order: Int,
    val percent_completed: Int,
    val stars: Int,
    val title: String,
    val topic: String,
    val words_active_count: Int,
    val words_count: Int,
    val words_new_count: Int,
    val words_ready_count: Int,
    val words_repeated_count: Int,
    val words_started_count: Int
) : Parcelable