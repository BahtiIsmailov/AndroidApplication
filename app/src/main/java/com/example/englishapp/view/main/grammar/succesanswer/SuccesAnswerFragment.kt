package com.example.englishapp.view.main.grammar.succesanswer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.englishapp.databinding.FragmentGrammarSuccesAnswerBinding
import com.example.englishapp.model.data.grammartraining.Question
import com.example.englishapp.view.main.base.BaseFragment

class SuccesAnswerFragment : BaseFragment(){

     private var _binding: FragmentGrammarSuccesAnswerBinding? = null
     private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrammarSuccesAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardView.isVisible = true
        arguments?.getParcelable<Question>("question")?.let { question ->
            binding.originalPhraseInSucces.text = question.translation
            binding.phraseInEnglishLangguage.text = question.phrase.replace("_"," ").replace("{","").replace("}","")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(question: Question): SuccesAnswerFragment {
            val bundle = Bundle()
            bundle.putParcelable("question", question)
            val fragment = SuccesAnswerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}