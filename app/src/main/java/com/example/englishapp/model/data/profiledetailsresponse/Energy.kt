package com.example.englishapp.model.data.profiledetailsresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Energy(
    val new_at: Int,
    val now: Int,
    val updated_at: Int,
    val value: Int
): Parcelable