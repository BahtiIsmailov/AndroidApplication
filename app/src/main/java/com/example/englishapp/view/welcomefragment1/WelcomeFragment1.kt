package com.example.englishapp.view.welcomefragment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.englishapp.databinding.FragmentWelcome1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment1 : Fragment(){
    private var _binding: FragmentWelcome1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcome1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.setOnClickListener {
            val action = WelcomeFragment1Directions.actionWelcomeFragment1ToAuthLoginFragment()
            findNavController().navigate(action)
         }

         binding.buttonNewUser.setOnClickListener {
            val action = WelcomeFragment1Directions.actionWelcomeFragment1ToCreateEmailAndPasswordFragment()
             findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
