package com.example.englishapp.view.main.words.wordsinwordset.training.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.englishapp.databinding.FragmentTrainingWordBinding
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.utils.loadImageWithStatueOfLibertyPlaceholder
import com.example.englishapp.utils.setStatueOfLibertyBackgroundColor
import com.example.englishapp.view.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class FragmentTrainingWordTypesBase : BaseFragment(useCustomInsets = true) {

    protected var _binding: FragmentTrainingWordBinding? = null
    val binding get() = _binding!!

    private var lastLoadedUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingWordBinding.inflate(inflater, container, false)

        binding.ivVoice.isVisible = false

        makeCommonItemsInvisible()
        return binding.root
    }

    fun makeCommonItemsInvisible() {
        binding.ivVoice.isVisible = false
        binding.tvBigWord.isVisible = false
        binding.tvWordOnImage.isVisible = false
        binding.ivBigSound.isVisible = false
        binding.imageView.isVisible = false
    }

    fun setImage(it: Word, withPlaceholderOnStart: Boolean = true) {
        binding.imageView.isVisible = true
        binding.imageViewPlaceholder.setStatueOfLibertyBackgroundColor()
        with(binding) {
            val url = it.pictures.firstOrNull()?.url ?: ""
            if(lastLoadedUrl != url) {
                imageView.loadImageWithStatueOfLibertyPlaceholder(
                    url = url,
                    withPlaceholder = withPlaceholderOnStart,
                ) {
                    imageView.isVisible = true
                    imageViewPlaceholder.isVisible = false
                }
            }
            lastLoadedUrl = url
        }

    }

    fun setTextOnImage(word: String) {
        binding.tvWordOnImage.isVisible = true
        binding.tvWordOnImage.text = word
    }

    fun setBigAudioIcon(word: Word, playOnStart: Boolean) {
        with(binding) {
            ivBigSound.isVisible = true

            ivBigSound.setOnClickListener {
                playSound(word.audio, word.translate)
            }


            if (playOnStart) {
                playSound(word.audio, word.translate)
            }
        }
    }

    fun setBigText(word: Word) {
        with(binding) {
            tvBigWord.isVisible = true
            tvBigWord.text = word.translate
        }

    }

    fun setAudio(word: Word) {
        binding.ivVoice.isVisible = true
        binding.ivVoice.setOnClickListener {
            playSound(word.audio, word.translate)
        }
    }

    private fun playSound(url: String, word: String) {
        SoundPlayer.playWord(url, word)
    }

    fun getBaseView(): TrainingView? {
        return parentFragment as? TrainingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val WORD = "word"
        const val VARIANTS = "variants"
        const val IMAGES = "images"
        const val LETTERS = "letters"
        const val ANIMATION_TYPE = "animation_type"
        const val SPEECH_WORD = "speech_word"
    }

}