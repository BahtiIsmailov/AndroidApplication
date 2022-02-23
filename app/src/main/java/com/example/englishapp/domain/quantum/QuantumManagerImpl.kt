package com.example.englishapp.domain.quantum

import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.utils.TokenHelper
import java.util.*
import javax.inject.Inject

class QuantumManagerImpl @Inject constructor(
    private val repository: LetMeSpeakRepository,
    private val tokenHelper: TokenHelper,
) : QuantumManager {

    private var quantum = Quantum(id = "id?", order = 1, gameId = UUID.randomUUID().toString())

    override fun createNewQuantum(
        question: String,
        type: Quantum.QuantumType,
        id: Int,
        order: Int
    ) {
        quantum = quantum.copy(
            startTime = System.currentTimeMillis(),
            question = question,
            quantumType = type,
            id = id.toString(),
            order = order
        )
    }

    override suspend fun setAnswerSelected(isCorrect: Boolean) {
        quantum = quantum.copy(isCorrect = isCorrect, answerTime = System.currentTimeMillis())

        repository.sendQuantum(quantum)

    }
}