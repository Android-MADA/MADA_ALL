package com.example.myapplication.HomeFunction.viewPager2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeViewpager2TodoAdapter(private val dataSet : ArrayList<SampleHomeTodoData>) : RecyclerView.Adapter<HomeViewpager2TodoAdapter.viewHolder>() {

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val todoCheckBox : CheckBox
        val tvTodo : TextView
        val todoMenu : TextView

        init {
            todoCheckBox = view.findViewById(R.id.cb_home_todo)
            tvTodo = view.findViewById(R.id.tv_home_todo)
            todoMenu = view.findViewById(R.id.tv_home_todo_edit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_todo_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        //checkbox value change
        //menu창 누르면 메뉴창 오픈, 각 메뉴 별로 행동 설정
        holder.tvTodo.text = dataSet[position].todoName
        holder.todoCheckBox.isChecked = dataSet[position].done
        holder.todoMenu.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.home_todo_edit) {
                    Log.d("todoEdit", "수정하기")
                }
                else{
                    Log.d("todoDelete", "삭제하기")
                }
                true
            }
            popup.show()
        }
    }
}

//checkbox 처리 -> value 바뀌면 db 변경
//tv dataset에서 가져오서 뿌리고
// ... 버튼 처리 -> 수정하기 :  edt 등장, db 수정
//             -> 삭제하기 : 삭제하고 db 수정