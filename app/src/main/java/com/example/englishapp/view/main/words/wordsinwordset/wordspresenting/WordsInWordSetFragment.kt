package com.example.englishapp.view.main.words.wordsinwordset.wordspresenting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.GraphDirections
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentWordsInWordSetBinding
import com.example.englishapp.utils.StatusBarColor
import com.example.englishapp.utils.toPx
import com.example.englishapp.view.main.base.BaseFragment
import com.google.android.material.appbar.AppBarLayout
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import com.xwray.groupie.GroupieAdapter
import kotlin.math.abs
import androidx.core.view.ViewCompat

import androidx.core.view.WindowInsetsCompat

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.view.OnApplyWindowInsetsListener
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.example.englishapp.utils.FeatureFlags
import com.example.englishapp.utils.setStatusBarPadding
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf


class WordsInWordSetFragment : BaseFragment(useCustomInsets = true) {
    private val viewModel: WordsInWordSetViewModel by viewModels()
    private var _binding: FragmentWordsInWordSetBinding? = null
    private val binding get() = _binding!!
    private val args: WordsInWordSetFragmentArgs by navArgs()
    val adapter = GroupieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordsInWordSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSeekBar()

        viewModel.getWordsInWordSet(args.id)

        bindViewModel()

        binding.recyclerViewWord.adapter = adapter

        binding.backClickView.setOnClickListener {
            val action = GraphDirections.actionGlobalWordsFragment()
            findNavController().navigate(action)
        }

        addSwipeLeftToRightListener(binding.root) {
            val action = GraphDirections.actionGlobalWordsFragment()
            findNavController().navigate(action)
        }

        val alphaStart = 0.2f
        val threshold = alphaStart - 0.1f

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percent = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            val positionOnPercent = 1f - percent
            binding.tvToolbar.isVisible =
                abs(verticalOffset).toFloat() - appBarLayout.totalScrollRange > (-50).toPx()

            binding.ivBackBg.alpha = positionOnPercent
            binding.ivBackBgShadow.alpha = positionOnPercent

            if (positionOnPercent < alphaStart) {
                binding.mainTopFoto.alpha = (positionOnPercent - threshold) * 10f

            } else {
                binding.mainTopFoto.alpha = 1f
            }
        })
    }

    private fun initSeekBar() {
        binding.seekBar.hideThumb(true)

        binding.seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                binding.seekBarLayout.findViewById<TextView>(R.id.textView).text =
                    "${seekParams.progress}%"
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }

        binding.seekBar.max = 100f
        binding.seekBar.setProgress(0f)
        setStarByProgress(0)

        binding.seekBar.setOnTouchListener { _, _ -> true }
        binding.tvToolbar.isVisible = false
    }

    private fun bindViewModel() {
        viewModel.wordsInWordsSetLiveData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is WordsInWordSetUiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.progressBar.playAnimation()
                    binding.appbar.isVisible = false
                    binding.scrollView.isVisible = false
                }

                is WordsInWordSetUiState.Data -> {
                    Glide.get(requireContext()).setMemoryCategory(MemoryCategory.HIGH)
                    uiState.main.words.flatMap { it.pictures }.forEach {
                        Glide.with(this).load(it.url).preload()
                    }

                    binding.progressBar.isGone = true
                    binding.progressBar.cancelAnimation()
                    binding.appbar.isVisible = true
                    binding.scrollView.isVisible = true



                    adapter.clear()
                    adapter.addAll(uiState.words)

                    with(binding) {
                        mainTeamTV.text = uiState.main.topic
                        tvToolbar.text = uiState.main.title
                        subTeam.text = uiState.main.title
                        trainingButton.isVisible = true
                        trainingButton.setOnClickListener {
                            val action =
                                WordsInWordSetFragmentDirections.actionWordsInWordSetFragmentToFragmentLearningWords(
                                    uiState.main
                                )
                            findNavController().navigate(action)
                        }

                        mainTopFoto.setImageUrls(uiState.imgList)

                        Handler(Looper.getMainLooper()).postDelayed({
                            seekBar.setProgress(uiState.main.percent_completed.toFloat())
                            setStarByProgress(uiState.main.percent_completed)
                        }, 500)
                    }
                }
            }

        }
    }

    private fun setStarByProgress(progress: Int) {
        val selectedStar = R.drawable.ic_progress_star_selected
        val unselectedStar = R.drawable.ic_progress_star_unselected

        binding.ivStart1.setImageResource(unselectedStar)
        binding.ivStart2.setImageResource(unselectedStar)
        binding.ivStart3.setImageResource(unselectedStar)

        if (progress >= 5) {
            binding.ivStart1.setImageResource(selectedStar)
        }

        if (progress >= 50) {
            binding.ivStart2.setImageResource(selectedStar)
        }

        if (progress == 100) {
            binding.ivStart3.setImageResource(selectedStar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}