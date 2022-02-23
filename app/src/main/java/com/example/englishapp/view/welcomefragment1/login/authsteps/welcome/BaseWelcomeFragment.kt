package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentBaseWelcomeBinding
import com.example.englishapp.utils.FeatureFlags
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.utils.setStatusBarPadding
import com.example.englishapp.view.main.base.BaseFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooselanguage.ChooseLanguageFragment

interface ElementOnFragmentClickLListener{
    fun onElementClick(value:String)
}
class BaseWelcomeFragment : BaseFragment(){

    private var _binding: FragmentBaseWelcomeBinding? = null
    private val binding get() = _binding!!

    val parentViewModel:BaseWelcomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (FeatureFlags.enableFullScreen) {
            binding.levelProgressBar.setStatusBarPadding()
        }

        binding.levelProgressBar.max = parentViewModel.containers.size

        val fragment = ChooseLanguageFragment()
        replaceContainer(fragment)

        binding.continueButton.setOnClickListener {
            parentViewModel.upCurrentIndexFragmentContainers()
        }

        binding.backFromAuth.setOnClickListener {
            if (parentViewModel.getCurrentIndexFragmentContainers() == 0)
                findNavController().popBackStack()
            else
                parentViewModel.downCurrentIndexFragmentContainers()
        }

        parentViewModel.currentIndexFragmentLiveData.observe {
            replaceContainer(parentViewModel.containers[it])
            binding.levelProgressBar.progress = it
        }

    }

    fun getContinueButton():View = binding.continueButton

    fun setTextForContinueButton(text:String) {
        binding.continueButton.text = text
    }

    fun getTextView():TextView = binding.textForEveryFragment

    fun getTextViewForSomethingFragment():TextView = binding.textForSomethingFragment

    fun realizeSpeechButtonContainerText(flag:Boolean = false) : View{
        binding.speechButton.isVisible = true
        if (flag) {
            binding.containerText.isGone = true
            binding.recognitionAnimation.isVisible = true
            binding.recognitionAnimation.playAnimation()
        }else{
            binding.containerText.isVisible = true
            binding.recognitionAnimation.cancelAnimation()
            binding.recognitionAnimation.isGone = true

        }
        return binding.speechButton
    }


    private fun replaceContainer(fragment: Fragment){
        binding.continueButton.isGone = true
        childFragmentManager
            .beginTransaction()
            .replace(R.id.centerViewContainer, fragment)
            .commitNow()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}