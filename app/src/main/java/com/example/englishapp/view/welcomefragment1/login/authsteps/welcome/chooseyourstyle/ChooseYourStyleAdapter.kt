package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooseyourstyle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.ItemChooseYourStyleBinding
import com.example.englishapp.model.data.createauthprofile.Avatar
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.ElementOnFragmentClickLListener

class ChooseYourStyleAdapter(private val elementClickLListener: ElementOnFragmentClickLListener) :
RecyclerView.Adapter<ChooseYourStyleAdapter.ChooseYourStyleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseYourStyleHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_choose_your_style, parent, false)
        return ChooseYourStyleHolder(view)
    }

    override fun onBindViewHolder(holder: ChooseYourStyleHolder, position: Int) {
        val elem = InformationForWelcome.getAvatar()[position]
        holder.bind(elem)

    }

    override fun getItemCount(): Int = InformationForWelcome.getAvatar().size


    inner class ChooseYourStyleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemChooseYourStyleBinding.bind(itemView)
        fun bind(avatar: Avatar) {
            binding.imageAvatar.setImageResource(avatar.images)
            binding.root.setOnClickListener {
                elementClickLListener.onElementClick(avatar.nameAvatar)
                Log.d("avatar",avatar.nameAvatar)
             }

        }
    }
}