package com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems

import android.view.View
import com.example.englishapp.R
import com.example.englishapp.databinding.ItemTrainingHeaderBinding
import com.example.englishapp.model.data.words.Word
import com.xwray.groupie.viewbinding.BindableItem

class HeaderItem(private val level: Int) : BindableItem<ItemTrainingHeaderBinding>() {
    override fun initializeViewBinding(view: View): ItemTrainingHeaderBinding {
        return ItemTrainingHeaderBinding.bind(view)
    }

    override fun bind(binding: ItemTrainingHeaderBinding, p1: Int) {
        binding.tvLevel.setText(
            when (level) {
                1 -> R.string.level_1
                2 -> R.string.level_2
                3 -> R.string.level_3
                else -> R.string.level_1
            }
        )
    }

    override fun getLayout(): Int {
        return R.layout.item_training_header
    }
}