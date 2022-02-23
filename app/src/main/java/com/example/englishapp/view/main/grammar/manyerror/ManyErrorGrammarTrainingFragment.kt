package com.example.englishapp.view.main.grammar.manyerror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.GraphDirections
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentManyErrorGrammarTrainingBinding
import com.example.englishapp.utils.SoundGrammarPlayer
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.main.grammar.endtraining.EndGrammarTrainingFragment




class ManyErrorGrammarTrainingFragment : BaseFragment() {

    private var _binding: FragmentManyErrorGrammarTrainingBinding? = null
    private val binding get() = _binding!!
    private val args: ManyErrorGrammarTrainingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManyErrorGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SoundPlayer.play(R.raw.lms_moto_brake)
        SoundPlayer.play(R.raw.lms_moto_fail)

        with(binding){
            readRulls.setOnClickListener {
                val action = GraphDirections.actionGlobalFragmentGrammarItemDescription(args.grammarMainData)
                findNavController().navigate(action)
            }


            trainingAgain.setOnClickListener {
                findNavController().popBackStack(R.id.grammarTrainingFragment, true)
            }
            closeManyError.setOnClickListener {
                findNavController().popBackStack(R.id.grammarTrainingFragment, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}