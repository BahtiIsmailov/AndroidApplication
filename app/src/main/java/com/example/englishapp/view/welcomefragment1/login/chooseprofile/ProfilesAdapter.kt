package com.example.englishapp.view.welcomefragment1.login.chooseprofile

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishapp.R
import com.example.englishapp.databinding.ProfileItemBinding
import com.example.englishapp.model.data.ProfileResponse

import com.squareup.picasso.Picasso

interface ProfilesAdapterListener {
    fun onProfileClick(profile: ProfileResponse)
}

class ProfilesAdapter(private val listener: ProfilesAdapterListener) :
    ListAdapter<ProfileResponse, ProfileViewHolder>(ProfileDiffUtilCallback()) {
    var isClicked = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_item, parent, false)
        val viewHolder = ProfileViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile, listener)
        with(holder) {
            binding.root.setOnClickListener {
                if (!isClicked) {
                    isClicked = true
                    binding.constraint.setBackgroundColor(itemView.context.getColor(R.color.greeen_10))
                    binding.root.strokeColor =
                        itemView.context.resources.getColor(R.color.green)
                    binding.imageChooseProfile.setImageResource(R.drawable.ic_baseline_check_circle_24)
                    listener.onProfileClick(profile)
                }
            }
        }
    }
}

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ProfileItemBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bind(profile: ProfileResponse, listener: ProfilesAdapterListener) {
        with(binding) {
            visaDaysProgressbar.max = profile.visa_days_total
            visaDaysProgressbar.progress = profile.visa_days_left
            howManyDays.text = itemView.context.resources.getQuantityString(
                R.plurals.days_left,
                profile.visa_days_left,
                profile.visa_days_left.toString()
            )
            Reshetka.text = "#${profile.number ?: 12345678}"
            NickNameProfile.text = profile.nickname
            Uncommon.text = profile.rarityTitle ?: "Uncommon"
            kotlin.runCatching { ColorStateList.valueOf(Color.parseColor(profile.rarityBadgeColor)) }
                .getOrNull()?.let {
                Uncommon.backgroundTintList = it
            }
            numberMolnia.text = profile.energy.toString()
            numberLevelProfileGreen.text = profile.level.toString()
            Picasso.get().load(profile.avatar_url).into(imageProfile)
        }


    }
}

class ProfileDiffUtilCallback : DiffUtil.ItemCallback<ProfileResponse>() {
    override fun areItemsTheSame(oldItem: ProfileResponse, newItem: ProfileResponse): Boolean {
        return oldItem.nickname == newItem.nickname
    }

    override fun areContentsTheSame(oldItem: ProfileResponse, newItem: ProfileResponse): Boolean {
        return oldItem == newItem
    }
}
