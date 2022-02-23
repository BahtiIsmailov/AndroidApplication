package com.example.englishapp.view.main.grammar.endtraining

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentGetStatisticFromOtherPersonBinding
import com.example.englishapp.view.main.base.BaseBottomSheetDialogFragment
import com.example.englishapp.view.main.grammar.endtraining.adapter.StatisticViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.bottomsheet.BottomSheetBehavior

import androidx.recyclerview.widget.LinearLayoutManager

import android.app.Dialog
import androidx.core.os.bundleOf

import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.databinding.StatisticItemPageBinding
import com.example.englishapp.model.data.grammartraining.endgrammartraining.TopPlayerDto
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.endtraining.adapter.TopPlayersAdapter

import com.google.android.material.bottomsheet.BottomSheetDialog


@AndroidEntryPoint
class FragmentStatisticPage : BaseFragment() {

    private val viewModel: EndTrainingViewModel by viewModels()
    private var _binding: StatisticItemPageBinding? = null
    private val binding get() = _binding!!

    val items: ArrayList<TopPlayerDto> by lazy {
        arguments?.getParcelableArrayList("items") ?: arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatisticItemPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.tvTitle.text = "Топ ${items.size} игроков"

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = TopPlayersAdapter()
        }

        (binding.recyclerView.adapter as TopPlayersAdapter).submitList(items)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(items: List<TopPlayerDto>) =
            FragmentStatisticPage().apply {
                arguments = bundleOf("items" to items)
            }
    }
}