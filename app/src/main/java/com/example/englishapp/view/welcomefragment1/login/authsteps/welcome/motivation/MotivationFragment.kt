package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.motivation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentMotivationBinding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener


class MotivationFragment : BaseFragment(),ElementOnFragmentClickLListener {

    private var _binding:FragmentMotivationBinding? = null
    private val  binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding  = FragmentMotivationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MotivationFragmentAdapter(this)
        binding.recycleMotivation.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleMotivation.adapter = adapter

        with(parentFragment as BaseWelcomeFragment) {
             getTextView().text =
                "Какая у тебя мотивация учить английский?"
             getTextViewForSomethingFragment().isVisible =
                true
             getTextViewForSomethingFragment().text =
                "Можешь выбрать несколько вариантов"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onElementClick(value:String) {
        val parentFragment = (parentFragment as BaseWelcomeFragment)
        parentFragment.getContinueButton().isVisible = true
        parentFragment.parentViewModel.setFewValueForMapWelcomeDataForServer("motivation",value)
    }


}