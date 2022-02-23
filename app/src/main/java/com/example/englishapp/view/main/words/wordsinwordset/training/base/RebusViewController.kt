package com.example.englishapp.view.main.words.wordsinwordset.training.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.englishapp.R
import com.example.englishapp.databinding.ContentTrainingRebusWordBinding
import com.example.englishapp.utils.toPx
import com.example.englishapp.view.customview.RebusButtonView

class RebusViewController(
    val context: Context,
    val _binding: ContentTrainingRebusWordBinding?,
    val baseView: TrainingView?
) {

    val binding get() = _binding!!

    var currentLetters = listOf<Char>()

    val handler = Handler(Looper.getMainLooper())
    private var runnableList = arrayListOf<Runnable>()

    private var errorCount = 0

    fun splitWordToLetters(word: String) {
        val letters = word
            .filter { !it.isWhitespace() }
            .lowercase()
            .toCharArray()
            .toMutableList()
            .shuffled()
            .groupBy { it }
        createLetters(letters)
    }

    private fun createLetters(
        letters: Map<Char, List<Char>>
    ) {
        val ll = getHorizontalLinearLayout()
        val ll2 = getHorizontalLinearLayout()

        letters.entries.forEachIndexed { index, entry ->
            if (letters.entries.size <= 6) {
                //for words less 6 symbols

                ll2.addView(getLetterView(entry.key.toString(), entry.value.size))

            } else {
                //words more 6 symbols
                if (4 <= index) {
                    ll.addView(getLetterView(entry.key.toString(), entry.value.size))
                } else {
                    binding.letterContainer.removeAllViews()
                    binding.letterContainer.addView(ll)
                    ll2.addView(getLetterView(entry.key.toString(), entry.value.size))
                }

            }

            binding.letterContainer2.removeAllViews()
            binding.letterContainer2.addView(ll2)
        }
    }

    private fun getHorizontalLinearLayout(): LinearLayout {
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return ll
    }

    private fun getLetterView(letter: String, count: Int): View {
        return RebusButtonView(context).apply {
            setLetter(letter, isFirstLetter(letter))
            setBadge(count)

            layoutParams = LinearLayout.LayoutParams(
                52.toPx().toInt(),
                52.toPx().toInt()
            ).apply {
                weight = 1f
            }

            binding.button.setOnClickListener {
                if (_binding == null) return@setOnClickListener

                onLetterClick(binding.button.text, onSuccess = {
                    badgeDecrement()

                    if (!hasNextLetter()) {
                        baseView?.onSuccess(AnimationType.Check)
                    }

                }, onError = {
                    binding.button.setTextColor(ContextCompat.getColor(context, R.color.red))

                    if (errorCount == ERROR_MAX_COUNT) {
                        baseView?.onFail()
                    }

                    val runnable = Runnable {
                        _binding?.button?.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                    }

                    runnableList.add(runnable)
                    handler.postDelayed(runnable, BUTTON_ERROR_COLOR_TIMEOUT)

                    errorCount++
                })
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun onLetterClick(letter: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (_binding == null) return

        while (getNextLetter().contains(" ")) {
            binding.tvText.text =
                _binding.tvText.text.toString() + " "
        }

        if (letter.lowercase() == getNextLetter().lowercase()) {
            binding.tvText.text =
                binding.tvText.text.toString() + letter
            onSuccess.invoke()
        } else {
            onError.invoke()
        }
    }

    private fun getNextLetter(): String {
        val positionForCompare =
            _binding?.tvText?.text?.length?.coerceAtLeast(0) ?: 0
        return currentLetters.getOrNull(positionForCompare)?.toString() ?: ""
    }

    private fun hasNextLetter(): Boolean {
        return currentLetters.size != _binding?.tvText?.text?.length ?: -1
    }

    private fun isFirstLetter(letter: String): Boolean {
        return currentLetters.firstOrNull()?.lowercase() == letter
    }
}

private const val BUTTON_ERROR_COLOR_TIMEOUT = 1000L
private const val ERROR_MAX_COUNT = 2