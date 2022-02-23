package com.example.englishapp.view.welcomefragment1.login.chooseprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishapp.GraphDirections
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentChooseProfileBinding
import com.example.englishapp.model.data.ProfileResponse
import com.example.englishapp.utils.EventObserver
import com.example.englishapp.utils.FeatureFlags
import com.example.englishapp.utils.setStatusBarPadding
import com.example.englishapp.utils.showToast
import com.example.englishapp.view.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChooseProfileFragment : BaseFragment(), ProfilesAdapterListener {
    private val viewModel: ChooseProfileViewModel by viewModels()

    private var _binding: FragmentChooseProfileBinding? = null
    private val binding get() = _binding!!

    private val args: ChooseProfileFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (FeatureFlags.enableFullScreen) {
            binding.choose.setStatusBarPadding()
        }

        setUrl("https://market.letmespeak.org/", binding.whenHaventProfiles.goToMarketPlayse)
        setUrl("https://market.letmespeak.org/#/profile", binding.controlPersonItemTv)


        addSwipeLeftToRightListener(binding.root) {
            val action = GraphDirections.actionGlobalAuthLoginFragment()
            findNavController().navigate(action)
        }

        binding.backArrow.setOnClickListener {
            val action = GraphDirections.actionGlobalAuthLoginFragment()
            findNavController().navigate(action)
        }

        viewModel.initViewModel(args.profiles?.toList())
        viewModel.getProfileLiveData.observe(viewLifecycleOwner) { profiles ->
            if (profiles.isNotEmpty()) {
                showDifferentViewsForUser(profiles)
                val adapter = ProfilesAdapter(this)
                adapter.submitList(profiles)
                binding.recycleViewProfile.adapter = adapter
                binding.recycleViewProfile.visibility = View.VISIBLE

            } else {
                binding.whenHaventProfiles.root.isVisible = true
            }
        }

        binding.whenHaventProfiles.createFree.setOnClickListener {
            val action = ChooseProfileFragmentDirections.actionChooseProfileFragmentToBaseWelcomeFragment()
            findNavController().navigate(action)
        }

        viewModel.isLoadingLiveData.observe(viewLifecycleOwner)
        {
            if (it) {
                binding.progresBar.isVisible = true
                binding.progresBar.playAnimation()
            } else {
                binding.progresBar.isGone = true
                binding.progresBar.cancelAnimation()
            }
        }
        viewModel.detailsReadyEvent.observe(viewLifecycleOwner)
        {
            val action = GraphDirections.actionGlobalWordsFragment()
            findNavController().navigate(action)
        }
        viewModel.errorEvent.observe(viewLifecycleOwner, EventObserver
        {
            showToast(R.string.server_error)
            findNavController().popBackStack()
        })
    }

    override fun onProfileClick(profile: ProfileResponse) {
        lifecycleScope.launch {
            viewModel.onChooseProfile(profile)
        }
    }

    private fun showDifferentViewsForUser(profile: List<ProfileResponse>) {
        for (i in profile) {
            if (profile.size == 1 && i.rarity <= 1) {
                binding.whenHaventProfiles.goToMarketPlayse.isVisible = true
            }
            if (i.rarity > 1) {
                binding.controlPersonItem.isVisible = true
                binding.addFreeProfiles.root.isVisible = true
            } else {
                binding.addFreeProfiles.root.isGone = true
            }
        }
    }

    private fun setUrl(url: String, view: View) {
        view.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}