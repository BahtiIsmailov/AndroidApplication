package com.example.englishapp.view.main.grammar.endtraining

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentYouHaveReavardFromGrammarTrainingBinding
import com.example.englishapp.databinding.FragmentYouHaveRewardBinding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.words.wordsinwordset.training.endtraining.FragmentYouHaveReward

class FragmentYouHaveRevardFromGrammarTraining : DialogFragment() {
    private var _binding: FragmentYouHaveReavardFromGrammarTrainingBinding? = null
    private val binding get() = _binding!!
    private val viewModel:EndTrainingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYouHaveReavardFromGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val xp = arguments?.getInt("XP") ?: 0
        val energy = arguments?.getInt("Energy") ?: 0
        val stars = arguments?.getDouble("Stars") ?: 0
        with(binding) {
            starsTv.text = stars.toString()
            EnergyTV.text = energy.toString()
            XPTV.text = xp.toString()
            buttonReady.setOnClickListener {
                dismiss()
            }


        }
    }

    companion object {
        fun getInstance(
            xp: Int,
            stars: Double,
            energy: Int
        ): FragmentYouHaveRevardFromGrammarTraining {
            val youHaveReward = FragmentYouHaveRevardFromGrammarTraining()
            val bundle = Bundle().apply {
                putInt("XP", xp)
                putDouble("Stars", stars)//??????
                putInt("Energy", energy)
            }
            youHaveReward.arguments = bundle
            return youHaveReward
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}