package com.example.englishapp.view.main.words.wordsinwordset.training

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory

class DrawableAlwaysCrossFadeFactory() : TransitionFactory<Drawable> {
    private val resourceTransition: DrawableCrossFadeTransition = DrawableCrossFadeTransition(300, true) //customize to your own needs or apply a builder pattern
    private val noTransition: DrawableCrossFadeTransition = DrawableCrossFadeTransition(0, false) //customize to your own needs or apply a builder pattern
    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> {
        //return if(dataSource == DataSource.REMOTE || dataSource == DataSource.DATA_DISK_CACHE) resourceTransition else noTransition
        return resourceTransition
    }
}