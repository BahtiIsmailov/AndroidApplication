package com.example.englishapp.view

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.englishapp.R
import com.example.englishapp.databinding.ActivityMainBinding
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.utils.FeatureFlags
import com.example.englishapp.utils.doOnApplyWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RemoteDataSource.Listener {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (FeatureFlags.enableFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.decorView.windowInsetsController?.hide(
                    WindowInsets.Type.statusBars()
                )
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

            ViewCompat.getWindowInsetsController(view)?.hide(WindowInsetsCompat.Type.statusBars())
            view.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                windowInsetsCompat
            }
        }

        val navController = findNavController(R.id.fragmentContainer)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.navView.menu[0].setIcon(R.drawable.ic_passive_dialogues)
            binding.navView.menu[1].setIcon(R.drawable.ic_passive_word)
            binding.navView.menu[2].setIcon(R.drawable.ic_passive_grammer)
            binding.navView.menu[3].setIcon(R.drawable.ic_passive_profile)
            when (destination.id) {
                R.id.storyFragment -> {
                    binding.navView.menu[0].setIcon(R.drawable.ic_dialogue_selector)
                    binding.navView.itemIconTintList = null
                }
                R.id.wordsFragment -> {
                    binding.navView.menu[1].setIcon(R.drawable.ic_word_selector)
                    binding.navView.itemIconTintList = null
                }
                R.id.grammarFragment -> {
                    binding.navView.menu[2].setIcon(R.drawable.ic_grammar_selector)
                    binding.navView.itemIconTintList = null
                }
                R.id.profileFragment -> {
                    binding.navView.menu[3].setIcon(R.drawable.ic_profile_selector)
                    binding.navView.itemIconTintList = null
                }
            }
            true

            when (destination.id) {
                R.id.profileFragment, R.id.storyFragment, R.id.grammarFragment, R.id.wordsFragment -> {
                    binding.navView.visibility = View.VISIBLE
                }
                else -> {
                    binding.navView.visibility = View.GONE
                }
            }

            remoteDataSource.setOnAuthorizedErrorListener(this)
        }


    }

    override fun onAuthorizedError() {
        //TODO("Not yet implemented")
    }
}