package com.example.englishapp.view.main.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal.ROUND_CEILING
import java.text.DecimalFormat

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initViewModel()


        viewModel.timeToHighEnergy.observe(viewLifecycleOwner) {
            binding.restoreEnergy.isVisible = it != null
            binding.arrowUp.isVisible = it != null
            it?.let { time ->
                val minutes = (time % 3600) / 60
                val seconds = time % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)
                binding.restoreEnergy.text = timeString
            }
        }

        viewModel.profileLiveData.observe(viewLifecycleOwner) { profile ->

            Picasso.get().load(profile.avatarUrl).into(binding.fotoProfile)
            binding.profileName.text = profile.name
            binding.reshetkaProfile.text = "#${profile.number}"
            binding.levelProgressBar.max = profile.xp.next - profile.xp.start
            binding.levelProgressBar.progress = profile.xp.current - profile.xp.start
            binding.showLevel.text = profile.xp.current.toString()
            binding.textView4.text = "/${profile.xp.next} XP"
            binding.level.text = profile.level.toString()
            binding.nameProfile.text = profile.rarity_title
            kotlin.runCatching { ColorStateList.valueOf(Color.parseColor(profile.rarity_badge_color)) }
                .getOrNull()?.let {
                    binding.nameProfile.backgroundTintList = it
                }
            binding.totalLevelNumber.text = profile.total_stars.roundTotalStars()
            binding.numberLevelProfileGreen.text = profile.level.toString()
            binding.numberMolnia.text = profile.energy.value.toString()
            binding.showVocabulary.text = profile.skills.vocabulary.toString()
            binding.showListening.text = profile.skills.listening.toString()
            binding.showPronuncation.text = profile.skills.pronouncing.toString()
            binding.showGrammar.text = profile.skills.grammar.toString()
            binding.numberTale.text = profile.talent.toString()
            binding.numberLearning.text = profile.learning_speed.toString()
            binding.numberRewards.text = "${(profile.currency_rewards * 100).toInt()}"
            binding.showVisaDays.text = profile.visa.days_used.toString()
            binding.visaDaysProgressbar.progress = profile.visa.days_used

        }
        binding.changeProfileButton.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToChooseProfileFragment(null)
            findNavController().navigate(action)
        }

        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { onLoading ->
            if (onLoading) {
                binding.linearLayout.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.linearLayout.visibility = View.VISIBLE
            }

        }


    }

    private fun Double.roundTotalStars(): String {
        val value = this.toBigDecimal()
        val df = DecimalFormat("###.00")
        return value.apply {
            setScale(2, ROUND_CEILING)
            df.format(this)
        }.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}