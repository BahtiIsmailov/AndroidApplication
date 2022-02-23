package com.example.englishapp.view.main.grammar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.GrammarItemBinding
import com.example.englishapp.model.data.grammar.GrammarMainData

interface GrammarAdapterListener {
    fun onGrammarItemClick(grammarItem: GrammarMainData)
}

class GrammarMainPageAdapter(val listener: GrammarAdapterListener) :
    ListAdapter<GrammarMainData, GrammarViewHolder>(GrammarDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grammar_item, parent, false)
        val viewHolder = GrammarViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: GrammarViewHolder, position: Int) {
        val grammarItem = getItem(position)
        holder.bind(grammarItem, listener)
        with(holder) {
            binding.root.setOnClickListener {
                listener.onGrammarItemClick(grammarItem)
            }
        }
    }
}

class GrammarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = GrammarItemBinding.bind(itemView)

    fun bind(grammarData: GrammarMainData, listener: GrammarAdapterListener) {
        binding.topic.text = grammarData.header
        binding.team.text = grammarData.name
        binding.progressBarStar.learning_progress = grammarData.stars
        binding.customChips.elementsWord = grammarData.tags
    }

}


class GrammarDiffUtilCallback : DiffUtil.ItemCallback<GrammarMainData>() {
    override fun areItemsTheSame(oldItem: GrammarMainData, newItem: GrammarMainData): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GrammarMainData, newItem: GrammarMainData): Boolean {
        return oldItem == newItem
    }
}