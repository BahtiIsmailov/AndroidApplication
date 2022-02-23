package com.example.englishapp.view.main.grammar.endtraining.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.englishapp.model.data.grammartraining.endgrammartraining.TopPlayerDto
import com.example.englishapp.view.main.grammar.endtraining.FragmentStatisticPage

class StatisticViewPagerAdapter(
    private val items: List<List<TopPlayerDto>>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return items.map { FragmentStatisticPage.newInstance(it) }[position]
    }
}