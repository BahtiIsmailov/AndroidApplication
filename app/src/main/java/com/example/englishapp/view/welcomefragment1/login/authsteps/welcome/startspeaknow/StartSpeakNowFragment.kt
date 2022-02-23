package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startspeaknow

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentStartSpeakNowBinding
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.view.main.words.wordsinwordset.training.base.WordRecognizer
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment


class StartSpeakNowFragment : Fragment() {

    private var _binding: FragmentStartSpeakNowBinding? = null
    private val binding get() = _binding!!

    private val wordRecognizer: WordRecognizer by lazy {
        WordRecognizer(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartSpeakNowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SoundPlayer.playWordAndStartAnimation(
            R.raw.start_talking_phrase,
            binding.lottieAlexDialog,
            binding.textForChanged
        )

        with(parentFragment as BaseWelcomeFragment) {
            getTextView().text = "Начни говорить уже сейчас"
            getTextViewForSomethingFragment().text =
                "Потребуется доступ к микрофону и распознованию речи"
            val imageView = parentViewModel.getValueFromMapWelcomeDataForServer("avatar")
            parentViewModel.getImageFromChooseYourStyle(imageView, binding.userImage)
            binding.userName.text = parentViewModel.getValueFromMapWelcomeDataForServer("nickname")
            getContinueButton().isInvisible = true
            val speechButton = realizeSpeechButtonContainerText()
            speechButton.setOnClickListener {
                wordRecognizer.recognizeWords(
                    registry = requireActivity().activityResultRegistry,
                    onError = {
                        binding.badListen.isVisible = true
                        binding.settingsLater.isGone = true
                        realizeSpeechButtonContainerText(false)
                    },
                    onRmsChanged = {

                    },
                    onResults = {
                        realizeSpeechButtonContainerText(false)
                        Log.d("itString",it)
                        if (it == "hello"){
                            binding.settingsLater.isGone = true
                            binding.successSpeechWord.isVisible = true
                            parentViewModel.upCurrentIndexFragmentContainers()
                        }
                    },
                    onReadyForSpeech = {
                        binding.badListen.isGone = true
                        realizeSpeechButtonContainerText(true)
                    }
                )
            }

            binding.settingsLater.setOnClickListener {
                parentViewModel.upCurrentIndexFragmentContainers()
            }

        }

        binding.staticText.setOnClickListener {
            SoundPlayer.playWordAndStartAnimation(
                R.raw.start_talking_phrase,
                binding.lottieAlexDialog,
                binding.textForChanged
            )

        }
        binding.staticText.text = styleTextHello(binding.staticText)

    }

//TODO(доделать он видит слово хелло видит текст из текст вью но не стилизует его в логах не понятно просто текст)
    private fun styleTextHello(textView: TextView): SpannableStringBuilder {
        val text = SpannableStringBuilder(textView.text)
        val wordHello = getWordHelloFromText(text)
        val resultText = SpannableStringBuilder(wordHello)
        resultText.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            resultText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        resultText.setSpan(
            BackgroundColorSpan(
                resources.getColor(android.R.color.holo_orange_light, requireContext().theme)
            ),
            0,
            resultText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        resultText.setSpan(
            UnderlineSpan(),
            0,
            resultText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return text
    }


    private fun getWordHelloFromText(text: CharSequence): String {
        var count = 0
        var wordHello = ""
        for ((index, i) in text.withIndex()) {
            if (i == 'H') {
                for (letter in index until text.length) {
                    wordHello += text[letter]
                    count++
                    if (count == 5) {
                        break
                    }
                }
                break
            }
        }
        return wordHello
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (parentFragment as BaseWelcomeFragment).realizeSpeechButtonContainerText().isGone = true
        _binding = null
    }


}