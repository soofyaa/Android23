package com.itis.android23.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.android23.databinding.ItemAnswerBinding
import com.itis.android23.holder.AnswerHolder
import com.itis.android23.model.Answer

class AnswerAdapter(
    val list: List<Answer>,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit,
) : RecyclerView.Adapter<AnswerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        return AnswerHolder(
            ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemChecked,
            onRootClicked,
        )
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
