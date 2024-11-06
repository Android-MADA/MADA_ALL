package com.mada.reapp.MyFunction.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mada.reapp.databinding.MyNoticeItemBinding

data class Notice(val title: String, val content: String, val date: String)
class MyNoticeAdapter(private val noticeList: List<Notice>) :

    RecyclerView.Adapter<MyNoticeAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: MyNoticeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: Notice) {
            binding.title.text = notice.title
            binding.content.text = notice.content
            binding.date.text = notice.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MyNoticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(noticeList[position])
    }

    override fun getItemCount() = noticeList.size
}