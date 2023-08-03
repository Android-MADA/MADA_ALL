package com.example.myapplication.HomeFunction.repeatTodo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.viewPager2.SampleHomeTodoData
import com.example.myapplication.R

class HomeRepeatTodoAdapter (private var dataSet : ArrayList<SampleHomeTodoData>) : RecyclerView.Adapter<HomeRepeatTodoAdapter.viewHolder>() {

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val todoCheckBox : ImageView
        val tvTodo : TextView
        val todoMenu : TextView
        val editLayout : LinearLayout
        val edtTodo : EditText
        val edtSave : ImageView
        val todoLayout : ConstraintLayout

        init {
            todoCheckBox = view.findViewById(R.id.iv_repeat_todo)
            tvTodo = view.findViewById(R.id.tv_repeat_todo)
            todoMenu = view.findViewById(R.id.tv_repeat_todo_edit)
            editLayout = view.findViewById(R.id.layout_repeat_todo_edit)
            edtTodo = view.findViewById(R.id.edt_repeat_todo_edit)
            edtSave = view.findViewById(R.id.iv_repeat_todo_save_edit)
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
        //checkbox value change
        //menu창 누르면 메뉴창 오픈, 각 메뉴 별로 행동 설정
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

                    itemClickListener.onClick(it, position)
                }
                else{
                    itemClickListener.onClick(it, position)
                    dataSet.removeAt(position)
                    notifyDataSetChanged()
                    Log.d("todoDelete", "삭제하기")
                }
                true
            }
            popup.show()
        }
        holder.edtSave.setOnClickListener {
            //데이터 수정 반영
            dataSet[position].todoName = holder.edtTodo.text.toString()
            //edt 업애고 이전 투두 복구
            holder.todoLayout.isVisible = true
            holder.edtTodo.text.clear()
            holder.editLayout.isGone = true
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: HomeRepeatTodoAdapter.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : HomeRepeatTodoAdapter.OnItemClickListener
}