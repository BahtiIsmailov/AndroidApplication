package com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems

import android.view.View
import com.example.englishapp.R
import com.example.englishapp.databinding.ItemTrainingBigSpaceBinding
import com.example.englishapp.databinding.ItemTrainingHeaderBinding
import com.example.englishapp.model.data.words.Word
import com.xwray.groupie.viewbinding.BindableItem

class BigSpaceItem : BindableItem<ItemTrainingBigSpaceBinding>() {
    override fun initializeViewBinding(view: View): ItemTrainingBigSpaceBinding {
        return ItemTrainingBigSpaceBinding.bind(view)
    }

    override fun bind(binding: ItemTrainingBigSpaceBinding, p1: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.item_training_big_space
    }
}