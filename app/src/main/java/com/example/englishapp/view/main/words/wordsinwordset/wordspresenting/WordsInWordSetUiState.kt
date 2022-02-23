package com.example.englishapp.view.main.words.wordsinwordset.wordspresenting

import com.example.englishapp.model.data.words.WordsResponse
import com.xwray.groupie.Group

sealed class WordsInWordSetUiState {
    object Loading: WordsInWordSetUiState()
    data class Data(val main: WordsResponse, val words: List<Group>, val imgList: List<String>): WordsInWordSetUiState()
}
