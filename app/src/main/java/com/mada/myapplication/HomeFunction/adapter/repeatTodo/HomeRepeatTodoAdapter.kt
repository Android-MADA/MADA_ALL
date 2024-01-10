package com.mada.myapplication.HomeFunction.adapter.repeatTodo

import android.os.Bundle
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
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.HomeFunction.Model.Todo
import com.mada.myapplication.HomeFunction.Model.repeatTodo
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R

class HomeRepeatTodoAdapter (private var view : View?, private var flag : String? ) : RecyclerView.Adapter<HomeRepeatTodoAdapter.viewHolder>() {

    var dataSet : ArrayList<repeatTodo>? = null
    var dataSet2 : ArrayList<Todo>? = null
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
        val imIcon : ImageView

        init {
            tvTodo = view.findViewById(R.id.tv_repeat_todo)
            todoMenu = view.findViewById(R.id.tv_repeat_todo_edit)
            editLayout = view.findViewById(R.id.layout_repeat_todo_edit)
            edtTodo = view.findViewById(R.id.edt_repeat_todo_edit)
            todoLayout = view.findViewById(R.id.layout_repeat_todo)
            imIcon = view.findViewById(R.id.iv_repeat_todo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_repeat_todo_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        val a = if(flag == "my"){
            dataSet2?.size
        }else {
            dataSet?.size
        }
        return a!!
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        if(flag != "my"){
            if(dataSet!!.isNotEmpty()){
                holder.tvTodo.text = dataSet!![position].todoName
                holder.imIcon.setImageResource(R.drawable.home_checkbox_symbol_unchecked)
                holder.todoMenu.isVisible = true

                holder.todoMenu.setOnClickListener {
                    val popup = PopupMenu(holder.itemView.context, it)
                    popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        if(item.itemId == R.id.home_todo_edit) {
                            val bundle = Bundle()

                            bundle.putStringArrayList("keyEdit", arrayListOf(
                                dataSet!![position].id.toString(),
                                dataSet!![position].todoName,
                                dataSet!![position].repeat,
                                dataSet!![position].repeatWeek,
                                dataSet!![position].repeatMonth,
                                dataSet!![position].startRepeatDate,
                                dataSet!![position].endRepeatDate,
                                cateIndex.toString(),
                                position.toString()
                            ))

                            Navigation.findNavController(view!!).navigate(R.id.action_homeRepeatTodoFragment_to_repeatTodoAddFragment, bundle)
                        }
                        else{
                            val todoId = dataSet!![position].id
                            //viewModel!!.deleteRepeatTodo(todoId, cateIndex, position, this)
                        }
                        true
                    }
                    popup.show()
                }
            }
        }
        else {
            if(dataSet2!!.isNotEmpty()){
                holder.tvTodo.text = dataSet2!![position].todoName
                if(dataSet2!![position].complete == true){
                    holder.imIcon.setImageResource(findRes(dataSet2!![position].category.color))
                }
                holder.todoMenu.isGone = true
            }
        }


    }

    fun findRes(color : String) : Int {
        var colorr : Int = when(color){
            "#21C362" -> {R.drawable.ch_checked_color1}
            "#0E9746" -> {R.drawable.ch_checked_color2}
            "#7FC7D4" -> {R.drawable.ch_checked_color3}
            "#2AA1B7" -> {R.drawable.ch_checked_color4}
            "#89A9D9" -> {R.drawable.ch_checked_color5}
            "#486DA3" -> {R.drawable.ch_checked_color6}
            "#FDA4B4" -> {R.drawable.ch_checked_color7}
            "#F0768C" -> {R.drawable.ch_checked_color8}
            "#F8D141" -> {R.drawable.ch_checked_color9}
            "#F68F30" -> {R.drawable.ch_checked_color10}
            "#F33E3E" -> {R.drawable.ch_checked_color11}
            else -> {R.drawable.ch_checked_color12}
        }
        return colorr

    }
}