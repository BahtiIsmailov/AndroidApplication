package com.example.englishapp.view.main.grammar.training

import com.example.englishapp.model.data.grammartraining.GrammarTrainingInfo

sealed class GrammarTrainingUiState {
    object InitialState: GrammarTrainingUiState()
    object Loading: GrammarTrainingUiState()
    data class Error(val error: String): GrammarTrainingUiState()
    object BeginTraining: GrammarTrainingUiState()
    object CountDownTimer: GrammarTrainingUiState()
    data class Training(val data: GrammarTrainingInfo, val isResume: Boolean): GrammarTrainingUiState()
    data class Pause(val openScreen: Boolean) : GrammarTrainingUiState()
}