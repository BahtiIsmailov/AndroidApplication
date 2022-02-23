package com.example.englishapp.model.data.profiledetailsresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Xp(
    val current: Int,
    val next: Int,
    val start: Int
): Parcelable