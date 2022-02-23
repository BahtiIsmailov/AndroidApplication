package com.example.englishapp.view.main.profile

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.profiledetailsresponse.ProfileDetailsResponse
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.value
import com.example.englishapp.utils.ProfileRepositoryLocal
import com.example.englishapp.utils.TokenHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: LetMeSpeakRepository,
    val tokenHelper: TokenHelper,
    val profileRepositoryLocal: ProfileRepositoryLocal
) : ViewModel() {

    val profileLiveData = MutableLiveData<ProfileDetailsResponse>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    private val _timetoHighEnergy = MutableLiveData<Long?>()
    val timeToHighEnergy: LiveData<Long?> = _timetoHighEnergy


    fun initViewModel() {
        val profile = profileRepositoryLocal.getProfile()
        if (profile != null) {
            profileLiveData.value = profile!!
            updateProfile()
        } else {
            isLoadingLiveData.value = true
            updateProfile()
            isLoadingLiveData.value = false
        }
    }

    private fun startTimerToRestoreEnergy(secondsToUpdate: Long) {
        _timetoHighEnergy.value = secondsToUpdate
        val timer = object : CountDownTimer(secondsToUpdate, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timetoHighEnergy.postValue(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                updateProfile()
            }
        }
        timer.start()
    }

    fun updateProfile() {
        viewModelScope.launch {
            val authToken = tokenHelper.readxAuthTokenLMS()
            if (authToken != null) {
                val profile = repository.getDetailsProfile(authToken).value
                if (profile != null) {
                    profileRepositoryLocal.saveProfile(profile)
                    profileLiveData.value = profile!!

                    val diferentTime = (profile.energy.new_at - profile.energy.now).toLong()

                    if (diferentTime > 0) {
                        startTimerToRestoreEnergy(diferentTime * 1000)
                    } else {
                        _timetoHighEnergy.value = null
                    }
                }
            }
        }

    }


}