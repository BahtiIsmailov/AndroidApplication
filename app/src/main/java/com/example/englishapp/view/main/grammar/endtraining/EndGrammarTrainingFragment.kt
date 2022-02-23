package com.example.englishapp.view.main.grammar.endtraining

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentEndGrammarTrainingBinding
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.view.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class EndGrammarTrainingFragment : BaseFragment() {

    private val viewModel: EndTrainingViewModel by viewModels()
    private var _binding: FragmentEndGrammarTrainingBinding? = null
    private val binding get() = _binding!!

    //private val args: EndGrammarTrainingFragmentArgs? by navArgs()
    private val args: EndGrammarTrainingFragmentArgs? = null

    private val gameId: String by lazy { args?.gameId ?: "d5dd6517-78de-457d-91c2-af860f35799e" }
    private val stars: Int by lazy { args?.stars ?: 3 }
    private val points: String by lazy { args?.points?.toString() ?: "300" }

    //private val grammarMainData: GrammarMainData? by lazy { args?.grammarMainData }
    private val grammarMainData: GrammarMainData? by lazy {
        GrammarMainData(
            true, "2", false, "", "", false, "Name", 10, 3, listOf()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEndGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.endGrammarTrainingInfo(
            gameId = gameId,
            id = UUID.randomUUID().toString().replace("-", "").replace("[a-z A-Z]".toRegex(), ""),
            result = stars.toString(),
            value = points
        )


        viewModel.endTrainingGrammarInfoWithRewardsLiveData.observeUIState(
            onLoading = {

            },
            onError = {

            },
            onSuccess = { endTrainingInfo ->
                val dialog = FragmentYouHaveRevardFromGrammarTraining.getInstance(
                    endTrainingInfo.xp,
                    endTrainingInfo.tokens,
                    endTrainingInfo.energy_now
                )
                dialog.show(childFragmentManager, null)
                binding.reiting.text = (endTrainingInfo.level_up?.level ?: 0).toString()
            }
        )



        SoundPlayer.play(R.raw.lms_moto_stop)
        SoundPlayer.play(R.raw.lms_moto_fanfare)

        binding.points.text = points

        val countStars = stars
        with(binding) {
            when (countStars) {
                1 -> {
                    stars.setAnimation(R.raw.finish_dialog_one_start)
                    stars.playAnimation()
                }
                2 -> {
                    stars.setAnimation(R.raw.finish_dialog_two_start)
                    stars.playAnimation()
                }
                3 -> {
                    stars.setAnimation(R.raw.finish_dialog_three_start)
                    stars.playAnimation()
                }
            }

            if (countStars < 3) {
                userNotHaveThreeStar()
            }

            trainingAgain.setOnClickListener {
                grammarMainData?.let {
                    findNavController().popBackStack(R.id.grammarTrainingFragment, true)
                }
            }
            closeEndTraining.setOnClickListener {
                grammarMainData?.let {
                    findNavController().popBackStack(R.id.grammarTrainingFragment, true)
                }
            }
            cardview.setOnClickListener {
                grammarMainData?.let {
                    val action =
                        EndGrammarTrainingFragmentDirections.actionEndGrammarTrainingFragmentToGetStatisticFromOtherPerson(
                            it
                        )
                    findNavController().navigate(action)
                }
            }
        }

    }

    fun userNotHaveThreeStar() {
        with(binding) {
            kubok.isGone = true
            lock.isVisible = true
            reiting.isGone = true
            reitingIsNotAvailable.isVisible = true
            staticPlaceInReiting.isGone = true
            textIfNotThreeStar.isVisible = true

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}