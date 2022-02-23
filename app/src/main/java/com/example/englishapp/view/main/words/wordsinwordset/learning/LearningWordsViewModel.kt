package com.example.englishapp.view.main.words.wordsinwordset.learning

import androidx.lifecycle.*
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.model.data.words.WordsResponse
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.utils.Event
import com.example.englishapp.utils.emitEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningWordsViewModel @Inject constructor(val repository: LetMeSpeakRepository) :
    ViewModel() {
    companion object {
        const val WORDS_TO_TRAINING_COUNT = 4
    }

    private val _learningisCompleteEvent = MutableLiveData<Event<List<Word>>>()
    val learningisCompleteEvent: LiveData<Event<List<Word>>> = _learningisCompleteEvent

    private val _currentWordIndexLiveData = MutableStateFlow(0)
    val currentWordIndexLiveData: LiveData<Int> = _currentWordIndexLiveData.asLiveData()

    private val _wordsLiveData = MutableStateFlow<List<Word>>(emptyList())//30
    val wordsLiveData: LiveData<List<Word>> = _wordsLiveData.asLiveData()

    private val _leftButtonIsVisibleliveData = MutableLiveData<Boolean>()
    val leftButtonIsVisibleliveData: LiveData<Boolean> = _leftButtonIsVisibleliveData

    private val _rightButtonIsVisibleliveData = MutableLiveData<Boolean>()
    val rightButtonIsVisibleliveData: LiveData<Boolean> = _rightButtonIsVisibleliveData

    val _wordsToTrainingLiveData = MutableStateFlow<List<Word>>(emptyList())
    val currentProgressLiveData = _wordsToTrainingLiveData.map { words ->
        words.size
    }.asLiveData()

    val needWordsLiveData = currentProgressLiveData.map { progress ->
        WORDS_TO_TRAINING_COUNT - progress
    }


    private val currentWordLiveData: Word?
        get() = _wordsLiveData.value.getOrNull(_currentWordIndexLiveData.value)

    val newKnownWords = mutableListOf<Word>()

    fun initViewModel(wordsResponse: WordsResponse) {
        viewModelScope.launch {
            delay(300)
            val allWords = wordsResponse.words //50
            _wordsLiveData.value = allWords.filter { !it.isKnown() }
            _currentWordIndexLiveData.value = 0
        }
    }

    fun setCurrentWordAsKnown() {
        _leftButtonIsVisibleliveData.value = true
        _rightButtonIsVisibleliveData.value = false
        currentWordLiveData?.let {
            newKnownWords.add(it)
        }

        if (_currentWordIndexLiveData.value < _wordsLiveData.value.lastIndex) {
            _currentWordIndexLiveData.value += 1
        } else {
            viewModelScope.launch {
                repository.setCompleteAllWords(newKnownWords)
            }
            viewModelScope.launch {
                repository.setStartWords(_wordsToTrainingLiveData.value)
            }
            _learningisCompleteEvent.emitEvent(_wordsToTrainingLiveData.value)
        }
    }

    fun setCurrentWordAsUnknown() {
        _rightButtonIsVisibleliveData.value = true
        _leftButtonIsVisibleliveData.value = false
        currentWordLiveData?.let {
            _wordsToTrainingLiveData.value = _wordsToTrainingLiveData.value.plus(it)
        }
        if (_wordsToTrainingLiveData.value.size < WORDS_TO_TRAINING_COUNT)
            _currentWordIndexLiveData.value += 1
        else {
            viewModelScope.launch {
                repository.setCompleteAllWords(_wordsToTrainingLiveData.value)
            }
            _learningisCompleteEvent.emitEvent(_wordsToTrainingLiveData.value)
        }
    }

    fun clickOnLeftButton() {
        if (newKnownWords.isNotEmpty()) {
            newKnownWords.removeAt(newKnownWords.lastIndex)
        }

        if (_currentWordIndexLiveData.value > 0) {
            _currentWordIndexLiveData.value--
            _leftButtonIsVisibleliveData.value = false
        }
    }

    fun clickOnRightButton() {
        if (_wordsToTrainingLiveData.value.isNotEmpty()) {
            _wordsToTrainingLiveData.value = _wordsToTrainingLiveData.value.subList(
                0,
                _wordsToTrainingLiveData.value.lastIndex
            )
        }

        if (_currentWordIndexLiveData.value > 0) {
            _currentWordIndexLiveData.value--
            _rightButtonIsVisibleliveData.value = false
        }
    }


}