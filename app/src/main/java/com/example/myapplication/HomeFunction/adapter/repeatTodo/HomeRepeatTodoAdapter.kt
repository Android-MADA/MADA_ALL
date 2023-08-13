package com.example.myapplication.HomeFunction.adapter.repeatTodo

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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.R

class HomeRepeatTodoAdapter (private var dataSet : ArrayList<Todo>, private var completeNum : Int) : RecyclerView.Adapter<HomeRepeatTodoAdapter.viewHolder>() {

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

            holder.todoMenu.setOnClickListener {
                val popup = PopupMenu(holder.itemView.context, it)
                popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    if(item.itemId == R.id.home_todo_edit) {
                        Log.d("todoEdit", "수정하기")
                        holder.editLayout.isVisible = true
                        holder.todoLayout.isGone = true
                        holder.edtTodo.setText(dataSet[position].todoName)
                    }
                    else{
                        itemClickListener.onClick(it, position)
                        dataSet.removeAt(position)
                        if(dataSet[position].complete){
                            completeNum--
                        }
                        Log.d("todoDelete", "삭제하기")
                        notifyDataSetChanged()
                    }
                    true
                }
                popup.show()
            }

            holder.edtTodo.setOnKeyListener { view, keyCode, event ->
                // Enter Key Action
                if (keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    //데이터 수정 반영
                    dataSet[position].todoName = holder.edtTodo.text.toString()
                    //edt 업애고 이전 투두 복구
                    holder.todoLayout.isVisible = true
                    holder.edtTodo.text.clear()
                    holder.editLayout.isGone = true
                    itemClickListener.onClick(view, position)

                    true
                }

                false
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}