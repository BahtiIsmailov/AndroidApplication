package com.example.englishapp.view.main.words.wordslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.Wordset
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.utils.Event
import com.example.englishapp.utils.emit
import com.example.englishapp.utils.emitEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WordsViewModel @Inject constructor(private val repository: LetMeSpeakRepository) :
    ViewModel() {

    private val _lookAllClickEvent = MutableLiveData<Event<WordGroup>>()
    val lookAllClickEvent: LiveData<Event<WordGroup>> = _lookAllClickEvent

    private val _uiStateLiveData = MutableLiveData<WordsUiState>()
    val uiStateLiveData: LiveData<WordsUiState> = _uiStateLiveData

    init {
        viewModelScope.launch {
            _uiStateLiveData.emit(WordsUiState.Loading)
        }

        getWordsGroups()
    }

    private fun getWordsGroups() {
        viewModelScope.launch {
            repository.getWordsGroups("ru", "en")
                .onSuccess { words ->
                val lastWordSet = findLastCard(words)
                val wordsWithLastCard = words.toMutableList()
                wordsWithLastCard.add(0, generateFakeWordGroup())
                wordsWithLastCard.add(1, WordGroup(1, 1, "Изучаю", findWordSetsInProgress(words)))
                _uiStateLiveData.emit(WordsUiState.Data(wordsWithLastCard, lastWordSet))
            }

        }
    }

    private fun findLastCard(words: List<WordGroup>): Wordset? {
        words.forEach { wordGroup ->
            val wordSet = wordGroup.wordsets?.find { it.percent_completed < 100 }
            if (wordSet != null) return wordSet
        }
        return null
    }

    private fun findWordSetsInProgress(words: List<WordGroup>): List<Wordset> {

        val activeWords = words.flatMap { it.wordsets ?: listOf() }
            .filter { it.is_active }

        val sortedWordByRepeatedCount = activeWords
            .filter { it.words_repeated_count > 0 }
            .sortedByDescending { it.words_repeated_count }

        val sortedByTimestamp = activeWords
            .filter { it.words_repeated_count == 0 }
            .sortedByDescending { it.last_time_repeated }

        return sortedWordByRepeatedCount + sortedByTimestamp
    }

    private fun generateFakeWordGroup(): WordGroup {
        return WordGroup(
            Random(100).nextInt(),
            Random(100).nextInt(),
            "Fake title",
            listOf()
        )
    }


    fun onLookAllClick(wordGroup: WordGroup) {
        _lookAllClickEvent.emitEvent(wordGroup)
    }

}