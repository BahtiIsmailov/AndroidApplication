package com.example.englishapp.view.main.grammar.training.interfaces

import com.example.englishapp.model.data.grammartraining.Question

interface GrammarQuestionsListener {
    fun onSuccessGrammarResult(question: Question)
    fun onErrorGrammarResult(question: Question, originalPhrase: String, errorPhrase: String)
}