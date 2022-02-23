package com.example.englishapp.view.main.words.wordslist

import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.Wordset

sealed class WordsUiState {
    object Loading: WordsUiState()
    data class Data(val wordGroups: List<WordGroup>, val lastWordSet: Wordset?): WordsUiState()
    object Error: WordsUiState()
}
