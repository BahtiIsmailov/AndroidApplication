package com.example.englishapp.model.data.grammartraining.endgrammartraining

data class GetStatisticFromOtherPerson(
    val all: List<TopPlayerDto>,
    val weekly: List<TopPlayerDto>
)