package com.example.englishapp.view.main.grammar.training.countdowntimer

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentCountDounBinding
import com.example.englishapp.databinding.FragmentGrammarTrainingBinding
import com.example.englishapp.databinding.FragmentRebusGrammarTrainingBinding
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.view.main.grammar.training.interfaces.CountDownListener
import com.example.englishapp.view.main.grammar.training.rebus.RebusGrammarTrainingViewModel

class CountDownFragment : Fragment() {

    private var _binding: FragmentCountDounBinding? = null
    private val binding get() = _binding!!

    private var countDownListener: CountDownListener? = null

    private val countDown = object : CountDownTimer(3500L, 10) {
        override fun onTick(p0: Long) {
            val percentStart = (p0 * 100L) / 3000L
            val count: String = when {
                percentStart >= 68 -> {
                    "3"
                }
                percentStart in 34..67 -> {
                    "2"
                }
                percentStart in 5 .. 33 -> {
                    "1"
                }
                else -> {
                    "Go"
                }

            }


            binding.number.text = count
            binding.progressBarCountDown.progress = percentStart.toInt()

        }

        override fun onFinish() {
            binding.progressBarCountDown.progress = 0
            binding.root.isVisible = false
            countDownListener?.onCountDownEnd()
        }
    }

    override fun onAttach(context: Context) {
        countDownListener = (parentFragment as? CountDownListener)
        super.onAttach(context)
    }

    override fun onDetach() {
        countDownListener = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountDounBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countDown.start()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDown.cancel()
        _binding = null
    }
}