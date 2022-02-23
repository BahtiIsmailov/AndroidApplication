package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooselanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.GraphDirections
import com.example.englishapp.databinding.FragmentChooseLanguageBinding
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener
import dagger.hilt.android.AndroidEntryPoint

class ChooseLanguageFragment : BaseFragment(), ElementOnFragmentClickLListener {

    private var _binding: FragmentChooseLanguageBinding? = null
    private val binding get() = _binding!!

    private val viewModels: ChooseLanguageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModels.getTokensForWelcome()
        viewModels.getLanguages()
        viewModels.languagesInfo.observeUIState(
            onSuccess = {
                binding.progressBar.cancelAnimation()
                binding.progressBar.isGone = true
                val adapter = ChooseLanguageAdapter(this)
                adapter.submitList(it)
                binding.recycleLanguage.adapter = adapter
            },
            onError = {
                Toast.makeText(requireContext(), "Server not access", Toast.LENGTH_SHORT).show()
                val action = GraphDirections.actionGlobalWelcomeFragment12()
                findNavController().navigate(action)
            },
            onLoading = {
                binding.progressBar.playAnimation()
                binding.progressBar.isVisible = true
            }
        )
        (parentFragment as BaseWelcomeFragment).getTextView().text = "Какой твой родной язык?"


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onElementClick(value:String) {
        (parentFragment as BaseWelcomeFragment).getContinueButton().isVisible = true
        val parentFragment = (parentFragment as BaseWelcomeFragment)
        parentFragment.parentViewModel.setDataForMapWelcomeDataForServer("language",value)

    }


}