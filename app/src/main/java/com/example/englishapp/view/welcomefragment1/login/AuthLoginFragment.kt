package com.example.englishapp.view.welcomefragment1.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.GraphDirections
import com.example.englishapp.databinding.FragmentAuthLoginBinding
import com.example.englishapp.view.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthLoginFragment : BaseFragment() {
    private val viewModel: AuthLoginViewModel by viewModels()

    private var _binding: FragmentAuthLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleDividerVisibility()
        with(binding) {
            backFromAuth.setOnClickListener {
               val action = GraphDirections.actionGlobalWelcomeFragment12()
                findNavController().navigate(action)
            }

            addSwipeLeftToRightListener(root){
                val action = GraphDirections.actionGlobalWelcomeFragment12()
                findNavController().navigate(action)
            }

            forgetPassword.setOnClickListener {
                val action = AuthLoginFragmentDirections.actionAuthLoginFragmentToForgetPasswordFromAuth()
                findNavController().navigate(action)
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

            viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    clGreyLoading.isVisible = true
                    progressBar.isVisible = true
                    progressBar.playAnimation()
                } else {
                    clGreyLoading.isGone = true
                    progressBar.isGone = true
                    progressBar.cancelAnimation()
                }
            }

            forgetPassword.setOnClickListener {
                val action =
                    AuthLoginFragmentDirections.actionAuthLoginFragmentToForgetPasswordFromAuth()
                findNavController().navigate(action)
            }

            buttonRegistration.setOnClickListener {
                val login = LoginEditText.text.toString()
                val pass = password.text.toString()
                AccountNotFound.isVisible = false
                viewModel.login(login, pass)
            }

            viewModel.profilesLiveData.observe(viewLifecycleOwner) {
                val action =
                    AuthLoginFragmentDirections.actionAuthLoginFragmentToChooseProfileFragment(it.toTypedArray())
                findNavController().navigate(action)
            }

            viewModel.errorProfileLiveData.observe(viewLifecycleOwner) {
                AccountNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun handleDividerVisibility() {
        fun setDividerVisibility() {
            val isVisible = binding.LoginEditText.isFocused and binding.password.isFocused
            binding.dividerLoginAndPassword.isInvisible = !isVisible
        }

        binding.LoginEditText.setOnFocusChangeListener { view, b -> setDividerVisibility() }
        binding.password.setOnFocusChangeListener { view, b -> setDividerVisibility() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



