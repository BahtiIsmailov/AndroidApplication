package com.example.englishapp.model.data.profiledetailsresponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileDetailsResponse(
    @SerializedName("avatar_url") val avatarUrl: String,
    val character_id: String,
    val currency_rewards: Double,
    val energy: Energy,
    val invited_list: List<Invited>,
    val invites: Int,
    val is_crypto: Boolean,
    val language_level: String,
    val learning_speed: Int,
    val level: Int,
    val name: String,
    val number: Int,
    val origin_language: String,
    val rarity: Int,
    val skills: Skills,
    val stars: Int,
    val talent: Int,
    val total_stars: Double,
    val visa: Visa,
    val xp: Xp,
    val rarity_title: String,
    val rarity_badge_color: String
): Parcelable
