package com.example.englishapp.domain

import com.example.englishapp.model.data.profiledetailsresponse.ProfileDetailsResponse
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.ResultWrapper
import com.example.englishapp.utils.ProfileRepositoryLocal
import com.example.englishapp.utils.TokenHelper
import javax.inject.Inject

class UserInteractorImpl @Inject constructor(
    private val repository: LetMeSpeakRepository,
    val tokenHelper: TokenHelper,
    val profileRepositoryLocal: ProfileRepositoryLocal
) : UserInteractor {

    override suspend fun getUser(): ProfileDetailsResponse? {
        val profile = profileRepositoryLocal.getProfile()
        val authToken = tokenHelper.readxAuthTokenLMS()
        return when {
            profile != null -> profile
            authToken != null -> {
                when (val response = repository.getDetailsProfile(authToken)) {
                    is ResultWrapper.Success -> response.value
                    is ResultWrapper.GenericError -> null
                    is ResultWrapper.NetworkError -> null
                }
            }
            else -> null
        }
    }

}