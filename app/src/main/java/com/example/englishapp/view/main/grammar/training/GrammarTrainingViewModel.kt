package com.example.englishapp.view.main.grammar.training

import android.content.Context
import android.content.res.Resources
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.lifecycle.*
import com.example.englishapp.domain.UserInteractor
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.model.data.grammartraining.GrammarTrainingInfo
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.onError
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.utils.*
import com.example.englishapp.view.main.grammar.training.models.MotoDriverState
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

typealias Loading = GrammarTrainingUiState.Loading

@HiltViewModel
class GrammarTrainingViewModel @Inject constructor(
    private val repository: LetMeSpeakRepository,
    private val userInteractor: UserInteractor
) : ViewModel() {

    data class QuestionData(val question: Question, val type: TypeCurrentTraining)

    private var countCorrectQuestion = 0


    private var strike = 1 // количество правильных ответов подряд
    var addPointWhenCorrect = true

    var trainingInfo: GrammarTrainingInfo? = null

    enum class TypeCurrentTraining {
        MATCH_WORDS, REBUS;

        companion object {
            fun getRandomValue() =
                if ((1..2).random() == 1) {
                    MATCH_WORDS
                } else {
                    REBUS
                }

        }
    }

    var listQuestionLiveData = MutableLiveData<List<Question>>(emptyList())

    private val _uiState =
        MutableLiveData<GrammarTrainingUiState>(GrammarTrainingUiState.InitialState)
    val uiState: LiveData<GrammarTrainingUiState> = _uiState

    var userLevel: String = ""
    var countForUpUserLevel = 0

    private val _countQuestion = MutableLiveData<Int>()
    val countQuestion: LiveData<Int> = _countQuestion

    private val _countHearts = MutableStateFlow(3)
    val countHearts: LiveData<Int> = _countHearts.asLiveData()

    private val _endTrainingIsVisibleEvent = NoParamsMutableEvent()
    val endTrainingIsVisibleEvent: NoParamsEvent = _endTrainingIsVisibleEvent

    private val _countStarsLiveData = MutableStateFlow(0)
    val countStarsLiveData: LiveData<Int> = _countStarsLiveData.asLiveData()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex.asLiveData()

    private val _restoreHeartsEvent = MutableLiveData<Event<Unit>>()
    val retoreHeartsEvent: NoParamsEvent = _restoreHeartsEvent

    val currentQuestionLiveData = MediatorLiveData<QuestionData?>().apply {
        addSource(listQuestionLiveData) {
            if (listQuestionLiveData.value?.isNotEmpty() == true)
                value = QuestionData(
                    question = listQuestionLiveData.value?.get(_currentQuestionIndex.value)!!,
                    type = TypeCurrentTraining.getRandomValue()
                )

        }
        addSource(currentQuestionIndex) {
            if (listQuestionLiveData.value?.isNotEmpty() == true) {
                if (_currentQuestionIndex.value == listQuestionLiveData.value!!.lastIndex + 1) {
                    _endTrainingIsVisibleEvent.emitEvent()
                } else {
                    value = QuestionData(
                        question = listQuestionLiveData.value?.get(_currentQuestionIndex.value)!!,
                        type = TypeCurrentTraining.getRandomValue()
                    )
                }
            }
        }
    }

    val motoDriverLiveData = MutableLiveData(MotoDriverState(currentPosition = 0, false))

    fun onViewCreated(
        grammarMainData: GrammarMainData
    ) {
        viewModelScope.launch {

            if (uiState.value is GrammarTrainingUiState.InitialState) {
                _uiState.value = Loading
                repository.getTrainingInfo("ru", "en", grammarMainData.code)
                    .onSuccess {
                        trainingInfo = it
                        _uiState.value = GrammarTrainingUiState.BeginTraining
                    }
                    .onError {
                        _uiState.value = GrammarTrainingUiState.Error(
                            it.error?.error_description
                                ?: "упало в ошибку в GrammarTrainingViewModel"
                        )
                    }
            } else if (uiState.value is GrammarTrainingUiState.Pause) {
                trainingInfo?.let { trainingInfo ->
                    _uiState.value = GrammarTrainingUiState.Training(trainingInfo, true)
                }
            }
        }

        viewModelScope.launch {
            userLevel = userInteractor.getUser()?.language_level ?: ""
        }
    }

    fun countDownEnd() {
        trainingInfo?.let { trainingInfo ->
            _countStarsLiveData.value = 0
            _countHearts.value = COUNT_HEARTS

            listQuestionLiveData.value = if (trainingInfo.questions.size > QUESTION_COUNT) {
                trainingInfo.questions.subList(0, QUESTION_COUNT)
            } else {
                trainingInfo.questions
            }

            _countQuestion.value = trainingInfo.questions.size

            _uiState.value = GrammarTrainingUiState.Training(data = trainingInfo, isResume = false)
        }

    }

    fun goToNextStep(resultIsCorrect: Boolean) {
        if (resultIsCorrect) {
            addPointWhenCorrect = true
            countCorrectQuestion++
            if (countCorrectQuestion % 5 == 0) {
                _countStarsLiveData.value++
                countForUpUserLevel++
                when (countForUpUserLevel) {
                    1 -> userLevel = "1"
                    2 -> userLevel = "2"
                    3 -> userLevel = "3"
                }

                _countHearts.value = COUNT_HEARTS
                _restoreHeartsEvent.emitEvent()
            }
        } else {
            addPointWhenCorrect = false
            _countHearts.value--

        }
        _currentQuestionIndex.value++
    }


    fun countPoint(userPhraseWords: Int, excessWords: Int, exactAnswerTime: Long): Long {
        val scoreForCorrectWords = 10 * userPhraseWords
        val scoreForExcessWords = 3 * excessWords
        val scoreForQuickAnswer = 20 * max(10 - exactAnswerTime, 0)
        val answerScore =
            strike * (scoreForCorrectWords + scoreForExcessWords + scoreForQuickAnswer)
        return answerScore
    }

    fun pushOnPause() {
        _uiState.value = GrammarTrainingUiState.Pause(true)
    }

    fun isPauseScreenOpenProcessed() {
        _uiState.value = GrammarTrainingUiState.Pause(false)
    }

    fun parallaxForAny(
        resources: Resources,
        imageView: ImageView,
        duration: Long,
        context: Context,
        @DrawableRes drawable: Int
    ) {
        ParallaxEffect(
            resources,
            ParallaxEffect.getBitmap(context, drawable)!!,
            imageView,
            LinearInterpolator(),
            duration
        ).runAnimation()

    }

    fun onStartTrainingClick() {
        _uiState.postValue(GrammarTrainingUiState.CountDownTimer)
    }

    fun motoDriverGoNext() {
        motoDriverLiveData.value?.let {
            val current = it.copy(withAnimation = true)

            if (it.withReturnAnimation) {
                current.currentPosition = 1
                current.withReturnAnimation = false
            } else {
                current.currentPosition++
            }

            if (current.currentPosition == QUESTION_PART_COUNT - 1) {
                current.withReturnAnimation = true
            }

            motoDriverLiveData.postValue(current)
        }
    }

    companion object {
        const val QUESTION_COUNT = 15
        const val COUNT_HEARTS = 3
        const val QUESTION_PART_COUNT = 5
    }

}

const val MOTO_DRIVER_ANIMATION_DURATION = 1100L