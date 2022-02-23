package com.example.englishapp.view.welcomefragment1.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.ProfileResponse
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthLoginViewModel @Inject constructor(val repository: LetMeSpeakRepository) : ViewModel() {

    val profilesLiveData = MutableLiveData<List<ProfileResponse>>()
    val errorProfileLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    var countString = "Show"

    fun login(login: String, pass: String) {
        viewModelScope.launch {
            isLoadingLiveData.value = true
            repository.getProfiles(login, pass)
                .onSuccess {
                    profilesLiveData.value = it
                }
                .onError {
                    errorProfileLiveData.value = it.error?.error_description.toString()
                }
            isLoadingLiveData.value = false
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