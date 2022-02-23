package com.example.englishapp.model.data.profiledetailsresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Skills(
    val grammar: Int,
    val listening: Int,
    val pronouncing: Int,
    val vocabulary: Int
): Parcelable