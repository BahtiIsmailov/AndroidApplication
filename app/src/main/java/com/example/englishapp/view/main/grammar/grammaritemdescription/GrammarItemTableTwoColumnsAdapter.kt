package com.example.englishapp.view.main.grammar.grammaritemdescription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.GrammarItemTableTwoColumnItemBinding
import com.example.englishapp.model.data.grammar.Content
import com.google.gson.internal.LinkedTreeMap

class GrammarItemTableTwoColumnsAdapter(val contents: List<LinkedTreeMap<String, List<Any>>>?) :
    RecyclerView.Adapter<GrammarItemTableTwoColumnsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrammarItemTableTwoColumnsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.grammar_item_table_two_column_item, parent, false)
        return GrammarItemTableTwoColumnsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrammarItemTableTwoColumnsViewHolder, position: Int) {
        contents?.let {
            holder.bind(it[position])
        }

    }


    override fun getItemCount(): Int {
        return contents?.size ?: 0
    }
}


class GrammarItemTableTwoColumnsViewHolder(
    itemView: View,
) :
    RecyclerView.ViewHolder(itemView) {

    val binding = GrammarItemTableTwoColumnItemBinding.bind(itemView)
    fun bind(items: LinkedTreeMap<String, List<Any>>) {

        val content = getContentFromList(listOf(items)).first()

        var linkedTreeMap = content.text?.getOrNull(0) as? LinkedTreeMap<String, String>
        linkedTreeMap?.let {
            val spanItem = linkedTreeMapToSpanItem(it)
            binding.text.text = getStyledTextFromSpanItem(spanItem, itemView.context)
        }

        linkedTreeMap = content.value?.getOrNull(0) as? LinkedTreeMap<String, String>
        linkedTreeMap?.let {
            val spanItem = linkedTreeMapToSpanItem(it)
            binding.translate.text = getStyledTextFromSpanItem(spanItem, itemView.context)
        }

    }

}
