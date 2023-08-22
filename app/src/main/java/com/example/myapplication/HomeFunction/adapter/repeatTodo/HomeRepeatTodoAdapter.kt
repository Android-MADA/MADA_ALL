package com.example.myapplication.HomeFunction.adapter.repeatTodo

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.Model.repeatTodo
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.time.HomeTimeColorAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R

class HomeRepeatTodoAdapter (private var view : View?, private var flag : String? ) : RecyclerView.Adapter<HomeRepeatTodoAdapter.viewHolder>() {

    lateinit var dataSet : ArrayList<repeatTodo>
    var topFlag = false
    var cateIndex = 0
    var viewModel : HomeViewModel? = null
    var api : HomeApi? = null

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val tvTodo : TextView
        val todoMenu : TextView
        val editLayout : LinearLayout
        val edtTodo : EditText
        val todoLayout : ConstraintLayout

        init {
            tvTodo = view.findViewById(R.id.tv_repeat_todo)
            todoMenu = view.findViewById(R.id.tv_repeat_todo_edit)
            editLayout = view.findViewById(R.id.layout_repeat_todo_edit)
            edtTodo = view.findViewById(R.id.edt_repeat_todo_edit)
            todoLayout = view.findViewById(R.id.layout_repeat_todo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_repeat_todo_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        if(dataSet.isNotEmpty()){
            holder.tvTodo.text = dataSet[position].todoName
            if(flag == "my"){
                holder.todoMenu.isInvisible = true
            }

            holder.todoMenu.setOnClickListener {
                val popup = PopupMenu(holder.itemView.context, it)
                popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    if(item.itemId == R.id.home_todo_edit) {
                        val bundle = Bundle()

                        bundle.putStringArrayList("keyEdit", arrayListOf(
                            dataSet[position].id.toString(),
                            dataSet[position].todoName,
                            dataSet[position].repeat,
                            dataSet[position].repeatWeek,
                            dataSet[position].repeatMonth,
                            dataSet[position].startRepeatDate,
                            dataSet[position].endRepeatDate,
                            cateIndex.toString(),
                            position.toString()
                        ))

                        Navigation.findNavController(view!!).navigate(R.id.action_homeRepeatTodoFragment_to_repeatTodoAddFragment, bundle)
                    }
                    else{
                        val todoId = dataSet[position].id
                        viewModel!!.deleteRepeatTodo(todoId, cateIndex, position, this)
                    }
                    true
                }
                popup.show()
            }
        }
    }
}