package com.example.englishapp.view.main.grammar.paused

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.GraphDirections
import com.example.englishapp.databinding.FragmentPausedGrammarTrainingBinding
import com.example.englishapp.utils.SoundGrammarPlayer
import com.example.englishapp.view.main.base.BaseFragment

class PausedGrammarTrainingFragment : BaseFragment() {

    private var _binding: FragmentPausedGrammarTrainingBinding? = null
    private val binding get() = _binding!!
    private val args: PausedGrammarTrainingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPausedGrammarTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isBackgroundMusicPaused()
        isSongPaused()
        setOnClickListener()

    }

    private fun isBackgroundMusicPaused() {
        with(binding) {
            if (SoundGrammarPlayer.isBackgroundMusicPaused) {
                cancelMusic.isVisible = true
                playMusic.isInvisible = true
            } else {
                cancelMusic.isInvisible = true
                playMusic.isVisible = true
            }
        }

    }

    private fun isSongPaused() {
        with(binding) {
            if (SoundGrammarPlayer.isSongPaused) {
                cancelSong.isVisible = true // grey state is Active
                playSong.isInvisible = true // white state is Cancel
            } else {
                cancelSong.isInvisible = true
                playSong.isVisible = true
            }
        }
    }

    fun setOnClickListener() {
        with(binding) {
            close.setOnClickListener {
                findNavController().popBackStack()

            }
            lookRules.setOnClickListener {
                val action =
                    GraphDirections.actionGlobalFragmentGrammarItemDescription(args.grammarMainData)
                findNavController().navigate(action)
            }



            repeatTraining.setOnClickListener {
//                val action =
//                    GraphDirections.actionGlobalFragmentBeginTraining(
//                        args.grammarMainData
//                    )
//                findNavController().navigate(action)
            }
            exit.setOnClickListener {
//                val action = GraphDirections.actionGlobalFragmentBeginTraining(
//                    args.grammarMainData
//                )
//                findNavController().navigate(action)
            }

            continueTraining.setOnClickListener {
                findNavController().popBackStack()
            }
            playMusic.setOnClickListener {
                playMusic.isInvisible = true
                cancelMusic.isVisible = true
                SoundGrammarPlayer.pauseBackgroundMusic()
            }
            cancelMusic.setOnClickListener {
                playMusic.isVisible = true
                cancelMusic.isInvisible = true
                SoundGrammarPlayer.startBackgroundMusic()
            }
            playSong.setOnClickListener {
                playSong.isInvisible = true// white state is Cancel
                cancelSong.isVisible = true // grey state is Active
                SoundGrammarPlayer.clickOnCancelSongOnOtherMusic()

            }
            cancelSong.setOnClickListener {
                playSong.isVisible = true
                cancelSong.isInvisible = true
                SoundGrammarPlayer.clickOnPlaySongOtherMusic()

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}