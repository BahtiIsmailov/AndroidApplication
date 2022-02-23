package com.example.englishapp.domain

import com.example.englishapp.model.data.profiledetailsresponse.ProfileDetailsResponse

interface UserInteractor {
    suspend fun getUser(): ProfileDetailsResponse?
}