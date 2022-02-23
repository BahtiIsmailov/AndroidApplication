package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startcheckvocabulary.trainingwordinwelcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.createauthprofile.DataForTrainingWelcome
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.utils.TokenHelper
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class TrainingWordInWelcomeViewModel @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val repository: LetMeSpeakRepository,
    val tokenHelper: TokenHelper
) : ViewModel() {


    private val _getDataForTrainingLiveData =
        MutableLiveData<UIState<List<DataForTrainingWelcome>>>()
    val getDataForTrainingLiveData: LiveData<UIState<List<DataForTrainingWelcome>>> =
        _getDataForTrainingLiveData


    fun getDataForTrainingInWelcome(
        teamsId: String,
        originalLanguage: String,
        translationLanguage: String
    ) {
        viewModelScope.launch {
            _getDataForTrainingLiveData.value = UIState.Loading

            repository.getDataForTrainingInWelcome(
                teamsId,
                originalLanguage,
                translationLanguage
            )
                .onSuccess {
                    _getDataForTrainingLiveData.value = UIState.Success(it)
                }
                .onError {
                    _getDataForTrainingLiveData.value =
                        UIState.Error(it.error?.error_description.toString())
                }
        }
    }
}