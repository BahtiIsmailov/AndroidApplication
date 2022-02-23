package com.example.englishapp.view.main.grammar.grammaritemdescription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.GrammarItemHeaderBinding
import com.example.englishapp.databinding.GrammarItemTableOneColumnBinding
import com.example.englishapp.databinding.GrammarItemTableTwoColumnsBinding
import com.example.englishapp.databinding.GrammarItemTextBinding
import com.example.englishapp.model.data.grammar.ItemInfo
import com.google.gson.internal.LinkedTreeMap


class FRagmentGrammarItemInfoAdapter :
    ListAdapter<ItemInfo, GrammarItemInfoViewHolder>(GrammarItemInfoDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarItemInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        val viewHolder = GrammarItemInfoViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: GrammarItemInfoViewHolder, position: Int) {
        val grammarItem = getItem(position)
        holder.bind(grammarItem)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.type) {
            ItemInfo.Type.TEXT -> R.layout.grammar_item_text
            ItemInfo.Type.TABLE_ONE_COLUMN -> R.layout.grammar_item_table_one_column
            ItemInfo.Type.HEADER -> R.layout.grammar_item_header
            ItemInfo.Type.TABLE_TWO_COLUMNS -> R.layout.grammar_item_table_two_columns
            ItemInfo.Type.TEXT_EXAMPLE -> R.layout.grammar_item_text
        }
    }
}

class GrammarItemInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: ItemInfo) {
        when (item.type) {
            ItemInfo.Type.TEXT -> {
                val binding = GrammarItemTextBinding.bind(itemView)
                binding.text.text = getStyledTextFromList(item.contents, itemView.context)
            }

            ItemInfo.Type.TABLE_ONE_COLUMN -> {

                val contentList = getContentFromList(item.contents?: listOf())

                val binding = GrammarItemTableOneColumnBinding.bind(itemView)
                val adapter = GrammarItemTableOneColumnAdapter(contentList)
                val divider = DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                binding.recycle.addItemDecoration(divider)
                binding.recycle.adapter = adapter
            }
            ItemInfo.Type.HEADER -> {
                val binding = GrammarItemHeaderBinding.bind(itemView)
                binding.header.text = getStyledTextFromList(item.contents,itemView.context)
            }
            ItemInfo.Type.TABLE_TWO_COLUMNS -> {
                val binding = GrammarItemTableTwoColumnsBinding.bind(itemView)
                binding.header.text = item.header
                val adapter = GrammarItemTableTwoColumnsAdapter(item.contents as List<LinkedTreeMap<String, List<Any>>>)
                binding.recycle.adapter =  adapter
                val divider = DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
                binding.recycle.addItemDecoration(divider)
            }
            ItemInfo.Type.TEXT_EXAMPLE -> {
                val binding = GrammarItemTextBinding.bind(itemView)

                //binding.header.text = item.toString()
                binding.text.text = getStyledTextFromList(item.contents, itemView.context)
            }
        }
    }
}
class GrammarItemInfoDiffUtilCallback : DiffUtil.ItemCallback<ItemInfo>() {
    override fun areItemsTheSame(oldItem: ItemInfo, newItem: ItemInfo): Boolean {
        return oldItem.contents == newItem.contents
    }

    override fun areContentsTheSame(oldItem: ItemInfo, newItem: ItemInfo): Boolean {
        return oldItem == newItem
    }
}
