package com.example.englishapp.view.main.words.wordslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.LastWordsItemBinding
import com.example.englishapp.databinding.WordGroupItemBinding
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.Wordset
import com.squareup.picasso.Picasso

class WordsGroupAdapter(
    private val wordSetAdapterListener: WordSetAdapterListener
) :
    ListAdapter<WordGroup, RecyclerView.ViewHolder>(WordGroupDiffUtilCallback()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    var lastWordSet: Wordset? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return if (viewType == R.layout.last_words_item)
            LastWordViewHolder(view, wordSetAdapterListener)
        else
            WordGroupViewHolder(view, wordSetAdapterListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val wordGroup = getItem(position)
        when (holder) {
            is WordGroupViewHolder -> holder.bind(
                wordGroup, position, itemCount, viewPool
            )
            is LastWordViewHolder -> holder.bind(
                lastWordSet
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.last_words_item
            else -> R.layout.word_group_item
        }
    }
}


class WordGroupViewHolder(
    itemView: View,
    private val wordSetAdapterListener: WordSetAdapterListener
) :
    RecyclerView.ViewHolder(itemView) {
    val binding = WordGroupItemBinding.bind(itemView)
    private val wordSetsAdapter = WordSetAdapter(wordSetAdapterListener)

    val childLayoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    fun bind(
        wordGroup: WordGroup,
        position: Int,
        itemCount: Int,
        viewPool: RecyclerView.RecycledViewPool
    ) {
        binding.groupTitle.text = wordGroup.title

        childLayoutManager.initialPrefetchItemCount = 4

        binding.horizontalRecycleView.apply {
            layoutManager = childLayoutManager
            adapter = wordSetsAdapter
            setRecycledViewPool(viewPool)
        }

        wordSetsAdapter.setWordGroup(wordGroup)


        binding.lookAllTextView.setOnClickListener {
            wordSetAdapterListener.onClickOther(wordGroup)
        }
    }
}

class LastWordViewHolder(
    itemView: View,
    private val wordSetAdapterListener: WordSetAdapterListener,
) :
    RecyclerView.ViewHolder(itemView) {

    val binding = LastWordsItemBinding.bind(itemView)

    @SuppressLint("ClickableViewAccessibility")
    fun bind(
        wordSet: Wordset?,
    ) {

        with(binding) {
            wordSet?.let {
                lastWordCard.isVisible = true
                tvLastWordsTitle.text = it.title
                tvLastWordProgress.text = binding.root.context.getString(
                    R.string.last_words_count,
                    it.words_new_count.toString(),
                    it.words_count.toString()
                )
                tvLastWordsHeader.text = it.topic
                ratingBar.rating = it.stars.toFloat()
                ratingBar.setOnTouchListener { view, motionEvent -> true }
                levelProgressBar.progress = it.percent_completed
                Picasso.get().load(it.link_pic).into(ivPicture)

                btnContinue.setOnClickListener {
                    wordSetAdapterListener.onClickWordSet(wordSet)
                }

            } ?: { lastWordCard.isVisible = false }
        }

        binding.root.setOnClickListener {
            wordSet?.let {
                wordSetAdapterListener.onClickWordSet(it)
            }
        }
    }
}

class WordGroupDiffUtilCallback : DiffUtil.ItemCallback<WordGroup>() {
    override fun areItemsTheSame(oldItem: WordGroup, newItem: WordGroup): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WordGroup, newItem: WordGroup): Boolean {
        return oldItem == newItem
    }
}