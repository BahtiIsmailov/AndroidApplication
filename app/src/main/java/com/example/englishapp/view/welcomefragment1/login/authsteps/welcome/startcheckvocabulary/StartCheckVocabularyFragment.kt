package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startcheckvocabulary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.englishapp.databinding.FragmentStartCheckVocabularyBinding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartCheckVocabularyFragment : BaseFragment() {
    private var _binding: FragmentStartCheckVocabularyBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartCheckVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(parentFragment as BaseWelcomeFragment) {
            getTextView().isGone = true
            getTextViewForSomethingFragment().isGone = true
            setTextForContinueButton("Начать")
            getContinueButton().isVisible = true
            getContinueButton().setOnClickListener {
                parentViewModel.upCurrentIndexFragmentContainers()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(parentFragment as BaseWelcomeFragment) {
            getContinueButton().isInvisible = true
            setTextForContinueButton("Начать")
            getTextView().isVisible = true
            getTextViewForSomethingFragment().isVisible = true
        }
        _binding = null
    }


}