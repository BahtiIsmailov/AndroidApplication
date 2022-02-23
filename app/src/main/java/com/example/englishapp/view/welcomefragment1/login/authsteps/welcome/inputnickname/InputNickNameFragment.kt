package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.inputnickname

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.englishapp.databinding.FragmentInputNickNameBinding
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputNickNameFragment : Fragment() {
    private var _binding: FragmentInputNickNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel:InputNickNameViewModel by viewModels()

    lateinit var imageView: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputNickNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(parentFragment as BaseWelcomeFragment) {
            getTextView().text = "Как тебя зовут?"
            getTextViewForSomethingFragment().isVisible = true
            getTextViewForSomethingFragment().text =
                "Введите своё имя. Это нужно, чтобы тебя видели в списке рейтингов."
            imageView = parentViewModel.getValueFromMapWelcomeDataForServer("avatar")
            parentViewModel.getImageFromChooseYourStyle(imageView,binding.imageForInputName)
        }



        binding.createNickName.requestFocus()

        showSoftKeyboard(binding.createNickName)

        binding.createNickName.doOnTextChanged { text, start, before, count ->
            if (count == 0){
                binding.buttonDisable.isVisible = true
                 binding.continueButton.isGone = true
            }else{
                binding.buttonDisable.isGone = true
                binding.continueButton.isVisible = true
            }

        }
        val parent = (parentFragment as BaseWelcomeFragment)
        binding.continueButton.setOnClickListener {
            viewModel.updateProfileData(
                binding.createNickName.text.toString(),
                parent.parentViewModel.getValueFromMapWelcomeDataForServer("language"),
                parent.parentViewModel.getValueFromMapWelcomeDataForServer("avatar")
            )
            parent.parentViewModel.setDataForMapWelcomeDataForServer("nickname",binding.createNickName.text.toString())
            parent.parentViewModel.upCurrentIndexFragmentContainers()
        }

    }



    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val inputMethodManager: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}