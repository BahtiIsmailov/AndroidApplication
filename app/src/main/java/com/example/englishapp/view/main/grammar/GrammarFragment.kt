package com.example.englishapp.view.main.grammar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.databinding.FragmentGrammarBinding
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.view.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GrammarFragment : BaseFragment(), GrammarAdapterListener {

    private val viewmodel: GrammarMainPageViewModel by viewModels()
    private var _binding: FragmentGrammarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrammarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getGrammarInfo()
        val adapter = GrammarMainPageAdapter(this)
        binding.recycle.adapter = adapter
        binding.recycle.isNestedScrollingEnabled = true
        viewmodel.getGrammarInfoForMainPageLiveData.observeUIState(
            onSuccess = {
                binding.progress.cancelAnimation()
                binding.progress.isGone = true
                binding.mainNestedScrollView.isVisible = true
                adapter.submitList(it)
            },
            onError = {

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

    override fun onGrammarItemClick(grammarItem: GrammarMainData) {
        val action =
            GrammarFragmentDirections.actionGrammarFragmentToGrammarTrainingFragment(grammarItem)
        findNavController().navigate(action)
    }
}
