package com.example.englishapp.model.data.createauthprofile

import androidx.annotation.DrawableRes

data class Motivation(
    @DrawableRes val images: Int,
    var topic : String,
    val description : String
)
