package com.example.englishapp.view.main.words.wordslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.databinding.FragmentWordsBinding
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.Wordset
import com.example.englishapp.utils.*
import com.example.englishapp.view.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

@AndroidEntryPoint
class WordsFragment : BaseFragment(useCustomInsets = true), WordSetAdapterListener {

    private val viewModel: WordsViewModel by viewModels()
    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!

    private val wordsGroupAdapter by lazy {
        WordsGroupAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FeatureFlags.enableFullScreen) {
            binding.recycleViewWordsGroups.setStatusBarPadding()
        }

        initRecyclerView()
        bindViewModel()
    }

    private fun initRecyclerView() {
        binding.recycleViewWordsGroups.adapter = wordsGroupAdapter
    }

    private fun bindViewModel() {
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is WordsUiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.progressBar.playAnimation()
                }
                is WordsUiState.Data -> {
                    wordsGroupAdapter.lastWordSet = it.lastWordSet
                    wordsGroupAdapter.submitList(it.wordGroups)
                    binding.progressBar.isVisible = false
                    binding.progressBar.cancelAnimation()
                }
                is WordsUiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.progressBar.cancelAnimation()
                }
            }
        }

        viewModel.lookAllClickEvent.observe(viewLifecycleOwner, EventObserver { wordGroup ->
            val action =
                WordsFragmentDirections.actionWordsFragmentToLookAllWordSetFragment(
                    wordGroup
                )
            findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickWordSet(wordset: Wordset) {
        val action =
            WordsFragmentDirections.actionWordsFragmentToWordsInWordSet(
                wordset.id
            )
        findNavController().navigate(action)
    }

    override fun onClickOther(wordGroup: WordGroup) {
        val action =
            WordsFragmentDirections.actionWordsFragmentToLookAllWordSetFragment(
                wordGroup
            )
        findNavController().navigate(action)
    }
}