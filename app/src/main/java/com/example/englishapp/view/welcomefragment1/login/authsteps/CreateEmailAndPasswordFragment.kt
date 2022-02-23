package com.example.englishapp.view.welcomefragment1.login.authsteps

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.GraphDirections
import com.example.englishapp.databinding.FragmentCreateEmailAndPasswordBinding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.welcomefragment1.login.AuthLoginViewModel


class CreateEmailAndPasswordFragment : BaseFragment() {

    private val viewModel: CreateEmailAndPasswordViewModel by viewModels()
    private var _binding: FragmentCreateEmailAndPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEmailAndPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSwipeLeftToRightListener(binding.root) {
            findNavController().popBackStack()
        }
        setOnclickListener()

        setUrl("https://www.letmespeak.org/terms/", binding.terms)
        setUrl("https://www.letmespeak.org/privacy/", binding.privacyPolicy)
        binding.buttonRegistration.setOnClickListener {
            binding.accountNotFound.isGone = true
            binding.constraintForLoading.isVisible = true
            val login = binding.loginEditText.text.toString()
            val password = binding.password.text.toString()
            if (login.contains("@") && login.contains(".")) {
                viewModel.createAuthProfile(login, password)
            } else {
                binding.accountNotFound.isVisible = true
                binding.accountNotFound.text = "Проверьте валидность логина"
            }
        }

        viewModel.firstRequestGetTokenLiveData.observeUIState(
            onSuccess = { createAuthProfile ->
                val ctoken = createAuthProfile.ctoken
                viewModel.getProfilesAfterCreatedAuthPerson(ctoken)
            },
            onError = {
                Toast.makeText(requireContext(), "Server is not Access now", Toast.LENGTH_SHORT)
                    .show()
                val action = GraphDirections.actionGlobalWelcomeFragment12()
                findNavController().navigate(action)
            },
            onLoading = {
                binding.progressBar.isVisible = true
                binding.progressBar.playAnimation()

            }
        )

        viewModel.getProfilesAfterCreatedAuthPersonLiveData.observeUIState(
            onLoading = {

                binding.progressBar.isVisible = true
                binding.progressBar.playAnimation()
            },
            onSuccess = {
                val action = CreateEmailAndPasswordFragmentDirections.actionCreateEmailAndPasswordFragmentToChooseProfileFragment(
                    emptyArray()
                )
                findNavController().navigate(action)
            },
            onError = {
                Toast.makeText(requireContext(), "Server is not Access now", Toast.LENGTH_SHORT)
                    .show()
                val action = GraphDirections.actionGlobalWelcomeFragment12()
                findNavController().navigate(action)
            }
        )

    }


    private fun setOnclickListener() {
        with(binding) {
            backFromAuth.setOnClickListener {
                findNavController().popBackStack()
            }
            checkbox.setOnClickListener {
                if (checkbox.isChecked
                    && !TextUtils.isEmpty(loginEditText.text.toString())
                    && !TextUtils.isEmpty(password.text.toString())
                ) {
                    buttonRegistration.isVisible = true
                    buttonDisable.isGone = true
                } else {
                    buttonRegistration.isGone = true
                    buttonDisable.isVisible = true
                }
            }
            password.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    hideEyesForShowPassword.isVisible = true
            }

            hideEyesForShowPassword.setOnClickListener {
                it.isGone = true
                eyesForShowPassword.isVisible = true
                viewModel.showPassword(password)
            }

            eyesForShowPassword.setOnClickListener {
                it.isGone = true
                hideEyesForShowPassword.isVisible = true
                viewModel.showPassword(password)
            }
        }
    }

    private fun setUrl(url: String, view: View) {
        view.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}