package com.example.englishapp.model.data.training


data class EndTrainingWordSetResponse(
    val energy_now: Int,
    val energy_spent: Int,
    val level_up: LevelUp?,
    val tokens: Double,
    val xp: Int
)