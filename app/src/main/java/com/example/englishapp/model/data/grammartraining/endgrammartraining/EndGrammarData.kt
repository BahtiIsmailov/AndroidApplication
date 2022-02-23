package com.example.englishapp.model.data.grammartraining.endgrammartraining

data class EndGrammarData(
    val energy_now: Int,
    val energy_spent: Int,
    val level_up: LevelUp?,
    val tokens: Double,
    val xp: Int
)