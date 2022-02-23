package com.example.englishapp.view.main.grammar.endtraining

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.englishapp.R
import com.example.englishapp.databinding.FragmentGetStatisticFromOtherPersonBinding
import com.example.englishapp.view.main.base.BaseBottomSheetDialogFragment
import com.example.englishapp.view.main.grammar.endtraining.adapter.StatisticViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.bottomsheet.BottomSheetBehavior

import androidx.recyclerview.widget.LinearLayoutManager

import android.app.Dialog
import android.content.DialogInterface
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieDrawable
import com.example.englishapp.view.main.base.BaseFragment

import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout

import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.NonNull
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback


@AndroidEntryPoint
class GetStatisticFromOtherPerson : BaseBottomSheetDialogFragment() {

    private val viewModel: EndTrainingViewModel by viewModels()
    private var _binding: FragmentGetStatisticFromOtherPersonBinding? = null
    private val binding get() = _binding!!
    val args: GetStatisticFromOtherPersonArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetStatisticFromOtherPersonBinding.inflate(inflater, container, false)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog?
            val bottomSheet =
                dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)

            // gets called after layout has been done but before display
            // so we can get the height then hide the view
            val height: Int = binding.root.height
            Log.d("Rating dialog", "Height: $height")
            mBottomSheetBehavior.peekHeight = height
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.getStatisticFromOtherPerson(args.grammarMainData.code.toIntOrNull() ?: 0)
        viewModel.getStatisticFromOtherPerson(1)

        initTabs()

        binding.viewPager.isUserInputEnabled = false
        val recyclerView = binding.viewPager.getRecyclerView()
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.overScrollMode = View.OVER_SCROLL_NEVER // Optional

        //viewModel.getStatisticFromOtherPerson(5)
        viewModel.getStatisticFromOtherPersonLiveData.observeUIState(
            onSuccess = {
                Log.d("getStatisticFromOtherPersonLiveData", it.toString())

                binding.viewPager.adapter = StatisticViewPagerAdapter(listOf(it.weekly, it.all), requireActivity())
                binding.viewPager.requestTransform()
                binding.progressBar.pauseAnimation()
                binding.progressBar.isVisible = false
            },
            onError = {
                binding.progressBar.pauseAnimation()
                binding.progressBar.isVisible = false
            },
            onLoading = {
                binding.progressBar.repeatMode = LottieDrawable.REVERSE
                binding.progressBar.playAnimation()
            }
        )
    }

    fun ViewPager2.getRecyclerView(): RecyclerView? {
        try {
            val field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            field.isAccessible = true
            return field.get(this) as RecyclerView
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    private fun initTabs() {
        binding.checkedTextView.setOnClickListener {
            binding.checkedTextView.isChecked = true
            binding.checkedTextView2.isChecked = false
            binding.viewPager.currentItem = 0
        }
        binding.checkedTextView2.setOnClickListener {
            binding.checkedTextView2.isChecked = true
            binding.checkedTextView.isChecked = false
            binding.viewPager.currentItem = 1
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().popBackStack()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}