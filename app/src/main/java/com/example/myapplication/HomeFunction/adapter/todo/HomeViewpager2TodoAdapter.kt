package com.example.myapplication.HomeFunction.adapter.todo

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.R

class HomeViewpager2TodoAdapter(private var dataSet : ArrayList<Todo>, var completeNum : Int, var arrange : Boolean) : RecyclerView.Adapter<HomeViewpager2TodoAdapter.viewHolder>() {

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val todoCheckBox : CheckBox
        val tvTodo : TextView
        val todoMenu : TextView
        val editLayout : LinearLayout
        val edtTodo : EditText
        val todoLayout : ConstraintLayout

        init {
            todoCheckBox = view.findViewById(R.id.iv_repeat_todo)
            tvTodo = view.findViewById(R.id.tv_home_todo)
            todoMenu = view.findViewById(R.id.tv_home_todo_edit)
            editLayout = view.findViewById(R.id.layout_viewpager_todo_edit)
            edtTodo = view.findViewById(R.id.edt_home_viewpager2_todo_edit)
            todoLayout = view.findViewById(R.id.layout_viewpager_todo)
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

        if(dataSet.isNotEmpty()){

            //checkbox value change
            //카테고리 아이디에 따라 다르게 넣기 -> 동적으로 변화해서...따로 livedata나 다른 서버 연결 하고서 다듬어야 될 듯..
            var cbColor = R.drawable.home_checkbox1
            when(dataSet[position].categoryId.categoryName){
                "약속" -> {cbColor = R.drawable.home_checkbox1}
                "2" -> {cbColor = R.drawable.home_checkbox2}
                "3" -> {cbColor = R.drawable.home_checkbox3}
                "4" -> {cbColor = R.drawable.home_checkbox4}
                "운동" -> {cbColor = R.drawable.home_checkbox5}
                "공부" -> {cbColor = R.drawable.home_checkbox6}
                "7" -> {cbColor = R.drawable.home_checkbox7}
                else -> {cbColor = R.drawable.home_checkbox6}

            }
            holder.todoCheckBox.setBackgroundResource(cbColor)
            //menu창 누르면 메뉴창 오픈, 각 메뉴 별로 행동 설정
            holder.tvTodo.text = dataSet[position].todoName
            holder.todoCheckBox.isChecked = dataSet[position].complete
            if(dataSet[position].repeat == "day"){
                holder.todoMenu.isInvisible = true
            }

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
                        Log.d("todoDelete", "삭제하기")
                        notifyDataSetChanged()
                        if(holder.todoCheckBox.isChecked){
                            completeNum--
                        }
                    }
                    true
                }
                popup.show()
            }

            holder.todoCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                dataSet[position].complete = buttonView.isChecked

                if(isChecked){
                    completeNum++
                    if(arrange){
                        var todoMove = dataSet[position]
                        dataSet.removeAt(position)
                        dataSet.add(todoMove)
                    }
                }
                else {
                    completeNum--
                    if(arrange){
                        var todoMove = dataSet[position]
                        dataSet.removeAt(position)
                        dataSet.add(0, todoMove)
                    }

                }

                Log.d("ch확인", "${dataSet[position].todoName} : ${dataSet[position].complete}")
                itemClickListener.onClick(buttonView, position)
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
