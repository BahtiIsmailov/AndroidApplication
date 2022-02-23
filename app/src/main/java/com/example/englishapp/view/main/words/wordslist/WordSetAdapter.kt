package com.example.englishapp.view.main.words.wordslist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.LookOtherWordSetsBinding
import com.example.englishapp.databinding.WordSetItemBinding
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.Wordset
import com.example.englishapp.utils.Utils
import com.example.englishapp.utils.toPx
import com.squareup.picasso.Picasso

interface WordSetAdapterListener {
    fun onClickWordSet(wordset: Wordset)
    fun onClickOther(wordGroup: WordGroup)
}

class WordSetAdapter(
    private val wordSetAdapterListener: WordSetAdapterListener,
    private val showAll: Boolean = false,
    private val gridLayout: Boolean = false
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var wordGroup: WordGroup? = null

    private val wordSets: List<Wordset> get() = wordGroup?.wordsets ?: emptyList()

    fun setWordGroup(wordGroup: WordGroup) {
        this.wordGroup = wordGroup
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return if (viewType == R.layout.word_set_item)
            WordSetViewHolder(view, wordSetAdapterListener)
        else
            LookOtherViewHolder(view, wordSetAdapterListener, wordGroup)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val wordSet = wordSets[position]
        when (holder) {
            is WordSetViewHolder -> holder.bind(wordSet, position, gridLayout, wordSets.size - 1)
            is LookOtherViewHolder -> holder.bind(wordSets.size - ITEM_COUNT, position)
        }
    }

    override fun getItemCount(): Int {
        return when {
            showAll -> wordSets.size
            wordSets.size > ITEM_COUNT -> ITEM_COUNT + 1
            else -> wordSets.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            showAll -> R.layout.word_set_item
            position <= ITEM_COUNT - 1 -> R.layout.word_set_item
            else -> R.layout.look_other_word_sets
        }
    }

    companion object {
        const val ITEM_COUNT = 5
    }
}


class WordSetViewHolder(
    itemView: View,
    private val wordSetAdapterListener: WordSetAdapterListener
) :
    RecyclerView.ViewHolder(itemView) {
    val binding = WordSetItemBinding.bind(itemView)

    @SuppressLint("ClickableViewAccessibility")
    fun bind(wordSet: Wordset, position: Int, gridLayout: Boolean, lastItemPosition: Int) {
        with(binding) {
            root.setOnClickListener {
                wordSetAdapterListener.onClickWordSet(wordSet)
            }
            tvWordSetTitle.text = wordSet.title
            tvWordSetCount.text =
                binding.root.context.getString(R.string.words_count, wordSet.words_count.toString())
            ratingBar.rating = wordSet.stars.toFloat()
            ratingBar.setOnTouchListener { view, motionEvent -> true }
            progressBar.progress = wordSet.percent_completed

            val textColorRes = if (wordSet.percent_completed == 100) {
                R.color.green
            } else {
                R.color.velvet

            }
            tvWordSetCount.setTextColor(textColorRes)

            val color = Utils.getColorFromUrl(wordSet.link_pic)
            color?.let {
                ivPictureBackground.imageTintList = ColorStateList.valueOf(color)
            }

            Picasso.get().load(wordSet.link_pic).into(ivPicture)
        }

        if (gridLayout) {
            binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
                bottomMargin = 0.toPx().toInt()
                width = MATCH_PARENT
            }

        } else {
            when (position) {
                0 -> {
                    binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
                        marginStart = 16.toPx().toInt()
                        marginEnd = 0.toPx().toInt()
                    }
                }
                lastItemPosition -> {
                    binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
                        marginStart = 8.toPx().toInt()
                        marginEnd = 16.toPx().toInt()
                    }
                }
                else -> {
                    binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
                        marginStart = 8.toPx().toInt()
                    }
                }
            }
        }
    }
}

class LookOtherViewHolder(
    itemView: View,
    private val wordSetAdapterListener: WordSetAdapterListener,
    private val wordGroup: WordGroup?
) :
    RecyclerView.ViewHolder(itemView) {

    val binding = LookOtherWordSetsBinding.bind(itemView)
    fun bind(otherCount: Int, position: Int) {
        binding.root.setOnClickListener {
            wordGroup?.let {
                wordSetAdapterListener.onClickOther(it)
            }
        }
        binding.tvTitle.text =
            binding.root.context.resources.getQuantityString(
                R.plurals.word_set_left_count,
                otherCount,
                otherCount.toString()
            )
    }
}