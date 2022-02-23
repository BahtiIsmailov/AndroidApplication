package com.example.englishapp.view.main.words.wordsinwordset.learning

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.englishapp.R
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.utils.SoundPlayer
import com.example.englishapp.utils.placeholderBackgroundColorList
import com.example.englishapp.utils.placeholderBackgroundColorListIterator
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class CardStackAdapter(
    private var words: List<Word> = emptyList(),
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_learning_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = words[position]
        holder.originalWord.text = word.word
        holder.translateWord.text = word.translate
        holder.imageView.setImageDrawable(null)

        val url = word.pictures.firstOrNull()?.url ?: ""
        if (url.isNotEmpty()) {
            Glide.with(holder.imageView).load(url).into(holder.imageView)
        }

        holder.audio.setOnClickListener {
            SoundPlayer.playWord(word.audio, word.translate)
        }

        fun setPlaceholder() {

            if (!placeholderBackgroundColorListIterator.hasNext()) {
                placeholderBackgroundColorList.shuffled()
                placeholderBackgroundColorListIterator =
                    placeholderBackgroundColorList.listIterator()
            }

            holder.imageView.setBackgroundColor(
                Color.parseColor(
                    placeholderBackgroundColorListIterator.next()
                )
            )
            //holder.imageView.setImageResource(R.drawable.ic_statue_of_liberty)
            //holder.imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        if (url.isEmpty()) {
            setPlaceholder()
        } else {
            Glide.with(holder.imageView)
                .load(url)
                .placeholder(R.drawable.ic_statue_of_liberty)
                .into(holder.imageView)
        }

        holder.endLayout.alpha = word.alpha
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setWords(spots: List<Word>) {
        this.words = spots
    }

    fun getWords(): List<Word> {
        return words
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.cardImageWord)
        var originalWord: TextView = view.findViewById(R.id.cardOriginalWord)
        var translateWord: TextView = view.findViewById(R.id.cardTranslateWord)
        var audio: ImageView = view.findViewById(R.id.cardImageVoice)
        var endLayout: FrameLayout = view.findViewById(R.id.endSelectLayout)
    }
}