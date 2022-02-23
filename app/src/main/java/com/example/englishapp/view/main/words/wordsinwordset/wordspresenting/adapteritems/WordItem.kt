package com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.example.englishapp.R
import com.example.englishapp.databinding.WordInWordSetItemBinding
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.utils.SoundPlayer
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.TextForm
import com.xwray.groupie.viewbinding.BindableItem

class WordItem(private val item: Word) : BindableItem<WordInWordSetItemBinding>() {

    override fun initializeViewBinding(view: View): WordInWordSetItemBinding {
        return WordInWordSetItemBinding.bind(view)
    }

    override fun bind(binding: WordInWordSetItemBinding, position: Int) {
        binding.translationWordTv.text = item.translate
        binding.originalWordTV.text = item.word

        binding.audio.setOnClickListener {
            val audioUrl = item.audio

            SoundPlayer.playWord(audioUrl, item.translate)
        }

        var tooltipText: String? = null

        if (item.repeated_count == 0 && item.repeatTimeoutHour == 0L) {
            binding.seekArc1.setEmptyState()
            binding.goTV.isVisible = false
            binding.ivDone.isVisible = false

            tooltipText = "Новое слово"
        }

        if (item.repeated_count == 1) {
            binding.goTV.isVisible = item.repeatTimeoutHour == 0L
            binding.ivDone.isVisible = false
            binding.seekArc1.setRepeatAfter8Hour(item.repeatTimeoutHour)

            binding.tvLeftHours.isVisible = item.repeatTimeoutHour != 0L
            binding.tvLeftHours.text = "${item.repeatTimeoutHour}ч"

            tooltipText = if (item.repeatTimeoutHour == 0L) {
                "Нужно\nповторить"
            } else {
                "Повторить\n" +
                        "через ${item.repeatTimeoutHour} часов"
            }

        }

        if (item.repeated_count == 2) {
            binding.goTV.isVisible = item.repeatTimeoutHour == 0L
            binding.ivDone.isVisible = false
            binding.seekArc1.setRepeatAfter24Hour(item.repeatTimeoutHour)

            binding.tvLeftHours.isVisible = item.repeatTimeoutHour != 0L
            binding.tvLeftHours.text = "${item.repeatTimeoutHour}ч"

            tooltipText = if (item.repeatTimeoutHour == 0L) {
                "Нужно\nповторить"
            } else {
                "Повторить\n" +
                        "через ${item.repeatTimeoutHour} часов"
            }
        }

        if (item.repeated_count == 3) {
            binding.seekArc1.learned()
            binding.ivDone.isVisible = true
            binding.tvLeftHours.isVisible = false
            binding.goTV.isVisible = false

            tooltipText = "Слово выучено"
        }

        binding.seekArc1.setOnClickListener {

            tooltipText?.let {

                val textForm = TextForm.Builder(binding.root.context)
                    .setText(tooltipText)
                    .setTextColorResource(R.color.black)
                    .setTextSize(15f)
                    .setTextTypeface(
                        ResourcesCompat.getFont(
                            binding.root.context,
                            R.font.proxima_soft_regular
                        )
                    )
                    .build()

                val balloon = Balloon.Builder(binding.root.context)
                    .setWidth(BalloonSizeSpec.WRAP)
                    .setTextForm(textForm)
                    .setMarginLeft(24)
                    .setMarginRight(-4)
                    .setPaddingBottom(16)
                    .setBackgroundDrawableResource(R.drawable.tooltip_background)
                    .setFocusable(false)
                    .setIsVisibleArrow(false)
                    .build()

                balloon.showAlignTop(binding.tooltipAnchor)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.word_in_word_set_item
    }

}