package com.example.englishapp.utils

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.example.englishapp.R

object SoundGrammarPlayer {

    var application: Application? = null
    private val context: Context get() = application!!

    private val listAudioClickButton = mutableListOf(
        R.raw.lms_moto_click_01, R.raw.lms_moto_click_02, R.raw.lms_moto_click_03,
        R.raw.lms_moto_click_04
    )

    private var musicForBeginTraining:MediaPlayer? = null
    private var backgroundMediaPlayer: MediaPlayer? = null
    private var pushButtonMusic: MediaPlayer? = null
    private var musicAnswerInGrammar: MediaPlayer? = null
    private var firstMusicOnTheStart: MediaPlayer? = null
    private var secondMusicOnTheStart:MediaPlayer? = null


    var isBackgroundMusicPaused = false
    var isSongPaused = false
    var isPlayCorrectOrNotCorrectAnswer = true
    var listenerForPlayMusicAfterRestart = false



    fun setInitMediaPlayerWithCountDownTimer(@RawRes rawRes1: Int, @RawRes rawRes2: Int ){
        firstMusicOnTheStart?.reset()
        secondMusicOnTheStart?.reset()
        firstMusicOnTheStart = MediaPlayer.create(context,rawRes1)
        secondMusicOnTheStart = MediaPlayer.create(context,rawRes2)
    }

    fun playMediaPlayerWithCountDownTimer(@RawRes rawRes1: Int, @RawRes rawRes2: Int ){
        firstMusicOnTheStart?.setOnPreparedListener {
            it.start()
        }
        secondMusicOnTheStart?.setOnPreparedListener {
            it.start()
        }
    }


    private fun setBackgroundMusic() {
        resetBackgroundMusic()
        backgroundMediaPlayer = MediaPlayer.create(context, R.raw.lms_moto_music_v01)
    }

    fun pauseBackgroundMusic() {
        this.isBackgroundMusicPaused = true
        backgroundMediaPlayer?.pause()
    }
    fun startBackgroundMusic() {
        setBackgroundMusic()
        this.isBackgroundMusicPaused = false
        backgroundMediaPlayer?.setOnPreparedListener {
            it.start()
        }
    }

    fun resetBackgroundMusic() {
        backgroundMediaPlayer?.reset()

    }


    private fun setMusicForBeginTraining(){
        musicForBeginTraining?.reset()
        musicForBeginTraining = MediaPlayer.create(context,R.raw.lms_moto_start)
    }

    fun playMusicBeginTraining(){
        setMusicForBeginTraining()
        musicForBeginTraining?.setOnPreparedListener {
            it.start()
        }
    }
    fun playPushButtonAndOtherMusic() {
        cancelPushButtonMusic()
        pushButtonMusic = MediaPlayer.create(context, getRandomAudio()).apply {
            this.start()
        }
    }
    private fun cancelPushButtonMusic() {
        pushButtonMusic?.reset()
    }




    fun playAnswerMusic(@RawRes rawRes: Int) {
        resetAnswerMusic()
        if (isPlayCorrectOrNotCorrectAnswer) {
            musicAnswerInGrammar = MediaPlayer.create(context, rawRes).apply {
                start()
            }
        }
    }
    private fun resetAnswerMusic() {
        musicAnswerInGrammar?.reset()
    }




    fun clickOnCancelSongOnOtherMusic() {
        isPlayCorrectOrNotCorrectAnswer = false
        this.isSongPaused = true
        resetAnswerMusic()
        cancelPushButtonMusic()
    }


    fun clickOnPlaySongOtherMusic() {
        isPlayCorrectOrNotCorrectAnswer = true
        this.isSongPaused = false
        playPushButtonAndOtherMusic()
    }


    private fun getRandomAudio(): Int {
        return when ((0..4).random()) {
            0 -> listAudioClickButton[0]
            1 -> listAudioClickButton[1]
            2 -> listAudioClickButton[2]
            else -> listAudioClickButton[3]
        }
    }
}