package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startcheckvocabulary.trainingwordinwelcome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.englishapp.databinding.FragmentTrainingWordInWelcomeBinding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingWordInWelcomeFragment : BaseFragment() {

    private var _binding: FragmentTrainingWordInWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrainingWordInWelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingWordInWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentFragment = (parentFragment as BaseWelcomeFragment)
        viewModel.getDataForTrainingInWelcome(
            parentFragment.parentViewModel.workWithUniqueValueTeams(),
            parentFragment.parentViewModel.getValueFromMapWelcomeDataForServer("language"),
            "en"
        )

        viewModel.getDataForTrainingLiveData.observeUIState {
            Log.d("DataForTrainingWelcome", it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}