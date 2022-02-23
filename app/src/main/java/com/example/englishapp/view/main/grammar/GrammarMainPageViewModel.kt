package com.example.englishapp.view.main.grammar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrammarMainPageViewModel @Inject constructor(
    private val repository: LetMeSpeakRepository
) : ViewModel() {

    private val _getGrammarInfoForMainPageLiveData =
        MutableLiveData<UIState<List<GrammarMainData>>>()
    val getGrammarInfoForMainPageLiveData: LiveData<UIState<List<GrammarMainData>>> =
        _getGrammarInfoForMainPageLiveData


    fun getGrammarInfo() {
        viewModelScope.launch {
            _getGrammarInfoForMainPageLiveData.value = UIState.Loading
            repository.getGrammarInfo("en", "ru")
                .onSuccess {
                    _getGrammarInfoForMainPageLiveData.value = UIState.Success(it)
                }
                .onError {
                    _getGrammarInfoForMainPageLiveData.value =
                        UIState.Error(it.error?.error_description.toString())
                }

        }
    }

}