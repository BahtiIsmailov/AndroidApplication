package com.example.englishapp.view.main.words.wordsinwordset.learning

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentLearningWordsBinding
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.utils.EventObserver
import com.example.englishapp.utils.FeatureFlags
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.utils.setStatusBarPadding
import com.example.englishapp.view.main.base.BaseFragment
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentLearningWords : BaseFragment(useCustomInsets = true) {
    private val viewModel: LearningWordsViewModel by viewModels()
    private var _binding: FragmentLearningWordsBinding? = null
    private val binding get() = _binding!!
    private val args: FragmentLearningWordsArgs by navArgs()

    private val adapter by lazy { CardStackAdapter() }
    private val manager by lazy {
        CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                if (viewModel.currentProgressLiveData.value == LearningWordsViewModel.WORDS_TO_TRAINING_COUNT - 1) {

                    val ll = binding.cardStackView.layoutManager as CardStackLayoutManager
                    val view = ll.findViewByPosition(ll.topPosition + 1) as? ConstraintLayout
                    val whiteLayout = view?.findViewById<FrameLayout>(R.id.endSelectLayout)

                    val layout = view?.findViewById<LinearLayout>(R.id.endSelectLayoutImage)

                    if (direction == Direction.Right) {
                        Log.d("onCardDragging", "ratio $ratio")

                        whiteLayout?.alpha = ratio + 0.5f
                        layout?.alpha = ratio

                        binding.text.alpha = 1 - ratio
                        binding.llKnow.alpha = 1 - ratio
                        binding.llUnknown.alpha = 1 - ratio
                        binding.cancelLeftPress.alpha = 1 - ratio
                        binding.cancelRightPress.alpha = 1 - ratio

                    } else {
                        whiteLayout?.alpha = 0f
                        layout?.alpha = 0f

                        binding.text.alpha = 1f
                        binding.llKnow.alpha = 1f
                        binding.llUnknown.alpha = 1f
                        binding.cancelLeftPress.alpha = 1f
                        binding.cancelRightPress.alpha = 1f
                    }
                }
            }

            override fun onCardSwiped(direction: Direction?) {
                when (direction) {
                    Direction.Left -> {
                        viewModel.setCurrentWordAsKnown()
                    }
                    Direction.Right -> {
                        viewModel.setCurrentWordAsUnknown()

                        if (viewModel.currentProgressLiveData.value == LearningWordsViewModel.WORDS_TO_TRAINING_COUNT) {

                            val ll = binding.cardStackView.layoutManager as CardStackLayoutManager
                            val view =
                                ll.findViewByPosition(ll.topPosition) as? ConstraintLayout
                            val whiteLayout = view?.findViewById<FrameLayout>(R.id.endSelectLayout)
                            val layout = view?.findViewById<LinearLayout>(R.id.endSelectLayoutImage)

                            whiteLayout?.alpha = 1f
                            layout?.alpha = 1f

                            binding.text.alpha = 0f
                            binding.llKnow.alpha = 0f
                            binding.llUnknown.alpha = 0f
                            binding.cancelLeftPress.alpha = 0f
                            binding.cancelRightPress.alpha = 0f
                        }
                    }
                    Direction.Top,
                    Direction.Bottom,
                    null -> {
                    }
                }
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
                viewModel.wordsLiveData.value?.getOrNull(position)?.let { word ->
                    speakWord(word)
                }
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FeatureFlags.enableFullScreen) {
            binding.root.setStatusBarPadding()
        }

        viewModel.initViewModel(args.wordResponse)

        setObserves()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.llKnow.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()

        }
        binding.llUnknown.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }

        binding.cancelLeftPress.setOnClickListener {
            viewModel.clickOnLeftButton()
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            binding.cardStackView.rewind()
        }

        binding.cancelRightPress.setOnClickListener {
            viewModel.clickOnRightButton()
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            binding.cardStackView.rewind()
        }
    }

    private fun setObserves() {
        viewModel.wordsLiveData.observe {
            manager.setStackFrom(StackFrom.Bottom)
            manager.setVisibleCount(3)
            manager.setTranslationInterval(4.0f)
            manager.setScaleInterval(0.95f)
            manager.setSwipeThreshold(0.3f)
            manager.setMaxDegree(-120.0f)
            manager.setDirections(Direction.FREEDOM)
            manager.setCanScrollHorizontal(true)
            manager.setCanScrollVertical(false)
            manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            manager.setOverlayInterpolator(LinearInterpolator())
            binding.cardStackView.layoutManager = manager
            binding.cardStackView.adapter = adapter
            binding.cardStackView.itemAnimator.apply {
                if (this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }

            adapter.setWords(it)
            binding.cardStackView.isInvisible = false
        }

        viewModel.currentWordIndexLiveData.observe(viewLifecycleOwner) {}

        viewModel.learningisCompleteEvent.observe(
            viewLifecycleOwner,
            EventObserver { wordstoTraining ->
                if (wordstoTraining.isNotEmpty()) {
                    lifecycleScope.launch {
                        binding.text.isGone = true
                        delay(2000)
                        val action =
                            FragmentLearningWordsDirections.actionFragmentLearningWordsToFragmentTrainingWords(
                                wordstoTraining.toTypedArray(),
                                args.wordResponse.id
                            )
                        findNavController().navigate(action)
                    }

                } else {
                    val id = args.wordResponse.id
                    val action =
                        FragmentLearningWordsDirections.actionFragmentLearningWordsToWordsInWordSetFragment(
                            id
                        )
                    findNavController().navigate(action)
                }
            })

        viewModel.leftButtonIsVisibleliveData.observe(viewLifecycleOwner) { isVisible ->
            binding.cancelLeftPress.isVisible = isVisible
        }

        viewModel.rightButtonIsVisibleliveData.observe(viewLifecycleOwner) { isVisible ->
            binding.cancelRightPress.isVisible = isVisible
        }

        viewModel.currentProgressLiveData.observe { progress ->

            binding.progressBar.learning_progress = progress
        }
        viewModel.needWordsLiveData.observe(viewLifecycleOwner) { number ->
            binding.number.text = number.toString()
        }
    }

    private fun speakWord(word: Word) {
        SoundPlayer.playWord(word.audio, word.translate)
    }

    //release почитать
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}