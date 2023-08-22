package com.example.myapplication.HomeFunction.adapter.todo

import android.util.Log
import android.view.KeyEvent
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
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R

class HomeViewpager2TodoAdapter(private var flag : String?) : RecyclerView.Adapter<HomeViewpager2TodoAdapter.viewHolder>() {

    lateinit var dataSet : ArrayList<Todo>
    var cateIndex = 0
    var topFlag = false
    var completeFlag = false
    var viewModel : HomeViewModel? = null
    var api : HomeApi? = null

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val todoCheckBox : CheckBox
        val tvTodo : TextView
        val todoMenu : TextView
        val editLayout : LinearLayout
        val edtTodo : EditText
        val todoLayout : ConstraintLayout
        val repeatIcon : ImageView
        val ivIcon : ImageView

        init {
            todoCheckBox = view.findViewById(R.id.iv_repeat_todo)
            tvTodo = view.findViewById(R.id.tv_home_todo)
            todoMenu = view.findViewById(R.id.tv_home_todo_edit)
            editLayout = view.findViewById(R.id.layout_viewpager_todo_edit)
            edtTodo = view.findViewById(R.id.edt_home_viewpager2_todo_edit)
            todoLayout = view.findViewById(R.id.layout_viewpager_todo)
            repeatIcon = view.findViewById(R.id.iv_home_repeat)
            ivIcon = view.findViewById(R.id.iv_my_todo)
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


            var cbColor = R.drawable.home_checkbox1

            when(dataSet[position].category.color){
                "#E1E9F5" -> {cbColor = R.drawable.home_checkbox1}
                "#89A9D9" -> {cbColor = R.drawable.home_checkbox2}
                "#486DA3" -> {cbColor = R.drawable.home_checkbox3}
                "#FFE7EB" -> {cbColor = R.drawable.home_checkbox4}
                "#FDA4B4" -> {cbColor = R.drawable.home_checkbox5}
                "#F0768C" -> {cbColor = R.drawable.home_checkbox6}
                "#D4ECF1" -> {cbColor = R.drawable.home_checkbox7}
                "#7FC7D4" -> {cbColor = R.drawable.home_checkbox8}
                "#2AA1B7" -> {cbColor = R.drawable.home_checkbox9}
                "#FDF3CF" -> {cbColor = R.drawable.home_checkbox10}
                "#F8D141" -> {cbColor = R.drawable.home_checkbox11}
                else -> {cbColor = R.drawable.home_checkbox12}

            }



            holder.todoCheckBox.setBackgroundResource(cbColor)
            //menu창 누르면 메뉴창 오픈, 각 메뉴 별로 행동 설정
            holder.tvTodo.text = dataSet[position].todoName
            holder.todoCheckBox.isChecked = dataSet[position].complete

            if(dataSet[position].repeat != "N"){
                if(flag == "my"){
                    holder.todoMenu.isGone = true
                    holder.todoCheckBox.isInvisible = true
                    holder.ivIcon.isVisible = true
                }
                else {
                    holder.todoCheckBox.isVisible = true
                    holder.ivIcon.isGone = true
                    holder.todoMenu.isGone = true
                    holder.repeatIcon.isVisible = true
                }
            }
            else {
                if(flag == "my"){
                    holder.todoMenu.isGone = true
                    holder.todoCheckBox.isInvisible = true
                    holder.ivIcon.isVisible = true
                }
                else {
                    holder.todoCheckBox.isVisible = true
                    holder.ivIcon.isGone = true
                    holder.todoMenu.isVisible = true
                    holder.repeatIcon.isGone = true
                }
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
                        val todoId = dataSet[position].id
                        viewModel!!.deleteTodo(todoId, cateIndex, position, dataSet[position].complete, this)
                    }
                    true
                }
                popup.show()
            }

            holder.todoCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                dataSet[position].complete = buttonView.isChecked

                viewModel!!.patchTodo(dataSet[position].id, PatchRequestTodo(dataSet[position].todoName, dataSet[position].repeat, dataSet[position].repeatWeek, dataSet[position].repeatMonth, dataSet[position].startRepeatDate, dataSet[position].endRepeatDate, isChecked), cateIndex, position, null )
                if(buttonView.isChecked == true){
                    viewModel!!.updateCompleteTodo("add")
//                    if(completeFlag == true){
//                        var completeTodo = dataSet[position]
//                        dataSet.removeAt(position)
//                        dataSet.add(completeTodo)
//                    }
                }
                else if(buttonView.isChecked == false){
                    viewModel!!.updateCompleteTodo("delete")
//                    if(completeFlag == true){
//                        var completeTodo = dataSet[position]
//                        dataSet.removeAt(position)
//                        dataSet.add(0, completeTodo)
//                    }
                }
                //patch, completenum

//                if(isChecked){
//                    if(completeFlag){
//                        var todoMove = dataSet[position]
//                        dataSet.removeAt(position)
//                        dataSet.add(todoMove)
//                    }
//                }
//                else {
//                    if(completeFlag){
//                        var todoMove = dataSet[position]
//                        dataSet.removeAt(position)
//                        dataSet.add(0, todoMove)
//                    }
//
//                }
                notifyDataSetChanged()
            }

            holder.edtTodo.setOnKeyListener { view, keyCode, event ->
                // Enter Key Action
                if (event.action == KeyEvent.ACTION_DOWN
                    && keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    //데이터 수정 반영
                    viewModel!!.patchTodo(dataSet[position].id, PatchRequestTodo(holder.edtTodo.text.toString(), "N", null, null, null, null, dataSet[position].complete), cateIndex, position, null)
                    dataSet[position].todoName = holder.edtTodo.text.toString()
                    //edt 업애고 이전 투두 복구
                    holder.todoLayout.isVisible = true
                    holder.edtTodo.text.clear()
                    holder.editLayout.isGone = true

                    notifyDataSetChanged()
                    true
                }

                false
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int, dataSet : ArrayList<Todo>)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}
