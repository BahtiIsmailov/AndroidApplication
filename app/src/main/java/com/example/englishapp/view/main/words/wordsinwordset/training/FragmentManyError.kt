package com.example.englishapp.view.main.words.wordsinwordset.training

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentManyErrorBinding

interface FragmentManyErrorsListener{
    fun onEndButtonClick()
}

class FragmentManyError :  DialogFragment() {
    private var _binding: FragmentManyErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManyErrorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun getTheme() = R.style.RoundedCornersDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            val listener = parentFragment as? FragmentManyErrorsListener
            endButton.setOnClickListener {
                listener?.onEndButtonClick()
                dismiss()
            }
            continueButton.setOnClickListener {
                dismiss()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun getInstance(): FragmentManyError {
            return FragmentManyError()
        }
    }


}