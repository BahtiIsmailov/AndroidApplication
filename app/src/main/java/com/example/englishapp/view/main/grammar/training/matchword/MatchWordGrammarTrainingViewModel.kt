package com.example.englishapp.view.main.grammar.training.matchword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.englishapp.R
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.utils.Event
import com.example.englishapp.utils.NoParamsEvent
import com.example.englishapp.utils.NoParamsMutableEvent
import com.example.englishapp.utils.emitEvent
import com.example.englishapp.view.main.grammar.training.rebus.RebusGrammarTrainingViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class MatchWordGrammarTrainingViewModel : ViewModel() {

    var question: Question? = null


    private val _orginalPhraseLiveData = MutableLiveData<String>()
    val orginalPhraseLiveData: LiveData<String> = _orginalPhraseLiveData

    private val _translationPhraseLiveData = MutableLiveData<String>()
    val translationPhraseLiveData: LiveData<String> = _translationPhraseLiveData

    private val _allVariantsWordsLiveData = MutableLiveData<List<String>>()
    val allVariantsWordsLiveData: LiveData<List<String>> = _allVariantsWordsLiveData

    private val _successChooseWordEvent = NoParamsMutableEvent()
    val successChooseWordEvent: NoParamsEvent = _successChooseWordEvent

    private val _errorChooseWordEvent = MutableLiveData<Event<ErrorParams>>()
    val errorChooseWordEvent: LiveData<Event<ErrorParams>> = _errorChooseWordEvent

    var wordWithBrackets: String? = null

    data class ErrorParams(
        val question: Question,
        val originalPhrase: String,
        val errorPhrase: String
    )

    fun initializeViewModel(question: Question) {
        this.question = question
        _translationPhraseLiveData.value = question.translation.replace("_", " ")
        val  questionPhrase = question.phrase.replace("_", " ")

        val allVariantsWords = question.matchWrongWords.split(",")

       wordWithBrackets = parseWordWithBrackets(questionPhrase)

        wordWithBrackets?.let {
            _allVariantsWordsLiveData.value =
                allVariantsWords + it
        }

        _orginalPhraseLiveData.value = questionPhrase.replace("\\{(.*?)\\}".toRegex(),
            "___")
    }

    fun userPressOnWhiteButton(word: String) {
        if (word == wordWithBrackets)
            _successChooseWordEvent.emitEvent()
        else {
            val wordForReplace = question!!.phrase.split(" ").find {
                it.startsWith("{") && it.endsWith("}")
            } ?: ""
            val resultPhrase = question?.phrase?.replace(wordForReplace, word) ?: ""

            _errorChooseWordEvent.emitEvent(
                ErrorParams(
                    question!!,
                    question!!.phrase.replace("{", "").replace("}", ""),
                    resultPhrase
                )
            )
        }
    }

    private fun parseWordWithBrackets(listWithBrackets: String): String? {
        val p: Pattern = Pattern.compile("\\{(.*?)\\}")
        val m: Matcher = p.matcher(listWithBrackets)
        while (m.find()) {
            wordWithBrackets = m.group(1)
        }
        return wordWithBrackets?.replace("{","")?.replace("}","")
    }


}