package com.example.englishapp.model.data.profiledetailsresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Visa(
    val active: Boolean,
    val active_since: Int,
    val expires_at: Int,
    val now: Int,
    val days_total:Int,
    val days_used:Int
): Parcelable