package com.example.englishapp.view.main.grammar.endtraining.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.englishapp.R
import com.example.englishapp.databinding.GrammarItemBinding
import com.example.englishapp.databinding.StatisticTopPlayerItemBinding
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.model.data.grammartraining.endgrammartraining.TopPlayerDto
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


class TopPlayersAdapter :
    ListAdapter<TopPlayerDto, TopPlayerViewHolder>(TopPlayerDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopPlayerViewHolder {
        return TopPlayerViewHolder(
            StatisticTopPlayerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopPlayerViewHolder, position: Int) {
        val grammarItem = getItem(position)
        holder.bind(grammarItem)
    }
}

class TopPlayerViewHolder(val binding: StatisticTopPlayerItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TopPlayerDto) {
        binding.tvTitle.text = item.nickname
        binding.tvMessage.text = formatToTopPlayerDate(item.date)
        binding.tvScore.text = item.time.roundToInt().toString()

        when(item.place) {
            1 -> {
                binding.ivMedal.isInvisible = false
                binding.tvNumber.isInvisible = true
                binding.ivMedal.setImageResource(R.drawable.ic_rating_place_1)
            }
            2 -> {
                binding.ivMedal.isInvisible = false
                binding.tvNumber.isInvisible = true
                binding.ivMedal.setImageResource(R.drawable.ic_rating_place_2)
            }
            3 -> {
                binding.ivMedal.isInvisible = false
                binding.tvNumber.isInvisible = true
                binding.ivMedal.setImageResource(R.drawable.ic_rating_place_3)
            }
            else -> {
                binding.ivMedal.isInvisible = true
                binding.tvNumber.isInvisible = false
                binding.tvNumber.text = item.place.toString()
            }
        }

        Glide.with(binding.ivAvatar).load(item.avatar_url).into(binding.ivAvatar)
    }

    @SuppressLint("NewApi")
    fun formatToTopPlayerDate(dateString: String): String {
        val date = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
        val now = LocalDateTime.now()
        return when {
            date.dayOfMonth == now.dayOfMonth -> {
                "Сегодня"
            }
            date.year == now.year -> {
                date.format(DateTimeFormatter.ofPattern("dd.MM"))
            }
            else -> {
                date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }
        }
    }
}


class TopPlayerDiffUtilCallback : DiffUtil.ItemCallback<TopPlayerDto>() {
    override fun areItemsTheSame(oldItem: TopPlayerDto, newItem: TopPlayerDto): Boolean {
        return oldItem.nickname == newItem.nickname
    }

    override fun areContentsTheSame(oldItem: TopPlayerDto, newItem: TopPlayerDto): Boolean {
        return oldItem == newItem
    }
}