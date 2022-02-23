package com.example.englishapp.view.welcomefragment1.login.authsteps

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.createauthprofile.CreateAuthProfile
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.utils.TokenHelper
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateEmailAndPasswordViewModel @Inject constructor(
    val repository: LetMeSpeakRepository,
    val remoteDataSource: RemoteDataSource,
    val tokenHelper: TokenHelper) :
    ViewModel() {
    var countString = "Show"

    private val _firstRequestGetTokenLiveData = MutableLiveData<UIState<CreateAuthProfile>>()
    val firstRequestGetTokenLiveData: LiveData<UIState<CreateAuthProfile>> =
        _firstRequestGetTokenLiveData

    private val _getProfilesAfterCreatedAuthPersonLiveData = MutableLiveData<UIState<Unit>>()
    val getProfilesAfterCreatedAuthPersonLiveData: LiveData<UIState<Unit>> =
        _getProfilesAfterCreatedAuthPersonLiveData

    fun createAuthProfile(login: String, pass: String) {
        _firstRequestGetTokenLiveData.value = UIState.Loading
        try {
            viewModelScope.launch {
                val result = remoteDataSource.createAuthPerson(login, pass)
                _firstRequestGetTokenLiveData.value = UIState.Success(result)
                tokenHelper.savexcTokenLMS(result.ctoken)
                Log.d("resultCtoken",result.toString())
            }
        } catch (e: Exception) {
            _firstRequestGetTokenLiveData.value = UIState.Error(e.message.toString())
        }

    }

    fun getProfilesAfterCreatedAuthPerson(ctoken: String) {
        _getProfilesAfterCreatedAuthPersonLiveData.value = UIState.Loading
        viewModelScope.launch {
            repository.getProfilesAfterCreatedAuthPerson(ctoken)
                .onSuccess {
                    _getProfilesAfterCreatedAuthPersonLiveData.value = UIState.Success(it)
                }
                .onError {
                    _getProfilesAfterCreatedAuthPersonLiveData.value =
                        UIState.Error(it.error?.error_description.toString())
                }
        }

    }


    fun showPassword(editText: EditText) {
        if (this.countString == "Show") {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            this.countString = "Hide"
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            this.countString = "Show"
        }
    }
}