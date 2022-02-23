package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooselanguage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.LanguageItemBinding
import com.example.englishapp.model.data.createauthprofile.ChooseLanguage
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener
import com.squareup.picasso.Picasso


class ChooseLanguageAdapter(val elementClickLListener: ElementOnFragmentClickLListener) :
    ListAdapter<ChooseLanguage, ChooseLanguageAdapter.LanguageHolder>(LanguagesDiffUtilCallback()) {

      var globalPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageHolder, position: Int) {
        val elem = getItem(position)
        holder.bind(elem)
        with(holder) {
            if (position == globalPosition) {
                binding.choosedLanguage.isVisible = true
                binding.nameCountry.setTextColor(
                    itemView.resources.getColor(
                        R.color.green,
                        itemView.context.theme
                    )
                )

            } else {
                binding.choosedLanguage.isGone = true
                binding.nameCountry.setTextColor(
                    itemView.resources.getColor(
                        R.color.black,
                        itemView.context.theme
                    )
                )
            }
        }
    }

    inner class LanguageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LanguageItemBinding.bind(itemView)
        fun bind(chooseLanguage: ChooseLanguage) {
            Picasso.get().load(chooseLanguage.flag).into(binding.imageFlag)
            binding.nameCountry.text = chooseLanguage.title
            binding.root.setOnClickListener {
                globalPosition = adapterPosition
                notifyDataSetChanged()
                elementClickLListener.onElementClick(chooseLanguage.code)

                Log.d("language",chooseLanguage.code)
            }
        }
    }
}
class LanguagesDiffUtilCallback : DiffUtil.ItemCallback<ChooseLanguage>() {
    override fun areItemsTheSame(oldItem: ChooseLanguage, newItem: ChooseLanguage): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: ChooseLanguage, newItem: ChooseLanguage): Boolean {
        return oldItem == newItem
    }
}