package com.example.englishapp.view.main.words.wordsinwordset.training.base

interface TrainingView {

    fun onRememberClick()

    fun onSelectTranslate(word: String)

    fun onSuccess(animationType: AnimationType)

    fun onFail(recognizedWord: String? = null)

    fun cancelWordRecognizer()

}