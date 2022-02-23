package com.example.englishapp.view.main.grammar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.englishapp.databinding.FragmentGrammarBeginTrainingBinding
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.utils.SoundGrammarPlayer
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.training.interfaces.GrammarBeginTrainingListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GrammarBeginTrainingFragment : BaseFragment() {
    private var _binding: FragmentGrammarBeginTrainingBinding? = null
    private val binding get() = _binding!!
    private val grammarItem: GrammarMainData? by lazy { arguments?.getParcelable(GRAMMAR_ITEM) }

    private var grammarBeginTrainingListener: GrammarBeginTrainingListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrammarBeginTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        grammarBeginTrainingListener = (parentFragment as? GrammarBeginTrainingListener)
        super.onAttach(context)
    }

    override fun onDetach() {
        grammarBeginTrainingListener = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialMusicForTraining()


        with(binding) {
            title.text = grammarItem?.header ?: ""
            header.text = grammarItem?.name ?: ""
            progressBarStar.learning_progress = grammarItem?.stars ?: 0
            readRulls.setOnClickListener {
                grammarBeginTrainingListener?.onReadRuleClick()
            }
            training.setOnClickListener {
                grammarBeginTrainingListener?.onBeginTraining()
            }
            createChip.elementsWord = grammarItem?.tags ?: listOf()
        }

    }

    private fun initialMusicForTraining() {
        SoundGrammarPlayer.playMusicBeginTraining()
    }

    companion object {
        private const val GRAMMAR_ITEM = "grammarItem"
        fun newInstance(grammarItem: GrammarMainData) = GrammarBeginTrainingFragment()
            .apply {
                arguments = bundleOf(GRAMMAR_ITEM to grammarItem)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
