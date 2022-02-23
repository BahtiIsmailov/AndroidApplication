package com.example.englishapp.view.main.grammar.grammaritemdescription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.GrammarItemTableOneColumnBinding
import com.example.englishapp.databinding.GrammarItemTableOneColumnItemBinding
import com.example.englishapp.databinding.GrammarItemTableTwoColumnItemBinding
import com.example.englishapp.model.data.grammar.Content
import com.example.englishapp.model.data.grammar.SpanItem
import com.google.gson.internal.LinkedTreeMap

class GrammarItemTableOneColumnAdapter (val contents: List<Content>) :
    RecyclerView.Adapter<GrammarItemTableOneColumnsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrammarItemTableOneColumnsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grammar_item_table_one_column_item, parent, false)
        return GrammarItemTableOneColumnsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrammarItemTableOneColumnsViewHolder, position: Int) {
            holder.bind(contents[position])


    }


    override fun getItemCount(): Int {
        return contents.size
    }
}


class GrammarItemTableOneColumnsViewHolder(
    itemView: View,
) :
    RecyclerView.ViewHolder(itemView) {

    val binding = GrammarItemTableOneColumnItemBinding.bind(itemView)
    fun bind(content: Content) {
        binding.text.text = getStyledTextFromContent(content,itemView.context,true)
        binding.translate.text = getStyledTextFromContent(content,itemView.context,false)
    }

}
