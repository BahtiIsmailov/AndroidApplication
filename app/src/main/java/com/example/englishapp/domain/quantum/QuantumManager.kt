package com.example.englishapp.domain.quantum

interface QuantumManager {

    fun createNewQuantum(question: String, type: Quantum.QuantumType, id: Int, order: Int)

    suspend fun setAnswerSelected(isCorrect: Boolean)

}