package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooseyourstyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentChooseYourStyleBinding
import com.example.englishapp.model.data.createauthprofile.Avatar
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.BaseWelcomeFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener
import com.skydoves.balloon.balloon


class ChooseYourStyleFragment : Fragment(), ElementOnFragmentClickLListener {

    private var _binding: FragmentChooseYourStyleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseYourStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChooseYourStyleAdapter(this)
        binding.myRecyclerView.adapter = adapter
        with(parentFragment as BaseWelcomeFragment) {
            getTextView().text = "Выбери свой образ"
            getContinueButton().isVisible = true
            parentViewModel.setDataForMapWelcomeDataForServer("avatar", "base_max")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onElementClick(value: String) {
        for (avatar in InformationForWelcome.getAvatar()) {
            if (value == avatar.nameAvatar) {
                binding.bigImage.setImageResource(avatar.images)
            }
        }
        val parentFragment = (parentFragment as BaseWelcomeFragment)
        parentFragment.parentViewModel.setDataForMapWelcomeDataForServer("avatar", value)
    }
}