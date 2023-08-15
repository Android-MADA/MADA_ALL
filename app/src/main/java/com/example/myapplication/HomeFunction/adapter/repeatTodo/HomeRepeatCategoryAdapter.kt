package com.example.myapplication.HomeFunction.adapter.repeatTodo

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.R

class HomeRepeatCategoryAdapter(private var view : View) : RecyclerView.Adapter<HomeRepeatCategoryAdapter.viewHolder>(){

    lateinit var dataSet : ArrayList<Category>
    var cateTodoSet : ArrayList<ArrayList<Todo>>? = null
    var repeatAdapter : HomeRepeatTodoAdapter? = null
    var topFlag = false

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val cateIcon : ImageView
        val cateTv : TextView
        val todoAdd : LinearLayout
        val todoRv : RecyclerView
        val addBtn : ImageView
        val edtTodo : EditText

        init {
            cateIcon = view.findViewById(R.id.ic_repeat_category)
            cateTv = view.findViewById(R.id.tv_repeat_category)
            todoAdd = view.findViewById(R.id.layout_repeat_todo_add)
            todoRv = view.findViewById(R.id.rv_repeat_todo)
            addBtn = view.findViewById(R.id.btn_repeat_add_todo)
            edtTodo = view.findViewById(R.id.edt_repeat_todo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_repeat_cate_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {



        if(cateTodoSet != null){
            if(cateTodoSet!![position].isNotEmpty()){

                repeatAdapter = HomeRepeatTodoAdapter(view)
                repeatAdapter!!.dataSet = cateTodoSet!![position]
                repeatAdapter!!.topFlag = topFlag

                repeatAdapter!!.setItemClickListener(object :
                    HomeRepeatTodoAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int, dataSet : ArrayList<Todo>) {
                    }

                })
                holder.todoRv.apply {
                    adapter = repeatAdapter
                    layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                }
            }
        }

        holder.cateIcon.setImageResource(dataSet[position].icon_id.toInt())
        holder.cateTv.text = dataSet[position].categoryName

        //클릭 리스너
        holder.addBtn.setOnClickListener{
            //repeatAdd로 이동(cateId, 넘기기)
            if(holder.todoAdd.isGone){
                holder.todoAdd.isVisible = true
            }
            else { holder.todoAdd.isGone = true }

//            val bundle = Bundle()
//
//            bundle.putStringArrayList("keyAdd", arrayListOf(
//                dataSet[position].id.toString(),
//                dataSet[position].categoryName,
//                dataSet[position].color,
//                dataSet[position].icon_id
//            ))
//
//            Navigation.findNavController(view).navigate(R.id.action_homeRepeatTodoFragment_to_repeatTodoAddFragment, bundle)
        }
        //sample btn click listener
        holder.edtTodo.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                itemClickListener.onClick(view, position, dataSet[position].categoryName, holder.edtTodo, holder.todoAdd)
                true
            }

            false
        }
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int, cate : String, edt : EditText, layout : LinearLayout)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

}