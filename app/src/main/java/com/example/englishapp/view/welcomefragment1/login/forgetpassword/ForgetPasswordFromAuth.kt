package com.example.englishapp.view.welcomefragment1.login.forgetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentAuthLoginBinding
import com.example.englishapp.databinding.FragmentForgetPasswordFromAuthBinding
import com.example.englishapp.view.main.base.BaseFragment


class ForgetPasswordFromAuth : BaseFragment() {

    private var _binding: FragmentForgetPasswordFromAuthBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgetPasswordFromAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        addSwipeLeftToRightListener(binding.root){
            findNavController().popBackStack()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}