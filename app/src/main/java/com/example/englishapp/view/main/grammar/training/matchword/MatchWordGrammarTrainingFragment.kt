package com.example.englishapp.view.main.grammar.training.matchword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentMatchWordGrammarTrainingBinding
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.utils.SoundGrammarPlayer
import com.example.englishapp.utils.toPx
import com.example.englishapp.view.customview.WhitePushButtonView
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.training.interfaces.GrammarQuestionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchWordGrammarTrainingFragment : BaseFragment() {

    private val viewmodel: MatchWordGrammarTrainingViewModel by viewModels()
    private var _binding: FragmentMatchWordGrammarTrainingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchWordGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Question>("question")?.let { question ->
            viewmodel.initializeViewModel(question)
            viewmodel.orginalPhraseLiveData.observe {
                binding.originalWord.text = it
            }
            viewmodel.translationPhraseLiveData.observe {
                binding.translateWord.text = it
            }
            viewmodel.allVariantsWordsLiveData.observe { listWord ->
                for (word in listWord) {
                    val whitePushButton = WhitePushButtonView(requireContext())
                    whitePushButton.text = word.replace("_", " ")
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        53f.toPx().toInt()
                    )
                    layoutParams.setMargins(10, 10, 10, 10)

                    whitePushButton.layoutParams = layoutParams
                    binding.containerWhitePushButtons.addView(whitePushButton)
                    whitePushButton.setOnClickListener {
                        SoundGrammarPlayer.playPushButtonAndOtherMusic()
                        val text = whitePushButton.text
                        viewmodel.userPressOnWhiteButton(text)
                    }

                }
                viewmodel.errorChooseWordEvent.observeEvent {
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_wrong_answer)
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_brake)
                    (parentFragment as GrammarQuestionsListener).onErrorGrammarResult(
                        it.question,
                        it.originalPhrase,
                        it.errorPhrase
                    )
                }
                viewmodel.successChooseWordEvent.observeEvent {
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_correct_answer)
                    SoundGrammarPlayer.playAnswerMusic(R.raw.lms_moto_speed)
                    (parentFragment as GrammarQuestionsListener).onSuccessGrammarResult(question)
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(question: Question): MatchWordGrammarTrainingFragment {
            val bundle = Bundle()
            bundle.putParcelable("question", question)
            val fragment = MatchWordGrammarTrainingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}