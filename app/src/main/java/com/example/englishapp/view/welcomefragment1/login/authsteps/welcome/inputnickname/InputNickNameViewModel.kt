package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.inputnickname

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.datasource.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputNickNameViewModel @Inject constructor(val remoteDataSource: RemoteDataSource) : ViewModel(){


    fun updateProfileData(nickName:String,originalLanguage:String,avatar: String){

        viewModelScope.launch {
            try {
                remoteDataSource.updateProfileData(nickName,originalLanguage,avatar)
                Log.d("successInUpdateProfile", "successInUpdateProfile")
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("errorInUpdateProfile",e.message.toString())
            }
        }
    }



}