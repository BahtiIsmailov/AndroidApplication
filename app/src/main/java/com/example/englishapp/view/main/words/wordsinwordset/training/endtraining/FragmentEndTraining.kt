package com.example.englishapp.view.main.words.wordsinwordset.training.endtraining

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.GraphDirections
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentEndTrainingBinding
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems.HeaderItem
import com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems.WordItem
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import com.xwray.groupie.GroupieAdapter
import java.security.acl.Group


class FragmentEndTraining : BaseFragment() {

    val viewModel: EndTrainingViewModel by viewModels()
    private var _binding: FragmentEndTrainingBinding? = null
    private val binding get() = _binding!!
    private val args: FragmentEndTrainingArgs by navArgs()
    val adapter = GroupieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEndTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSeekBar(0)

        binding.lmsTechnology.setOnClickListener {
            val lms_technology = FragmentShowLmsTechnology.getInstance()
            lms_technology.show(childFragmentManager, null)
        }
        binding.close.setOnClickListener {
            val action =
                FragmentEndTrainingDirections.actionFragmentEndTrainingToWordsInWordSetFragment(args.wordSetId)
            findNavController().navigate(action)
        }
        binding.trainingAgain.setOnClickListener {
            val action =
                FragmentEndTrainingDirections.actionFragmentEndTrainingToWordsInWordSetFragment(args.wordSetId)
            findNavController().navigate(action)
        }

        viewModel.getEndTrainingInfo(
            args.endTrainingResult.wordsIds,
            args.endTrainingResult.total,
            args.endTrainingResult.error,
            args.trainedWords.toList(),
            args.wordSetId
        )

        viewModel.trainingWordsResultLiveData.observeUIState(
            onLoading = {

            },
            onError = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            },
            onSuccess = { result ->
                binding.recycle.adapter = adapter
                //adapter.addAll(result.trainedWords.map { WordItem(it) })
                result.trainedWords.forEach {
                    adapter.add(WordItem(it))
                }
                initSeekBar(result.progress)

                if (result.allWordsIsKnown) {
                    binding.trainingAgain.text = resources.getString(R.string.go_to_wordset)
                    binding.footerConstraint.isGone = true
                    val count = result.trainedWords.size
                    binding.sentencesTV.text = requireContext().resources.getQuantityString(
                        R.plurals.all_words_trained,
                        count,
                        count.toString()
                    )
                    binding.trainingAgain.setOnClickListener {
                        val action = GraphDirections.actionGlobalWordsFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        )


        viewModel.endTrainingInfoLiveData.observeUIState(
            onLoading = {
                binding.progressBarLoading.isVisible = true
                binding.progressBarLoading.playAnimation()
            },
            onError = {
                binding.progressBarLoading.isGone = true
                binding.progressBarLoading.cancelAnimation()
            },
            onSuccess = { endTrainingInfo ->
                binding.maiinCl.isVisible = true
                binding.progressBarLoading.isGone = true
                val dialog = FragmentYouHaveReward.getInstance(
                    endTrainingInfo.xp,
                    endTrainingInfo.tokens,
                    endTrainingInfo.energy_now
                )
                dialog.show(childFragmentManager, "you_have_reward")

            }
        )


    }

    private fun initSeekBar(progress: Int) {
        binding.seekBar.hideThumb(true)
        binding.seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                binding.seekBarLayout.findViewById<TextView>(R.id.textView).text =
                    "${seekParams.progress}%"
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }

        binding.seekBar.setProgress(progress.toFloat())
        setStarByProgress(progress.toInt())

        binding.seekBar.setOnTouchListener { _, _ -> true }

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
