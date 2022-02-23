package com.example.englishapp

import android.app.Application
import com.example.englishapp.utils.ParallaxEffect
import com.example.englishapp.utils.SoundGrammarPlayer
import com.example.englishapp.utils.SoundPlayer
import com.squareup.picasso.OkHttp3Downloader
import dagger.hilt.android.HiltAndroidApp
import com.squareup.picasso.Picasso


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        SoundPlayer.setApplication(this)
        SoundGrammarPlayer.application = this

        val picasso =
            Picasso.Builder(this).downloader(OkHttp3Downloader(cacheDir, 250000000)).build()
        Picasso.setSingletonInstance(picasso)

        super.onCreate()
    }
}