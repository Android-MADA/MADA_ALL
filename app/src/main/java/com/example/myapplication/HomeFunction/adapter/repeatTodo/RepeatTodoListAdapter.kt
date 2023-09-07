package com.example.myapplication.HomeFunction.adapter.repeatTodo

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeRepeatTodoListBinding
import com.example.myapplication.databinding.HomeTodoListBinding
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity

class RepeatTodoListAdapter(private val view : View) : ListAdapter<RepeatEntity, RepeatTodoListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RepeatEntity>(){
            override fun areItemsTheSame(oldItem: RepeatEntity, newItem: RepeatEntity): Boolean {
                //아이템  id 가 같은지 확인
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RepeatEntity, newItem: RepeatEntity): Boolean {
                //아이템 내용이 같은 지 확인
                return oldItem == newItem
            }

        }
    }

    var viewModel : HomeViewModel? = null
    var category : Category? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeRepeatTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))

        holder.todoMenu.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.home_todo_edit) {
                    val bundle = Bundle()

                    bundle.putStringArrayList("keyEdit", arrayListOf(
                        holder.data!!.todoId.toString(),
                        holder.data!!.id.toString(),
                        holder.data!!.date,
                        holder.data!!.category.toString(),
                        holder.data!!.todoName,
                        holder.data!!.repeat,
                        holder.data!!.repeatWeek,
                        holder.data!!.repeatMonth,
                        holder.data!!.startRepeatDate,
                        holder.data!!.endRepeatDate,
                    ))

                    Navigation.findNavController(view!!).navigate(R.id.action_homeRepeatTodoFragment_to_repeatTodoAddFragment, bundle)
                }
                else{
                    val data = holder.data
                    viewModel!!.deleteRepeatTodo(data!!)
                    //서버 연결 delete
                    notifyDataSetChanged()
                }
                true
            }
            popup.show()
        }
    }

    inner class ViewHolder(private val binding : HomeRepeatTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        var data : RepeatEntity? = null
        fun bind(repeatTodoEntity : RepeatEntity) {
            binding.tvRepeatTodo.text = repeatTodoEntity.todoName
            binding.ivRepeatTodo.setImageResource(R.drawable.home_checkbox_symbol_unchecked)
            binding.tvRepeatTodoEdit.isVisible = true
            data = repeatTodoEntity
        }
        val todoMenu = binding.tvRepeatTodoEdit

    }
}