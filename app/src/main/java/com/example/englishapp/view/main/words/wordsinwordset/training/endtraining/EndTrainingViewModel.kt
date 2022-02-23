package com.example.englishapp.view.main.words.wordsinwordset.training.endtraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.training.EndTrainingWordSetResponse
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.utils.ProfileRepositoryLocal
import com.example.englishapp.utils.emit
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndTrainingViewModel @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val repository: LetMeSpeakRepository,
    val profileRepositoryLocal: ProfileRepositoryLocal
) :
    ViewModel() {

    data class TrainingWordsResult(
        val allWordsIsKnown: Boolean,
        val progress: Int,
        val trainedWords: List<Word>
    )

    private val _endTrainingInfoLiveData = MutableLiveData<UIState<EndTrainingWordSetResponse>>()
    val endTrainingInfoLiveData: LiveData<UIState<EndTrainingWordSetResponse>> =
        _endTrainingInfoLiveData

    private val _trainingWordsResultLiveData = MutableLiveData<UIState<TrainingWordsResult>>()
    val trainingWordsResultLiveData: LiveData<UIState<TrainingWordsResult>> =
        _trainingWordsResultLiveData


    fun getEndTrainingInfo(
        wordsIds: String,
        total: String,
        error: String,
        trainedWords: List<Word>,
        wordSetId: Int
    ) {
        viewModelScope.launch {

            remoteDataSource.clearWordsCache()
            profileRepositoryLocal.clearProfile()

            _endTrainingInfoLiveData.value = UIState.Loading

            repository.getEndTrainingInfo(wordsIds, total, error)
                .onSuccess {
                    _endTrainingInfoLiveData.value = UIState.Success(it)
                }
                .onError {
                    _endTrainingInfoLiveData.value =
                        UIState.Error(it.error?.error_description.toString())
                }

            _trainingWordsResultLiveData.value = UIState.Loading

            repository.getWordsByWordSetId("ru", "en", wordSetId, false)
                .onSuccess { wordsResponse ->
                    val trainedWordsNewInfo = wordsResponse.words.filter { word ->
                        trainedWords.find { it.id == word.id } != null
                    }
                    val allWordsCount = wordsResponse.words.size
                    val trainedWordsCount = wordsResponse.words.count { it.isKnown() }
                    val result = (100 * trainedWordsCount) / allWordsCount
                    _trainingWordsResultLiveData.emit(
                        UIState.Success(
                            TrainingWordsResult(
                                trainedWordsCount == allWordsCount,
                                result,
                                if (trainedWordsCount == allWordsCount) {
                                    wordsResponse.words
                                } else {
                                    trainedWordsNewInfo
                                }
                            )
                        )
                    )

                }
                .onError {
                    _trainingWordsResultLiveData.emit(
                        UIState.Error(
                            it.error?.error_description ?: "Server is not responding"
                        )
                    )
                }

        }
    }

}