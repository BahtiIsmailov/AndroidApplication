package com.example.englishapp.view.main.grammar.erroranswer

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentGrammarErrorAnswerBinding
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.utils.joinToStringForSpannable

class ErrorAnswerFragment : Fragment() {

    private var _binding: FragmentGrammarErrorAnswerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrammarErrorAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.bounds_for_error_grammar_answer
        )
        binding.cardView.isVisible = true
        binding.cardView.startAnimation(anim)
        arguments?.getParcelable<Question>("question")?.let { question ->
            binding.originalPhraseInError.text =
                replaceOtherLetter(question.translation)
        }
        arguments?.getString("originalPhrase")?.let { originalPhrased ->
            val originalPhrase = replaceOtherLetter(originalPhrased)
            arguments?.getString("errorPhrase")?.let { errorPhrased ->
                val errorPhrase = replaceOtherLetter(errorPhrased)
                stylizeWordInIncomingData(originalPhrase.split(" "), errorPhrase.split(" "))
            }
        }
    }

    fun stylizeWordInIncomingData(originalPhrase: List<String>, errorPhrase: List<String>) {
        val spannableForErrorWords = errorPhrase.map {
            SpannableString(it)
        }
        val spannableForOriginalWords = originalPhrase.map {
            SpannableString(it)
        }

        for (errorWord in spannableForErrorWords) {
            if (!spannableForOriginalWords.contains(errorWord)) {
                val foregroundSpan = ForegroundColorSpan(
                    requireContext().resources.getColor(
                        R.color.red,
                        requireContext().theme
                    )
                )
                errorWord.setSpan(
                    foregroundSpan,
                    0,
                    errorWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                errorWord.setSpan(
                    StrikethroughSpan(),
                    0,
                    errorWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        for (correctWord in spannableForOriginalWords){
            if (!spannableForErrorWords.contains(correctWord)){
                val foregroundSpan = ForegroundColorSpan(
                    requireContext().resources.getColor(
                        R.color.green,
                        requireContext().theme
                    )
                )
                correctWord.setSpan(
                    foregroundSpan,
                    0,
                    correctWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                correctWord.setSpan(
                    UnderlineSpan(),
                    0,
                    correctWord.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }


        binding.notCorrectPhrase.text =
            SpannableString(spannableForErrorWords.joinToStringForSpannable(" ")) // верхнее

        binding.correctAnswer.text =
            SpannableString(spannableForOriginalWords.joinToStringForSpannable(" ")) //  нижннее

    }


    fun replaceOtherLetter(word:String): CharSequence {
        return word.replace("_", " ").replace("{", "").replace("}", "")
    }

    companion object {
        fun newInstance(
            question: Question,
            originalPhrase: String,
            errorPhrase: String
        ): ErrorAnswerFragment {
            val bundle = Bundle()
            bundle.putParcelable("question", question)
            bundle.putString("originalPhrase", originalPhrase)
            bundle.putString("errorPhrase", errorPhrase)
            val errorAnswerFragment = ErrorAnswerFragment()
            errorAnswerFragment.arguments = bundle
            return errorAnswerFragment

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}