package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.motivation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.MotivationItemBinding
import com.example.englishapp.model.data.createauthprofile.Motivation
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener

class MotivationFragmentAdapter(val elementClickLListener: ElementOnFragmentClickLListener) :
    RecyclerView.Adapter<MotivationFragmentAdapter.MotivationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotivationHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.motivation_item, parent, false)
        return MotivationHolder(view)
    }

    override fun onBindViewHolder(holder: MotivationHolder, position: Int) {
        val elem = InformationForWelcome.getMotivation()[position]
        holder.bind(elem)
        with(holder){
            binding.root.tag = true
            binding.root.setOnClickListener {
                if (binding.root.tag == true){
                    binding.root.tag = false
                    elementClickLListener.onElementClick(elem.topic)
                    binding.root.setBackgroundColor(itemView.context.getColor(R.color.greeen_10))
                    binding.root.strokeColor =
                        itemView.context.resources.getColor(R.color.green, itemView.context.theme)
                }else{
                    binding.root.tag = true
                    binding.root.setBackgroundColor(itemView.context.getColor(R.color.white))
                    binding.root.strokeColor =
                        itemView.context.resources.getColor(R.color.gray_600, itemView.context.theme)
                }

            }
        }

    }

    override fun getItemCount(): Int = InformationForWelcome.getMotivation().size


    inner class MotivationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MotivationItemBinding.bind(itemView)
        fun bind(chooseCountry: Motivation) {
            binding.motivationImageAuth.setImageResource(chooseCountry.images)
            binding.motivationTextTitle.text = chooseCountry.topic
            binding.textDescription.text = chooseCountry.description

        }
    }
}