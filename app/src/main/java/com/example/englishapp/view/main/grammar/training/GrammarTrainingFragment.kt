package com.example.englishapp.view.main.grammar.training


import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation

import android.view.animation.AnimationUtils

import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentGrammarTrainingBinding
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.utils.*
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.erroranswer.ErrorAnswerFragment
import com.example.englishapp.view.main.grammar.succesanswer.SuccesAnswerFragment
import com.example.englishapp.view.main.grammar.training.GrammarTrainingViewModel.TypeCurrentTraining
import com.example.englishapp.view.main.grammar.training.matchword.MatchWordGrammarTrainingFragment
import com.example.englishapp.view.main.grammar.training.rebus.RebusGrammarTrainingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.englishapp.view.main.grammar.GrammarBeginTrainingFragment
import com.example.englishapp.view.main.grammar.training.countdowntimer.CountDownFragment
import com.example.englishapp.view.main.grammar.training.interfaces.CountDownListener
import com.example.englishapp.view.main.grammar.training.interfaces.GrammarBeginTrainingListener
import com.example.englishapp.view.main.grammar.training.interfaces.GrammarQuestionsListener

@AndroidEntryPoint
class GrammarTrainingFragment : BaseFragment(), GrammarQuestionsListener,
    GrammarBeginTrainingListener, CountDownListener {
    private val viewmodel: GrammarTrainingViewModel by viewModels()
    private var _binding: FragmentGrammarTrainingBinding? = null
    private val binding get() = _binding!!
    private val args: GrammarTrainingFragmentArgs by navArgs()
    var startSecondsForAnswerTime = 0L
    private var game_id: String? = null

    var currentPositionInWidth = 0f
    var countForStar = 0
    var resultStarsForEndTraining = 0
    var resultPoint = 0L

    private fun startParallaxAnimation() {
        opacityImageAfternoon()
        parallaxForDay()
        parallaxForNight()
    }

    private fun continueParallaxAnimation() {
        opacityImageAfternoon()
        parallaxForDay()
        parallaxForNight()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SoundGrammarPlayer.setInitMediaPlayerWithCountDownTimer(
            R.raw.lms_moto_holostoy,
            R.raw.moto_count_down
        )


        viewmodel.onViewCreated(args.grammarMainData)

        initListeners()
        bindViewModel()
    }

    private fun initListeners() {
        binding.stopTraining.setOnClickListener {
            viewmodel.pushOnPause()
        }
    }

    private fun bindViewModel() {
        with(binding) {
            viewmodel.uiState.observe {
                when (it) {
                    GrammarTrainingUiState.InitialState -> {
                    }
                    GrammarTrainingUiState.Loading -> {
                        progressBar.isVisible = true
                        progressBar.playAnimation()
                    }

                    is GrammarTrainingUiState.Error -> {
                        progressBar.isGone = true
                        progressBar.cancelAnimation()
                    }

                    GrammarTrainingUiState.CountDownTimer -> {
                        SoundGrammarPlayer.playMediaPlayerWithCountDownTimer(
                            R.raw.lms_moto_holostoy,
                            R.raw.moto_count_down
                        )

                        val fragment = CountDownFragment()
                        showFragment(fragment)
                    }

                    GrammarTrainingUiState.BeginTraining -> {
                        progressBar.isGone = true
                        progressBar.cancelAnimation()

                        val fragment =
                            GrammarBeginTrainingFragment.newInstance(args.grammarMainData)
                        showFragment(fragment)
                    }

                    is GrammarTrainingUiState.Training -> {
                        if (!it.isResume) {
                            startParallaxAnimation()
                            game_id = it.data.game_id

                            with(SoundGrammarPlayer) {
                                if (!isBackgroundMusicPaused) {
                                    startBackgroundMusic()
                                }
                            }

                            viewmodel.currentQuestionLiveData.value?.let { questionData ->
                                showQuestion(questionData)
                            }
                        } else {
                            with(SoundGrammarPlayer) {
                                if (!isBackgroundMusicPaused) {
                                    startBackgroundMusic()
                                }
                            }
                            continueParallaxAnimation()
                            viewmodel.currentQuestionLiveData.value?.let { questionData ->
                                showQuestion(questionData)
                            }
                        }
                    }

                    is GrammarTrainingUiState.Pause -> {
                        if (it.openScreen) {
                            viewmodel.isPauseScreenOpenProcessed()

                            val action =
                                GrammarTrainingFragmentDirections.actionGrammarTrainingFragmentToPausedGrammarTrainingFragment(
                                    args.grammarMainData
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }

        viewmodel.currentQuestionIndex.observe {
            binding.currentCountTraining.text = it.toString()
        }

        viewmodel.countHearts.observe {
            when (it) {
                2 -> binding.ivHeart3.setFailHearts()
                1 -> binding.ivHeart2.setFailHearts()
                0 -> {
                    clearMarginMotociklist()
                    val action =
                        GrammarTrainingFragmentDirections.actionGrammarTrainingFragmentToManyErrorGrammarTrainingFragment(
                            args.grammarMainData
                        )

                    findNavController().navigate(action)
                }
            }

        }

        viewmodel.retoreHeartsEvent.observeEvent {
            with(binding) {
                ivHeart1.setSuccessHearts()
                ivHeart2.setSuccessHearts()
                ivHeart3.setSuccessHearts()
            }
        }

        viewmodel.listQuestionLiveData.observe {
            binding.allCountTraining.text = it.size.toString()
        }

        viewmodel.countStarsLiveData.observe {
            with(binding) {
                when (it) {
                    1 -> {
                        ivStar1.setSuccessStar()
                    }
                    2 -> {
                        ivStar2.setSuccessStar()
                    }
                    3 -> {
                        ivStar3.setSuccessStar()
                    }
                }
                resultStarsForEndTraining = it
            }
        }

        viewmodel.currentQuestionLiveData.observe { it ->
            it?.let {
                showQuestion(it)
            }
            if (viewmodel.addPointWhenCorrect) {
                binding.points.text = resultPoint.toString()

                val currentPoint = viewmodel.countPoint(
                    userPhraseWords = it?.question?.phrase?.split(" ")?.size
                        ?: 0,
                    excessWords = it?.question?.matchWrongWords?.split(
                        " "
                    )?.size ?: 0,
                    exactAnswerTime = SetTimeForGrammarTrainingAnswer.resultAnswerTime
                )
                resultPoint += currentPoint

            }
        }


        viewmodel.endTrainingIsVisibleEvent.observeEvent {
                SoundGrammarPlayer.resetBackgroundMusic()
                setDataForEndTraining(resultPoint, resultStarsForEndTraining)
            }

            viewmodel.motoDriverLiveData.observe {
                val parentWidth = (binding.motociklist.root.parent as View).width

                val everyPartOfParentWidth = parentWidth / 6
                currentPositionInWidth += everyPartOfParentWidth

                binding.motociklist.root.animate()
                    .translationX(it.currentPosition.toFloat() * everyPartOfParentWidth)
                    .withEndAction {
                        if (it.withReturnAnimation) {
                            binding.motociklist.root
                                .animate()
                                .translationX(0f)
                                .setDuration(MOTO_DRIVER_ANIMATION_DURATION)
                                .start()

                        }
                    }
                    .setDuration(MOTO_DRIVER_ANIMATION_DURATION)
                    .start()
            }
    }

    override fun onSuccessGrammarResult(question: Question) {
        countForStar++
        viewmodel.motoDriverGoNext()

        SetTimeForGrammarTrainingAnswer.resultAnswerTime =
            (System.currentTimeMillis() - startSecondsForAnswerTime) / 1000

        lifecycleScope.launch {
            val fragment = SuccesAnswerFragment.newInstance(question)
            showFragment(fragment)

            delay(2000)
            viewmodel.goToNextStep(true)
        }
    }

    override fun onErrorGrammarResult(
        question: Question,
        originalPhrase: String,
        errorPhrase: String
    ) {
        lifecycleScope.launch {
            val fragment =
                ErrorAnswerFragment.newInstance(question, originalPhrase, errorPhrase)
            showFragment(fragment)
            delay(2000)
            viewmodel.goToNextStep(false)
        }
    }

    private fun showQuestion(questionData: GrammarTrainingViewModel.QuestionData) {
        startSecondsForAnswerTime = System.currentTimeMillis()

        when (questionData.type) {
            TypeCurrentTraining.REBUS -> {
                val fragment = RebusGrammarTrainingFragment.newInstance(
                    questionData.question,
                    viewmodel.userLevel
                )

                showFragment(fragment)
            }

            TypeCurrentTraining.MATCH_WORDS -> {
                val fragment = MatchWordGrammarTrainingFragment.newInstance(
                    questionData.question,
                )

                showFragment(fragment)
            }
        }
    }

    private fun setDataForEndTraining(points: Long, countStars: Int) {
        val action =
            GrammarTrainingFragmentDirections.actionGrammarTrainingFragmentToEndGrammarTrainingFragment(
                args.grammarMainData,
                points,
                countStars,
                game_id!!
            )
        findNavController().navigate(action)
    }

    private fun opacityImageAfternoon() {

        val transition: TransitionDrawable =
            binding.bottomViewContainer.background as TransitionDrawable
        transition.startTransition(60000)

        val animationForDay = setAnimation(R.anim.from_afternoon_to_night) // из дня в ночи
        binding.imageView.root.startAnimation(animationForDay)

        val animation =
            setAnimation(R.anim.animation_for_sun_grammar_training) // отдельно для солнца
        binding.imageView.sun.startAnimation(animation)

        val animationForNight = setAnimation(R.anim.from_night_to_afternoon) // из ночи в день
        binding.imageViewForNight.root.startAnimation(animationForNight)

        animation.setOnAnimationEnd {
            with(binding.motociklist) {
                fari.isVisible = true
                backStopFari.isVisible = true
            }

        }
    }

    private fun parallaxForDay() {
        with(viewmodel) {
            with(binding.imageView) {
                parallaxForAny(resources, sky, 20000, requireContext(), R.drawable.ic_sky)
                parallaxForAny(
                    resources,
                    cityForBackgroundCity,
                    40000,
                    requireContext(),
                    R.drawable.ic_back_background_city
                )
                parallaxForAny(
                    resources,
                    cityForBackground,
                    35000,
                    requireContext(),
                    R.drawable.ic_city_for_afternoon
                )
                parallaxForAny(resources, korabli, 30000, requireContext(), R.drawable.ic_korabli)
                parallaxForAny(resources, road, 20000, requireContext(), R.drawable.ic_road)
                parallaxForAny(resources, topTrees, 20000, requireContext(), R.drawable.ic_trees)
                parallaxForAny(
                    resources,
                    bottomTrees,
                    20000,
                    requireContext(),
                    R.drawable.ic_bottom_trees
                )
            }
        }
    }

    private fun parallaxForNight() {
        with(viewmodel) {
            with(binding.imageViewForNight) {
                parallaxForAny(resources, sky, 20000, requireContext(), R.drawable.ic_night_sky)
                parallaxForAny(
                    resources,
                    backgroundCityForCityNight,
                    40000,
                    requireContext(),
                    R.drawable.ic_background_city_for_city_night
                )
                parallaxForAny(
                    resources,
                    cityForBackground,
                    35000,
                    requireContext(),
                    R.drawable.ic_background_city_night
                )
                parallaxForAny(
                    resources,
                    korabliNight,
                    30000,
                    requireContext(),
                    R.drawable.ic_korabli_night
                )
                parallaxForAny(resources, road, 20000, requireContext(), R.drawable.ic_road_dark)
                parallaxForAny(
                    resources,
                    trees2,
                    20000,
                    requireContext(),
                    R.drawable.ic_trees_night
                )
                parallaxForAny(
                    resources,
                    bottomTreesNight,
                    20000,
                    requireContext(),
                    R.drawable.ic_bottom_trees_night
                )
            }
        }
    }

    private fun returnMotoBack() {
        this.currentPositionInWidth = 0f
        this.countForStar = 0
        val params = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        params.bottomToBottom = R.id.imageView
        params.startToStart = R.id.frameLayout
        params.endToEnd = R.id.frameLayout
        params.bottomMargin = 9
        params.marginStart = (30f.toDp()).toInt()
        Log.d("(15f.toDp()).toInt()", (30f.toDp()).toInt().toString())


        val animation = setAnimation(R.anim.animation_motociklist_from_end_to_start)
        binding.motociklist.root.layoutParams = params
        binding.motociklist.root.startAnimation(animation)

    }

    private fun setAnimation(anim: Int): Animation {
        return AnimationUtils.loadAnimation(
            requireContext(),
            anim
        )
    }

    private fun clearMarginMotociklist() {
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomToTop = R.id.bottomViewContainer
        params.startToStart = R.id.begin_training
        params.marginStart = 10
        params.bottomMargin = 6
        binding.motociklist.root.layoutParams = params
        binding.motociklist.bodyMoto.isGone = true
        binding.motociklist.bodyMotoForManyError.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SoundGrammarPlayer.resetBackgroundMusic()
        SetTimeForGrammarTrainingAnswer.resultAnswerTime = 0
        _binding = null

    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.bottomViewContainer, fragment)
            .commit()
    }

    override fun onReadRuleClick() {
        val d =
            GrammarTrainingFragmentDirections.actionGlobalFragmentGrammarItemDescription(args.grammarMainData)
        findNavController().navigate(d)
    }

    override fun onBeginTraining() {
        viewmodel.onStartTrainingClick()
    }

    override fun onCountDownEnd() {
        viewmodel.countDownEnd()
    }

}
