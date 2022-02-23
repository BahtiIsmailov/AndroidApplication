package com.example.englishapp.view.main.words.wordsinwordset.training

import android.content.Context
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.englishapp.domain.quantum.Quantum
import com.example.englishapp.domain.quantum.QuantumManager
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.utils.*
import com.example.englishapp.view.main.words.wordsinwordset.training.base.AnimationType
import com.example.englishapp.view.main.words.wordsinwordset.training.base.ErrorWord
import com.example.englishapp.view.main.words.wordsinwordset.training.base.TrainType
import com.example.englishapp.view.main.words.wordsinwordset.training.base.TrainView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingWordsViewModel @Inject constructor(
    private val repository: LetMeSpeakRepository,
    private val savedStateHandle: SavedStateHandle,
    private val quantumManager: QuantumManager
) : ViewModel() {

    private val wordsList by lazy { savedStateHandle.get<Array<Word>>("words") }
    private val wordSetId by lazy { savedStateHandle.get<Int>("wordSetId") ?: -1 }

    private val _endTrainingEvent = MutableLiveData<Event<EndTrainingResult>>()
    val endTrainingEvent: LiveData<Event<EndTrainingResult>> = _endTrainingEvent

    private val _progressLiveData = MutableLiveData<Pair<Int, Int>>()
    val progressLiveData = _progressLiveData

    private val _needPreloadImages = MutableLiveData<Unit>()
    val needPreloadImages = _needPreloadImages

    var progress: Int = -1

    private val _manyErrorEvent = NoParamsMutableEvent()
    val manyErrorEvent: NoParamsEvent = _manyErrorEvent

    private val _hearts = MutableLiveData<Int>()
    val hearts: MutableLiveData<Int> = _hearts

    private var heartsCount = HEARTS_START_COUNT

    val currentFragment = MutableLiveData<TrainView>()
    private val trainsList =
        arrayListOf(
            TrainType.TranslateWord,
            TrainType.WordTranslate,
            TrainType.Listen,
            TrainType.Rebus,
            TrainType.WordRecognition,
        )

    val words: ArrayList<Word> = ArrayList(wordsList?.toMutableList() ?: arrayListOf())
    private var needRepeatWords = emptyList<Word>()
    private val needRepeatWordIterator by lazy { needRepeatWords.iterator() }

    private var trainErrorWordMode = false

    private var currentWordIndex = 0
    private var currentTrainIndex = 0

    private var currentErrorWordIndex = 0

    private var trainMaxProgress = WORD_SET_COUNT * trainsList.size

    private val currentWord get() = words[currentWordIndex]
    private val currentTraining get() = trainsList[currentTrainIndex]

    private val currentErrorWord get() = errorWords[currentErrorWordIndex]

    private var additionalWordSets = listOf<Word>()

    private val wordTranslateVariants
        get() = additionalWordSets
            .filter { if (trainErrorWordMode) it.word != currentErrorWord.word.word else it.word != currentWord.word }
            .shuffled()
            .take(2)
            .plus(if (trainErrorWordMode) currentErrorWord.word else currentWord)
            .shuffled()
            .map { it.word }

    private val translateWordVariants
        get() = additionalWordSets
            .filter { if (trainErrorWordMode) it.word != currentErrorWord.word.word else it.word != currentWord.word }
            .shuffled()
            .take(2)
            .plus(if (trainErrorWordMode) currentErrorWord.word else currentWord)
            .shuffled()
            .map { it.translate }

    private val listenWordVariants
        get() = additionalWordSets
            .filter { it.word != currentWord.word }
            .shuffled()
            .take(1)
            .plus(if (trainErrorWordMode) currentErrorWord.word else currentWord)
            .shuffled()
            .map { (it.pictures.randomOrNull()?.url ?: "") to it.word }

    private val errorWords = arrayListOf<ErrorWord>()

    init {

        viewModelScope.launch {
            repository.getWordsByWordSetId("ru", "en", wordSetId).onSuccess {
                additionalWordSets = it.words
            }

            words.addAll(
                additionalWordSets.filter { filter -> filter.id !in words.map { it.id } }.shuffled()
                    .take(WORD_SET_COUNT - words.size).toMutableList()
            )

            needRepeatWords = words.filter { it.needRepeat() }

            startRememberWordsTrain()

            updateProgress()

            _needPreloadImages.emit(Unit)
        }
    }

    fun preloadImages(context: Context) {
        additionalWordSets.flatMap { it.pictures }.map { it.url }.forEach {
            Glide.with(context).load(it).preload()
        }
    }

    private fun openTrain(openNextTrain: Boolean) {
        if (openNextTrain) currentWordIndex++

        if (currentWordIndex >= words.size) {
            currentTrainIndex++
            words.shuffle()
            currentWordIndex = 0
        }

        if (currentTrainIndex >= trainsList.size) {
            trainErrorWordMode = true
            openErrorWordsTrain(openNextTrain = false)
        } else {
            openScreenByTrain(currentTraining, currentWord)
        }
    }

    private fun updateProgress() {
        progress++
        _progressLiveData.emit(progress to trainMaxProgress)
    }

    private fun openScreenByTrain(currentTraining: TrainType, currentWord: Word) {
        val (fragment, type) = when (currentTraining) {
            TrainType.WordTranslate -> {
                wordTranslateView(
                    currentWord,
                    wordTranslateVariants
                ) to Quantum.QuantumType.WordsTrainingChooseWordByTranslationAndAudio
            }
            TrainType.TranslateWord -> {
                translateWordView(
                    currentWord,
                    translateWordVariants
                ) to Quantum.QuantumType.WordsTrainingChooseTranslationByWordAndAudio
            }
            TrainType.Listen -> {
                listenWordView(
                    currentWord,
                    listenWordVariants
                ) to Quantum.QuantumType.WordsTrainingChoosePictureByAudio

            }
            TrainType.Rebus -> {
                rebusWordView(currentWord) to Quantum.QuantumType.WordsTrainingConstructWord

            }
            TrainType.WordRecognition -> {
                speechRecognizedWordView(currentWord) to Quantum.QuantumType.WordsTrainingSpeakWordByWordAndPictureAndAudio
            }
            else -> throw Throwable("not support")
        }

        currentFragment.emit(fragment)

        quantumManager.createNewQuantum(
            question = currentWord.word,
            type = type,
            id = currentWord.id,
            order = progress
        )
    }

    private fun openErrorWordsTrain(openNextTrain: Boolean) {
        if (openNextTrain) currentErrorWordIndex++

        if (currentErrorWordIndex < errorWords.size) {

            openScreenByTrain(currentErrorWord.trainType, currentErrorWord.word)
        } else {
            val endTrainingResult = EndTrainingResult(
                words.joinToString(",") { it.id.toString() },
                words.size.toString(),
                errorWords.size.toString()
            )
            _endTrainingEvent.emitEvent(endTrainingResult)
        }
    }

    private fun startRememberWordsTrain() = onRememberClick()

    fun onRememberClick() {
        if (needRepeatWordIterator.hasNext()) {
            currentFragment.emit(repeatWord(needRepeatWordIterator.next()))
        } else {
            openTrain(openNextTrain = false)
        }
    }

    fun onSuccess(animationType: AnimationType = AnimationType.Check) {
        viewModelScope.launch {
            quantumManager.setAnswerSelected(isCorrect = true)
            delay(200)
            currentFragment.emit(
                successView(
                    if (trainErrorWordMode) currentErrorWord.word else currentWord,
                    animationType
                )
            )

            updateProgress()

            openTrainByMode()
        }
    }

    fun onFail(speechWord: String? = null) {
        val paramsWord = if (trainErrorWordMode) currentErrorWord.word else currentWord

        viewModelScope.launch {
            quantumManager.setAnswerSelected(isCorrect = false)
            updateProgress()

            if (!trainErrorWordMode) {
                errorWords.add(ErrorWord(currentWord, trainsList[currentTrainIndex]))
                trainMaxProgress++
            }

            delay(200)
            currentFragment.emit(failView(paramsWord, speechWord))

            updateHeartCount()

            if (errorWords.size % 7 == 0 && errorWords.size > 0) {
                _manyErrorEvent.emitEvent()
            }

            openTrainByMode()
        }
    }

    private fun updateHeartCount() {
        heartsCount--
        if (heartsCount >= 0) _hearts.emit(heartsCount)
    }

    private fun openTrainByMode() {
        viewModelScope.launch {
            delay(1000)
            if (!trainErrorWordMode) {
                openTrain(openNextTrain = true)
            } else {
                openErrorWordsTrain(openNextTrain = true)
            }
        }
    }

    fun onSelectTranslate(word: String, animationType: AnimationType = AnimationType.Check) {
        val wordToCompare = if (trainErrorWordMode) currentErrorWord.word else currentWord

        viewModelScope.launch {
            if (word.equals(
                    wordToCompare.word,
                    ignoreCase = true
                ) or word.equals(wordToCompare.translate, ignoreCase = true)
            ) {
                onSuccess(animationType)
            } else {
                onFail(word)
            }
        }
    }

    private fun translateWordView(word: Word, translateWordVariants: List<String>): TrainView {
        return TrainView(
            trainType = TrainType.TranslateWord,
            word = word,
            variants = translateWordVariants
        )
    }

    private fun wordTranslateView(word: Word, wordTranslateVariants: List<String>): TrainView {
        return TrainView(
            trainType = TrainType.WordTranslate,
            word = word,
            variants = wordTranslateVariants
        )
    }

    private fun listenWordView(
        word: Word,
        listenWordVariants: List<Pair<String, String>>
    ): TrainView {
        return TrainView(trainType = TrainType.Listen, word = word, images = listenWordVariants)
    }

    private fun rebusWordView(
        word: Word
    ): TrainView {
        return TrainView(trainType = TrainType.Rebus, word = word)
    }

    private fun speechRecognizedWordView(word: Word): TrainView {
        return TrainView(trainType = TrainType.WordRecognition, word = word)
    }

    fun cancelWordRecognizer() {
        trainsList.replaceRecognizerToWordTranslate()
        openTrain(openNextTrain = false)
    }

    private fun ArrayList<TrainType>.replaceRecognizerToWordTranslate() {
        val posToReplace = this.indexOf(TrainType.WordRecognition)
        this.removeAt(posToReplace)
        this.add(posToReplace, TrainType.WordTranslate)
    }

    private fun successView(word: Word, animationType: AnimationType): TrainView {
        return TrainView(trainType = TrainType.Success, word = word, animationType = animationType)
    }

    private fun failView(word: Word, speechWord: String?): TrainView {
        return TrainView(trainType = TrainType.Fail, word = word, speechWord = speechWord)
    }

    private fun repeatWord(word: Word): TrainView {
        return TrainView(trainType = TrainType.Repeat, word = word)
    }

    companion object {
        const val WORD_SET_COUNT = 4
        const val HEARTS_START_COUNT = 6
    }
}