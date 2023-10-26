package com.itis.android23.holder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.itis.android23.databinding.ItemAnswerBinding
import com.itis.android23.model.Answer

class AnswerHolder(
    private val binding: ItemAnswerBinding,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.rbAnswer.setOnClickListener {
            onItemChecked.invoke(adapterPosition)
        }
        binding.root.setOnClickListener {
            onRootClicked.invoke(adapterPosition)
        }
    }

    fun onBind(answer: Answer) {
        with(binding) {
            tvAnswer.text = answer.answerText
            rbAnswer.isChecked = answer.isSelected
            rbAnswer.isEnabled = !answer.isSelected
            root.foreground = if (answer.isSelected) {
                null // Make the selected item look normal
            } else {
                ColorDrawable(Color.TRANSPARENT) // Add transparent foreground to non-selected items
            }
        }
    }
}
