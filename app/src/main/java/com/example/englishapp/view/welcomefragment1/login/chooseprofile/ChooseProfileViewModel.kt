package com.example.englishapp.view.welcomefragment1.login.chooseprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.ProfileResponse
import com.example.englishapp.model.data.profiledetailsresponse.ProfileDetailsResponse
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.model.datasource.value
import com.example.englishapp.utils.Event
import com.example.englishapp.utils.ProfileRepositoryLocal
import com.example.englishapp.utils.TokenHelper
import com.example.englishapp.utils.emitEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChooseProfileViewModel @Inject constructor(
    val repository: LetMeSpeakRepository,
    val tokenHelper: TokenHelper,
    val profileRepositoryLocal: ProfileRepositoryLocal
) : ViewModel() {

    private val _detailsReadyEvent = MutableLiveData<Event<ProfileDetailsResponse>>()
    val detailsReadyEvent: LiveData<Event<ProfileDetailsResponse>> = _detailsReadyEvent

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData


    private val _errorEvent = MutableLiveData<Event<String>>()
    val errorEvent: LiveData<Event<String>> = _errorEvent

    private val _profilesLiveData = MutableLiveData<List<ProfileResponse>>()
    val getProfileLiveData: LiveData<List<ProfileResponse>> = _profilesLiveData

    fun initViewModel(profiles: List<ProfileResponse>?) {
        if (profiles != null) {
            _profilesLiveData.value = profiles!!
        } else {
            getProfiles()
        }
    }

    fun onChooseProfile(profile: ProfileResponse) {
        viewModelScope.launch {
            _isLoadingLiveData.value = true
            val authTokenResponse = repository.getToken(profile.auth_token, profile.xcToken).value
            authTokenResponse?.let {
                tokenHelper.savexcTokenLMS(profile.xcToken)
                tokenHelper.savexAuthTokenLMS(authTokenResponse.token)
                tokenHelper.saveProfileToken(profile.auth_token)
                repository.getDetailsProfile(authTokenResponse.token)
                    .onSuccess {
                        profileRepositoryLocal.saveProfile(it)
                        _detailsReadyEvent.emitEvent(it)
                    }
                    .onError {
                        _errorEvent.emitEvent(it.error?.error_description.toString())
                    }
            }
            _isLoadingLiveData.value = false

        }

    }

    private fun getProfiles() {
        viewModelScope.launch {
            _isLoadingLiveData.value = true
            repository.getProfiles(tokenHelper.readxcTokenLMS()!!)
                .onSuccess {
                    _profilesLiveData.value = it
                }
                .onError {
                    _errorEvent.emitEvent(it.error?.error_description.toString())
                }
            _isLoadingLiveData.value = false

        }
    }
}