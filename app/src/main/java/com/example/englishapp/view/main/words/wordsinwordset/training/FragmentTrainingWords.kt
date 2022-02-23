package com.example.englishapp.view.main.words.wordsinwordset.training

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.R
import com.example.englishapp.databinding.*
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.utils.*
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.words.wordsinwordset.training.base.*
import com.example.englishapp.view.main.words.wordsinwordset.training.base.TrainType.*
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

@AndroidEntryPoint
class FragmentTrainingWords : BaseFragment(useCustomInsets = true), TrainingView,
    FragmentManyErrorsListener {
    private val viewModel: TrainingWordsViewModel by viewModels()
    private var _binding: FragmentTrainingWordsBinding? = null
    val binding get() = _binding!!

    private val args: FragmentTrainingWordsArgs by navArgs()

    val fragment: FragmentTrainingWordTypesBase by lazy { FragmentTrainingWordTypesBase() }
    var lastTrainType: TrainType? = null

    private val wordRecognizer: WordRecognizer by lazy {
        WordRecognizer(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FeatureFlags.enableFullScreen) {
            binding.topItems.setStatusBarPadding()
        }

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.manyErrorEvent.observeEvent {
            val manyError = FragmentManyError.getInstance()
            manyError.show(childFragmentManager, null)
        }
        viewModel.endTrainingEvent.observeEvent {
            val action =
                FragmentTrainingWordsDirections.actionFragmentTrainingWordsToFragmentEndTraining(
                    it,
                    viewModel.words.toTypedArray(),
                    args.wordSetId
                )
            findNavController().navigate(action)
        }

        viewModel.progressLiveData.observe {
            binding.progressBar.max = it.second
            binding.progressBar.progress = it.first
        }

        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        viewModel.currentFragment.observe {
            fragment.makeCommonItemsInvisible()

            lastTrainType = it.trainType

            when (it.trainType) {
                WordTranslate -> {
                    val bottomBinding = ContentTrainingTranslateWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setWordTranslateMode(bottomBinding, it)
                }
                TranslateWord -> {
                    val bottomBinding = ContentTrainingTranslateWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setTranslateWordMode(bottomBinding, it)
                }
                Rebus -> {
                    val bottomBinding = ContentTrainingRebusWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setRebusMode(bottomBinding, it)
                }
                WordRecognition -> {
                    val bottomBinding =
                        ContentTrainingSpeechRecognizedBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setSpeechRecognizerMode(bottomBinding, it)
                }
                Success -> {
                    val bottomBinding = ContentTrainingSuccessWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setSuccessMode(bottomBinding, it)
                }
                Fail -> {
                    val bottomBinding = ContentTrainingFailWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setFailMode(bottomBinding, it)
                }
                Listen -> {
                    val bottomBinding = ContentTrainingListenWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setListenMode(bottomBinding, it)
                }
                Repeat -> {
                    val bottomBinding = ContentTrainingRepeatWordBinding.inflate(layoutInflater)
                    viewChangeAnimation(
                        view = bottomBinding.root
                    )
                    setRepeatMode(bottomBinding, it)
                }
            }
        }

        viewModel.needPreloadImages.observe {
            viewModel.preloadImages(requireContext())
        }

        val animation = loadAnimation(requireContext(), R.anim.bounce)
        viewModel.hearts.observe {

            when (it.toString()) {
                "5" -> {
                    binding.ivHeart1.startAnimation(animation)

                }
                "4" -> {
                    binding.ivHeart1.animation = null
                    binding.ivHeart1.setUncheckedHeart()
                }
                "3" -> {
                    binding.ivHeart2.startAnimation(animation)
                }
                "2" -> {
                    binding.ivHeart2.animation = null
                    binding.ivHeart1.animation = null

                    binding.ivHeart1.setUncheckedHeart()
                    binding.ivHeart2.setUncheckedHeart()
                }
                "1" -> {
                    binding.ivHeart3.startAnimation(animation)
                }
                "0" -> {
                    binding.ivHeart1.animation = null
                    binding.ivHeart2.animation = null
                    binding.ivHeart3.animation = null

                    binding.ivHeart3.setUncheckedHeart()
                    binding.ivHeart2.setUncheckedHeart()
                    binding.ivHeart1.setUncheckedHeart()
                }
            }
        }
    }

    fun ImageView.setCheckedHeart() {
        setImageResource(R.drawable.ic_heart)
    }

    fun ImageView.setUncheckedHeart() {
        setImageResource(R.drawable.ic_heart_unchecked)
    }

    override fun onRememberClick() {
        viewModel.onRememberClick()
    }

    override fun onSelectTranslate(word: String) {
        viewModel.onSelectTranslate(word)
    }

    override fun onSuccess(animationType: AnimationType) {
        viewModel.onSuccess(animationType)
    }


    override fun onFail(recognizedWord: String?) {
        viewModel.onFail(recognizedWord)
    }

    override fun cancelWordRecognizer() {
        viewModel.cancelWordRecognizer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTranslateWordMode(
        bottomBinding: ContentTrainingTranslateWordBinding,
        trainView: TrainView
    ) = with(fragment) {
        bottomBinding.root.isVisible = true
        binding.tvWordOnImage.isVisible = true

        setImage(trainView.word)
        setTextOnImage(trainView.word.word)

        with(bottomBinding) {
            btnSelect1.text = trainView.variants?.getOrNull(0) ?: ""
            btnSelect2.text = trainView.variants?.getOrNull(1) ?: ""
            btnSelect3.text = trainView.variants?.getOrNull(2) ?: ""

            var isClicked = false
            btnSelect1.setOnPushListener {
                if (!isClicked) {
                    getBaseView()?.onSelectTranslate(btnSelect1.text)
                    isClicked = true
                }
            }

            btnSelect2.setOnPushListener {
                if (!isClicked) {
                    getBaseView()?.onSelectTranslate(btnSelect2.text)
                    isClicked = true
                }
            }

            btnSelect3.setOnPushListener {
                if (!isClicked) {
                    getBaseView()?.onSelectTranslate(btnSelect3.text)
                    isClicked = true
                }
            }
        }
    }

    private fun setWordTranslateMode(
        bottomBinding: ContentTrainingTranslateWordBinding,
        trainView: TrainView
    ) = with(fragment) {
        binding.imageView.isVisible = false

        setBigAudioIcon(trainView.word, true)
        setBigText(trainView.word)

        with(bottomBinding) {
            btnSelect1.text = trainView.variants?.getOrNull(0) ?: ""
            btnSelect2.text = trainView.variants?.getOrNull(1) ?: ""
            btnSelect3.text = trainView.variants?.getOrNull(2) ?: ""

            var isClicked = false
            btnSelect1.setOnPushListener {
                if (!isClicked) {
                    getBaseView()?.onSelectTranslate(btnSelect1.text)
                    isClicked = true
                }
            }

            btnSelect2.setOnPushListener {
                if (!isClicked) {
                    getBaseView()?.onSelectTranslate(btnSelect2.text)
                    isClicked = true
                }
            }

            btnSelect3.setOnPushListener {
                if (!isClicked) {
                    getBaseView()?.onSelectTranslate(btnSelect3.text)
                    isClicked = true
                }
            }
        }
    }

    private fun setRebusMode(
        bottomBinding: ContentTrainingRebusWordBinding,
        trainView: TrainView
    ) = with(fragment) {
        binding.imageView.isVisible = false

        val rebusViewController =
            RebusViewController(requireContext(), bottomBinding, getBaseView())
        rebusViewController.currentLetters = trainView.word.translate.toCharArray().toMutableList()

        setImage(trainView.word)
        setAudio(trainView.word)
        setTextOnImage(trainView.word.word)
        rebusViewController.splitWordToLetters(trainView.word.translate)
    }

    private fun setSuccessMode(
        bottomBinding: ContentTrainingSuccessWordBinding,
        trainView: TrainView
    ) = with(fragment) {

        bottomBinding.tvWord.text = trainView.word.translate
        setImage(trainView.word)
        setTextOnImage(trainView.word.word)

        when (trainView.animationType) {
            AnimationType.WithoutAnimation -> {

            }
            AnimationType.Check -> {
                SoundPlayer.play(R.raw.easy_talk_resources_sounds_actions_correct_answer_selected)
                bottomBinding.containerDefault.isVisible = true
                bottomBinding.containerFireworks.isVisible = false
            }
            AnimationType.Fireworks -> {
                bottomBinding.containerDefault.isGone = true
                bottomBinding.containerFireworks.isVisible = true
                bottomBinding.succesLottie.playAnimation()
            }
            else -> {
            }
        }
    }

    private fun setFailMode(
        bottomBinding: ContentTrainingFailWordBinding,
        trainView: TrainView
    ) = with(fragment) {

        SoundPlayer.play(R.raw.easy_talk_resources_sounds_actions_incorrect_answer_selected)
        if (trainView.speechWord == null) {
            bottomBinding.tvWord.text = trainView.word.translate
        } else {
            bottomBinding.tvWord.text = trainView.speechWord
        }

        setImage(trainView.word)
        setTextOnImage(trainView.word.word)
    }

    private fun setListenMode(
        bottomBinding: ContentTrainingListenWordBinding,
        trainView: TrainView
    ) = with(fragment) {
        binding.imageView.isVisible = false

        setBigAudioIcon(trainView.word, true)

        with(bottomBinding) {
            tvVariant1.text = trainView.images?.getOrNull(0)?.second ?: ""
            tvVariant2.text = trainView.images?.getOrNull(1)?.second ?: ""
            ivVariant1.loadImageWithStatueOfLibertyPlaceholder(
                trainView.images?.getOrNull(0)?.first ?: ""
            )
            ivVariant2.loadImageWithStatueOfLibertyPlaceholder(
                trainView.images?.getOrNull(1)?.first ?: ""
            )

            ivVariant1.setOnClickListener {
                getBaseView()?.onSelectTranslate(tvVariant1.text.toString())
            }

            ivVariant2.setOnClickListener {
                getBaseView()?.onSelectTranslate(tvVariant2.text.toString())
            }
        }
    }

    private fun setRepeatMode(
        bottomBinding: ContentTrainingRepeatWordBinding,
        trainView: TrainView
    ) = with(fragment) {
        binding.imageView.isVisible = false

        binding.ivVoice.isVisible = true

        setImage(trainView.word)
        setAudio(trainView.word)

        with(bottomBinding) {
            tvTitle.text = trainView.word.translate
            tvSubtitle.text = trainView.word.word
            btnSelect.setOnClickListener {
                SoundPlayer.play(R.raw.easy_talk_resources_sounds_actions_correct_answer_selected)
                getBaseView()?.onRememberClick()
            }

        }
    }

    private fun setSpeechRecognizerMode(
        bottomBinding: ContentTrainingSpeechRecognizedBinding,
        trainView: TrainView
    ) = with(fragment) {

        bottomBinding.speechButton.setOnClickListener {
            wordRecognizer.recognizeWords(
                registry = requireActivity().activityResultRegistry,
                onError = {
                    bottomBinding.audioRec.isVisible = true
                    bottomBinding.lottieRecognizer.isVisible = false

                },
                onRmsChanged = {
                    if (it > RMS_DB_MIN)
                        bottomBinding.lottieRecognizer.playAnimation()
                    else
                        bottomBinding.lottieRecognizer.cancelAnimation()
                    bottomBinding.audioRec.isVisible = false

                },
                onReadyForSpeech = {
                    bottomBinding.audioRec.isVisible = false
                    bottomBinding.lottieRecognizer.isVisible = true

                },
                onResults = { recognizedWord ->
                    setRecognizedWords(recognizedWord, trainView.word)
                }
            )
            lifecycle.addObserver(wordRecognizer)
        }

        setImage(trainView.word)
        bottomBinding.originalWord.text = trainView.word.translate
        binding.tvWordOnImage.isVisible = true
        binding.tvWordOnImage.text = trainView.word.word

        bottomBinding.statusTV.setOnClickListener {
            getBaseView()?.cancelWordRecognizer()
        }
    }

    private fun setRecognizedWords(recognizedWord: String, currentWord: Word) {
        if (recognizedWord.isNotEmpty()) {
            val result = recognizedWord.lowercase()
            if (result == currentWord.translate.lowercase()) {
                fragment.getBaseView()?.onSuccess(AnimationType.Fireworks)

            } else {
                fragment.getBaseView()?.onFail(result)
            }
        }
    }

    private fun viewChangeAnimation(view: View?) {
        fragment.binding.bottomViewContainer.addView(view)
        view?.apply {
            alpha = 0f
            isVisible = true

            animate()
                .alpha(1f)
                .setDuration(100)
                //.setInterpolator(AccelerateInterpolator())
                .start()
        }
    }

    override fun onEndButtonClick() {
        val action =
            FragmentTrainingWordsDirections.actionFragmentTrainingWordsToWordsInWordSetFragment(args.wordSetId)
        findNavController().navigate(action)
    }
}

private const val RMS_DB_MIN = 3f