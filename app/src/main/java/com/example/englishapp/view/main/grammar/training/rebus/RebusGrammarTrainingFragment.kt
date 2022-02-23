package com.example.englishapp.view.main.grammar.training.rebus

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentRebusGrammarTrainingBinding
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.utils.SoundGrammarPlayer
import com.example.englishapp.view.customview.RebusGrammarButtonView
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.training.interfaces.GrammarQuestionsListener
import dagger.hilt.android.AndroidEntryPoint
import org.apmem.tools.layouts.FlowLayout

@AndroidEntryPoint
class RebusGrammarTrainingFragment : BaseFragment() {

    private val viewModel: RebusGrammarTrainingViewModel by viewModels()
    private var _binding: FragmentRebusGrammarTrainingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRebusGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Question>("question")?.let { question ->
            val level = arguments?.getString("level") ?: ""

            with(viewModel) {
                initCurrentViewModel(question, level)
                translationPhraseLiveData.observe {
                    binding.translationWord.text = it
                }

                showDeleteWordButtonLiveData.observe {
                    binding.deleteWord.isVisible = it
                }
                allPhraseWordsLiveData.observe { originalPhrase  ->
                    binding.llcontainer.removeAllViews()
                    for (word in originalPhrase.map {
                        replaceOtherLetter(it)
                    }) {

                        val button = RebusGrammarButtonView(requireContext())

                        val userWritedWords = userWritedWordsLiveData.value ?: emptyList()

                        button.showAll = !userWritedWords.contains(word)

                        button.setWord(word)
                        val params = FlowLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(6, 6, 6, 6)
                        params.gravity = Gravity.CENTER
                        button.layoutParams = params
                        binding.llcontainer.addView(button)

                        button.setOnClickListener {
                            if (button.showAll) {
                                SoundGrammarPlayer.playPushButtonAndOtherMusic()
                                userChooseWord(word)
                            }
                        }

                    }
                    binding.deleteWord.setOnClickListener {
                        pushButtonDelete()
                    }
                }

                succesChooseWordEvent.observeEvent {
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_correct_answer)
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_speed)
                    (parentFragment as GrammarQuestionsListener).onSuccessGrammarResult(question)

                }
                errorChooseWordEvent.observeEvent {
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_wrong_answer)
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_brake)
                    (parentFragment as GrammarQuestionsListener).onErrorGrammarResult(
                        it.question,
                        it.originalPhrase,
                        it.errorPhrase
                    )
                }

                userWritedWordsLiveData.observe {
                    binding.textUserWrited.text = it.joinToString(" ")
                    if (binding.textUserWrited.text == "")
                        binding.deleteWord.isGone = true
                }

            }


        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(question: Question, level: String): RebusGrammarTrainingFragment {
            val bundle = Bundle()
            bundle.putParcelable("question", question)
            bundle.putString("level", level)
            val fragment = RebusGrammarTrainingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}