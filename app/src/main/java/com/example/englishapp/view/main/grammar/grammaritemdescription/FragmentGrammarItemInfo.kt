package com.example.englishapp.view.main.grammar.grammaritemdescription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.GraphDirections
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentGrammarBinding
import com.example.englishapp.databinding.FragmentGrammarItemInfoBinding
import com.example.englishapp.databinding.GrammarItemBinding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.GrammarMainPageAdapter
import com.example.englishapp.view.main.grammar.GrammarMainPageViewModel
import com.example.englishapp.view.main.words.wordsinwordset.learning.FragmentLearningWordsArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentGrammarItemInfo : BaseFragment() {

    private val viewmodel: GrammarDataItemInfoViewModel by viewModels()
    private var _binding: FragmentGrammarItemInfoBinding? = null
    private val binding get() = _binding!!

    private val args: FragmentGrammarItemInfoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrammarItemInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FRagmentGrammarItemInfoAdapter()
        binding.recycle.adapter = adapter

        binding.trainingRules.setOnClickListener {
           findNavController().popBackStack()
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack(R.id.grammarTrainingFragment, true)
        }
        viewmodel.getGrammarDataIndo(args.grammarItem.grammarLink)
        viewmodel.getGrammarItemInfoLiveData.observeUIState(
            onSuccess = {
                binding.progress.cancelAnimation()
                binding.progress.isGone = true
                binding.mainNestedScrollView.isVisible = true
                adapter.submitList(it.items)
                binding.header.text = it.header
                binding.title.text = it.title
            },
            onError = {
                binding.progress.cancelAnimation()
                binding.progress.isGone = true
            },
            onLoading = {
                binding.mainNestedScrollView.isGone = true
                binding.progress.isVisible = true
                binding.progress.playAnimation()
            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}