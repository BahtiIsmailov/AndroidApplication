package com.example.englishapp.model.data

import android.os.Parcelable
import com.example.englishapp.model.data.profiledetailsresponse.Energy
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResponse(
    val address: String,
    val nickname: String,
    @SerializedName("rarity_title") val rarityTitle: String?,
    @SerializedName("rarity_badge_color") val rarityBadgeColor: String?,
    val rarity:Int,
    val visa_days_total:Int,
    val visa_days_left:Int,
    val avatar: String,
    val avatar_url: String,
    val auth_token: String,
    val number: String?,
    val energy: Int,
    val level: Int,
    @SerializedName("token") val xcToken: String
): Parcelable