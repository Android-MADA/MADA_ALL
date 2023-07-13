package com.example.ticker.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.namespace.databinding.LayoutDefaultTickerTimeBinding
import com.example.ticker.utils.toDoubleDigitString

class TickerTimeAdapter : ListAdapter<String, TickerTimeAdapter.DefaultTickerViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultTickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutDefaultTickerTimeBinding.inflate(inflater, parent, false)
        return DefaultTickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefaultTickerViewHolder, position: Int) {
        val time = getItem(position)
        holder.bindTime(time)
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 200
        holder.itemView.requestLayout()
    }


    inner class DefaultTickerViewHolder(val binding: LayoutDefaultTickerTimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTime(time: String) {
            if(time.length==1)
                binding.root.text = "0"+time
            else
                binding.root.text = time
        }

    }
}