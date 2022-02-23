package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooselanguage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.createauthprofile.ChooseLanguage
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.utils.TokenHelper
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class ChooseLanguageViewModel @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val tokenHelper: TokenHelper) : ViewModel() {


    private val _languagesInfo = MutableLiveData<UIState<List<ChooseLanguage>>>()
    val languagesInfo: LiveData<UIState<List<ChooseLanguage>>> = _languagesInfo



    fun getLanguages(){
        viewModelScope.launch {
            _languagesInfo.value = UIState.Loading
            try {
                val result = remoteDataSource.getLanguages()
                _languagesInfo.value = UIState.Success(result)
            } catch (e: Exception) {
                _languagesInfo.value = UIState.Error(e.message.toString())
            }
        }
    }
    fun getTokensForWelcome(){
        viewModelScope.launch {
            try {
                val response = remoteDataSource.getTokenForWelcome()
                tokenHelper.saveProfileToken(response.token)
                val authTokenResponse = remoteDataSource.getToken(response.token, tokenHelper.readxcTokenLMS()!!)
                tokenHelper.savexAuthTokenLMS(authTokenResponse.token)

            }catch (e:Exception){
                e.printStackTrace()
                Log.d("newtoken",e.message.toString())
            }
        }
    }

}