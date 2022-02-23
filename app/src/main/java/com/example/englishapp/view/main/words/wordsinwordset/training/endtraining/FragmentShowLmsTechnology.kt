package com.example.englishapp.view.main.words.wordsinwordset.training.endtraining

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentManyErrorBinding
import com.example.englishapp.databinding.FragmentShowLmsTechnologyBinding
import com.example.englishapp.view.main.words.wordsinwordset.training.FragmentManyError
import com.example.englishapp.view.main.words.wordsinwordset.training.FragmentManyErrorsListener

class FragmentShowLmsTechnology :  DialogFragment() {
    private var _binding: FragmentShowLmsTechnologyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowLmsTechnologyBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun getTheme() = R.style.RoundedCornersDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            //val listener = parentFragment as? FragmentManyErrorsListener
            understand.setOnClickListener {
                dismiss()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun getInstance(): FragmentShowLmsTechnology {
            return FragmentShowLmsTechnology()
        }
    }


}