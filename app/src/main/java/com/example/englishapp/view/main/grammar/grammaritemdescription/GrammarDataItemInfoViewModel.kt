package com.example.englishapp.view.main.grammar.grammaritemdescription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.grammar.GrammarDataItemInfo
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrammarDataItemInfoViewModel @Inject constructor(
    private val repository: LetMeSpeakRepository
) : ViewModel() {

    private val _getGrammarItemInfoLiveData = MutableLiveData<UIState<GrammarDataItemInfo>>()
    val getGrammarItemInfoLiveData: LiveData<UIState<GrammarDataItemInfo>> =
        _getGrammarItemInfoLiveData

    fun getGrammarDataIndo(url: String) {
        viewModelScope.launch {
            _getGrammarItemInfoLiveData.value = UIState.Loading
            repository.getGrammarItemInfo(url)
                .onSuccess {
                    _getGrammarItemInfoLiveData.value = UIState.Success(it)
                }
                .onError {
                    _getGrammarItemInfoLiveData.value =
                        UIState.Error(it.error?.error_description.toString())
                }

        }
    }
}