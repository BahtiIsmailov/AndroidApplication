package com.example.englishapp.view.main.grammar.training.rebus

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.*
import com.example.englishapp.R
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.utils.*
import com.skydoves.balloon.iconForm
import kotlinx.coroutines.flow.MutableStateFlow


class RebusGrammarTrainingViewModel : ViewModel() {

    var originalPhrase: List<String> = emptyList()
    var question: Question? = null

    private val _userWritedWordsLiveData = MutableStateFlow<List<String>>(emptyList())
    val userWritedWordsLiveData: LiveData<List<String>> = _userWritedWordsLiveData.asLiveData()

    private val _allPhraseWordsLiveData = MutableLiveData<List<String>>(emptyList())
    val allPhraseWordsLiveData: LiveData<List<String>> = _allPhraseWordsLiveData

    private val _translationPhraseLiveData = MutableLiveData<String>()
    val translationPhraseLiveData: LiveData<String> = _translationPhraseLiveData

    private val _successChooseWordEvent = NoParamsMutableEvent()
    val succesChooseWordEvent: NoParamsEvent = _successChooseWordEvent


    data class ErrorParams(
        val question: Question,
        val originalPhrase: String,
        val errorPhrase: String
    )

    private val _errorChooseWordEvent = MutableLiveData<Event<ErrorParams>>()
    val errorChooseWordEvent: LiveData<Event<ErrorParams>> = _errorChooseWordEvent


    val showDeleteWordButtonLiveData = userWritedWordsLiveData.map {
        it.isNotEmpty()
    }


    fun userChooseWord(word: String) {
        val newList = _userWritedWordsLiveData.value + word
        _userWritedWordsLiveData.value = newList
        _allPhraseWordsLiveData.value = _allPhraseWordsLiveData.value
        val resultOriginalWord = originalPhrase.map {
            replaceOtherLetter(it)
        }
        if (resultOriginalWord == newList) {
            _successChooseWordEvent.emitEvent()
        } else if (!_allPhraseWordsLiveData.value!!.startsWith(_userWritedWordsLiveData.value) &&
            ((word.endsWith(".") || word.endsWith("!") || word.endsWith("?")))
        ) {
            _errorChooseWordEvent.emitEvent(
                ErrorParams(
                    question!!,
                    originalPhrase.joinToString(" "),
                    _userWritedWordsLiveData.value.joinToString(" ")
                )
            )
        }
    }

    fun initCurrentViewModel(question: Question, level: String) {
        this.question = question
        originalPhrase = question.phrase.replace("_", " ").split(" ")
        val shuffledOriginalPhrase = originalPhrase.shuffled()
        _allPhraseWordsLiveData.value = (shuffledOriginalPhrase + when (level) {
            "" -> fromStringToArray(question.excessWordsLevel1)
            "1" -> fromStringToArray(question.excessWordsLevel2)
            "2" -> fromStringToArray(question.excessWordsLevel3)
            else -> fromStringToArray(question.excessWordsLevel3)
        }).shuffled()
        _translationPhraseLiveData.value = question.translation
    }

    fun pushButtonDelete() {
        _userWritedWordsLiveData.value =
            _userWritedWordsLiveData.value.subList(0, _userWritedWordsLiveData.value.lastIndex)
        _allPhraseWordsLiveData.value = _allPhraseWordsLiveData.value
    }

    fun replaceOtherLetter(word: String): String {
        return word.replace("_", " ").replace("{", "").replace("}", "")
    }

    fun fromStringToArray(word: String): List<String> {
        return word
            .split(",")
            .filter { it != "" }
            .map { //clear whitespaces at start and end
                it.filterIndexed { index, c ->
                    when (index) {
                        0 -> {
                            !c.isWhitespace()
                        }
                        it.length - 1 -> {
                            !c.isWhitespace()
                        }
                        else -> true
                    }
                }
            }
    }


}