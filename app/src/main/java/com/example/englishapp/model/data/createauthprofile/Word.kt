package com.example.englishapp.model.data.createauthprofile

data class Word(
    val alterWords: List<String>,
    val audio: String,
    val id: Int,
    val level: Int,
    val pictures: List<Picture>,
    val repeat_timeout: Int,
    val repeated_count: Int,
    val started: Boolean,
    val translate: String,
    val word: String
)