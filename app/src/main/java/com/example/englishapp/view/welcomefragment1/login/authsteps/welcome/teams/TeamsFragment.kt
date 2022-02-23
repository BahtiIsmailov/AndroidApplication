package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentMotivationBinding
import com.example.englishapp.databinding.FragmentTeamsBinding
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener


class TeamsFragment : Fragment(),ElementOnFragmentClickLListener {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TeamsAdapter(this)
        binding.recycleTeams.adapter = adapter

        (parentFragment as BaseWelcomeFragment).getTextView().text = "Какие темы тебе интересны?"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onElementClick(value:String) {
        val parentFragment = (parentFragment as BaseWelcomeFragment)
        parentFragment.getContinueButton().isVisible = true
        parentFragment.parentViewModel.setFewValueForMapWelcomeDataForServer("teams",value)
    }
}