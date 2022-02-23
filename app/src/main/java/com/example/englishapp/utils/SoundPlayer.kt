package com.example.englishapp.utils

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import java.util.*


object SoundPlayer {

    private var application: Application? = null
    private val context: Context get() = application!!
    private var rawMediaPlayer: MediaPlayer? = null


    fun setApplication(application: Application) {
        this.application = application
        tts = TextToSpeech(context) {

            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {

            }

            tts?.setSpeechRate(0.5f)
        }
    }


    private val soundPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    private val wordPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    private var tts: TextToSpeech? = null

    fun play(@RawRes resId: Int) {
        rawMediaPlayer?.apply {
            reset()
        }
        rawMediaPlayer = MediaPlayer.create(context, resId).apply {
            start()
        }
    }

    fun playWord(wordUrl: String, word: String) {
        if (wordUrl.isNotEmpty()) {
            wordPlayer.apply {
                soundPlayer.apply {
                    reset()
                    setDataSource(wordUrl)
                    prepare()
                    start()
                }
            }
        } else {
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, bundleOf(), "")
        }
    }

    fun playWordAndStartAnimation(@RawRes resId: Int,animation: LottieAnimationView,textView: TextView){
        rawMediaPlayer?.apply {
            reset()
        }
        rawMediaPlayer = MediaPlayer.create(context, resId).apply {
            start()
            setOnCompletionListener {
                animation.cancelAnimation()
                animation.isGone = true
                textView.isVisible = true
            }
        }
        textView.isGone = true
        animation.isVisible = true
        animation.playAnimation()

    }

}

