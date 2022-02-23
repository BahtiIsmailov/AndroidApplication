package com.example.englishapp.view.main.words.wordsinwordset.training.base

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver

class WordRecognizer(private val context: Context) :
    DefaultLifecycleObserver {
    private lateinit var speech: SpeechRecognizer


    private fun recognizeWord(recognitionListener: RecognitionListener) {
        speech = SpeechRecognizer.createSpeechRecognizer(
            context,
            ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService")
        )

        speech.setRecognitionListener(recognitionListener)
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US")
        recognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)

        speech.startListening(recognizerIntent)
    }

    private fun checkPermissionAndRecognizeWord(
        registry: ActivityResultRegistry,
        recognitionListener: RecognitionListener
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                recognizeWord(recognitionListener)

            }
            else -> {
                val requestPermissionLauncher =
                    registry.register(
                        "key",
                        ActivityResultContracts.RequestPermission()
                    ) { isGranted: Boolean ->
                        if (isGranted) {
                            recognizeWord(recognitionListener)
                        } else {

                        }
                    }
                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }
        }
    }


    fun recognizeWords(
        registry: ActivityResultRegistry,
        onReadyForSpeech: () -> Unit,
        onError: (Int) -> Unit,
        onResults: (String) -> Unit,
        onRmsChanged: (Float) -> Unit,
    ) {

        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { // не заходит

                onReadyForSpeech()
            }

            override fun onBeginningOfSpeech() { // не заходит

            }

            override fun onRmsChanged(rmsdB: Float) {
                onRmsChanged(rmsdB)
            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(error: Int) {
                onError(error)


            }

            override fun onResults(results: Bundle?) {
                onResults(
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                        ?: ""
                )
            }

            override fun onPartialResults(partialResults: Bundle?) {

            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        }
        checkPermissionAndRecognizeWord(registry, recognitionListener)
    }

    fun destroy() {
        speech.setRecognitionListener(null)


        speech.destroy()
    }
}