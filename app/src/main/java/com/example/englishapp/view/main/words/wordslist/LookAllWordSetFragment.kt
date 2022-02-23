package com.example.englishapp.view.main.words.wordslist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentLookAllWordSetBinding
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.Wordset
import com.example.englishapp.utils.*
import com.example.englishapp.view.customview.GridAutofitLayoutManager
import com.example.englishapp.view.main.base.BaseFragment
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

class LookAllWordSetFragment() : BaseFragment(useCustomInsets = true), WordSetAdapterListener {

    //private val viewModel: Welcome1ViewModel by viewModels
    private var _binding: FragmentLookAllWordSetBinding? = null
    private val binding get() = _binding!!

    private val args: LookAllWordSetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookAllWordSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FeatureFlags.enableFullScreen) {
            binding.cl.setStatusBarPadding()
        }

        binding.titleTextView.text = args.wordGroup.title

        binding.countWordsTextView.text = resources.getQuantityString(
            R.plurals.word_sets_count,
            args.wordGroup.wordsets?.size ?: 0,
            args.wordGroup.wordsets?.size?.toString() ?: 0
        )

        val adapter = WordSetAdapter(this, showAll = true, gridLayout = true)
        adapter.setWordGroup(args.wordGroup)
        binding.recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                12.toPx().toInt(),
                false,
                0
            )
        )
        binding.recycleView.adapter = adapter

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickWordSet(wordset: Wordset) {
        val action =
            LookAllWordSetFragmentDirections.actionLookAllWordSetFragmentToWordsInWordSet(
                wordset.id
            )
        findNavController().navigate(action)
    }

    override fun onClickOther(wordGroup: WordGroup) {
    }
}