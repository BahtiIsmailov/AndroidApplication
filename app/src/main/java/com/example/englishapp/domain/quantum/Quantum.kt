package com.example.englishapp.domain.quantum

data class Quantum(
    val id: String,
    val gameId: String,
    val quantumType: QuantumType? = null,
    val order: Int? = null,
    val startTime: Long? = null,
    val answerTime: Long? = null,
    val question: String? = null,
    val isCorrect: Boolean? = null,
) {
    enum class QuantumType(name: String) {
        GrammarConstructSentence("grammarConstructSentence"),
        GrammarChooseOption("grammarChooseOption"),
        WordsTrainingWordPresentation("wordsTrainingWordPresentation"),
        WordsTrainingWordByPicture("wordsTrainingWordByPicture"),
        WordsTrainingChooseWordByTranslationAndAudio("wordsTrainingChooseWordByTranslationAndAudio"),
        WordsTrainingChooseTranslationByWordAndAudio("wordsTrainingChooseTranslationByWordAndAudio"),
        WordsTrainingChooseTranslationByAudio("wordsTrainingChooseTranslationByAudio"),
        WordsTrainingChoosePictureByAudio("wordsTrainingChoosePictureByAudio"),
        WordsTrainingSpeakWordByWordAndPictureAndAudio("wordsTrainingSpeakWordByWordAndPictureAndAudio"),
        WordsTrainingConstructWord("wordsTrainingConstructWord"),
        DialogSelectOption("dialogSelectOption"),
        DialogPhrase("dialogPhrase"),
        DialogMakeSentence("dialogMakeSentence"),
        DialogWhatWasSaid("dialogWhatWasSaid"),
    }


}