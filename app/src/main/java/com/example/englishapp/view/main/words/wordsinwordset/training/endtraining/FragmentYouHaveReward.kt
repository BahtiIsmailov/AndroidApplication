package com.example.englishapp.view.main.words.wordsinwordset.training.endtraining

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentYouHaveRewardBinding

class FragmentYouHaveReward: DialogFragment() {
    private var _binding: FragmentYouHaveRewardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYouHaveRewardBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun getTheme() = R.style.RoundedCornersDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val xp = arguments?.getInt("XP")?:0
        val energy = arguments?.getInt("Energy")?:0
        val stars = arguments?.getDouble("Stars")?:0
        with(binding){
              starsTv.text = stars.toString()
            EnergyTV.text = energy.toString()
            XPTV.text =xp.toString()
            buttonReady.setOnClickListener {
                dismiss()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun getInstance(xp: Int, stars: Double, energy: Int): FragmentYouHaveReward{
            val youHaveReward = FragmentYouHaveReward()
            val bundle = Bundle().apply {
                putInt("XP",xp)
                putDouble("Stars",stars)
                putInt("Energy",energy)
            }
            youHaveReward.arguments = bundle
            return youHaveReward
        }

    }


}