package com.example.englishapp.view.main.grammar.endtraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.grammartraining.endgrammartraining.EndGrammarData
import com.example.englishapp.model.data.grammartraining.endgrammartraining.GetStatisticFromOtherPerson
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EndTrainingViewModel @Inject constructor(val repository: LetMeSpeakRepository) :
    ViewModel() {

    private val _endTrainingGrammarInfoWithRewardsLiveData =
        MutableLiveData<UIState<EndGrammarData>>()
    val endTrainingGrammarInfoWithRewardsLiveData: LiveData<UIState<EndGrammarData>> =
        _endTrainingGrammarInfoWithRewardsLiveData

    private val _getStatisticFromOtherPersonLiveData =
        MutableLiveData<UIState<GetStatisticFromOtherPerson>>(UIState.Loading)
    val getStatisticFromOtherPersonLiveData: LiveData<UIState<GetStatisticFromOtherPerson>> =
        _getStatisticFromOtherPersonLiveData


    fun endGrammarTrainingInfo(gameId: String, id: String, result: String, value: String) {
        viewModelScope.launch {
            repository.endGrammarTraining(
                game_id = gameId,
                id = id,
                result = result,
                value = value
            ).onSuccess {
                _endTrainingGrammarInfoWithRewardsLiveData.value = UIState.Success(it)
            }.onError {
                _endTrainingGrammarInfoWithRewardsLiveData.value =
                    UIState.Error(it.error?.error_description ?: "")
            }
        }
    }


    fun getStatisticFromOtherPerson(id: Int) {
        _getStatisticFromOtherPersonLiveData.value = UIState.Loading

        viewModelScope.launch {
            repository.getPersonStatistic(id)
                .onSuccess {
                    _getStatisticFromOtherPersonLiveData.value = UIState.Success(it)
                }.onError {
                    _getStatisticFromOtherPersonLiveData.value =
                        UIState.Error(it.error?.error_description ?: "")
                }
        }
    }

}