package com.example.englishapp.view.main.words.wordsinwordset.training.base

import com.example.englishapp.model.data.words.Word

data class TrainView(
    val trainType: TrainType,
    val word: Word,
    val variants: List<String>? = null,
    val images: List<Pair<String, String>>? = null,
    val animationType: AnimationType? = null,
    val speechWord: String? = null
)
