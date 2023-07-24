package com.example.myapplication.HomeFunction.viewPager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeViewpager2CategoryAdapter(private val dataSet : ArrayList<SampleHomeCateData> ) : RecyclerView.Adapter<HomeViewpager2CategoryAdapter.viewHolder>(){


    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val cateIcon : ImageView
        val cateTv : TextView
        val todoAdd : LinearLayout
        val todoRv : RecyclerView
        val addBtn : ImageView
        val edtTodo : EditText

        init {
            cateIcon = view.findViewById(R.id.ic_home_category)
            cateTv = view.findViewById(R.id.tv_home_category)
            todoAdd = view.findViewById(R.id.layout_home_todo_add)
            todoRv = view.findViewById(R.id.rv_home_todo)
            addBtn = view.findViewById(R.id.btn_home_add_todo)
            edtTodo = view.findViewById(R.id.edt_home_viewpager2_todo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_catagory_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.cateIcon.setImageResource(dataSet[position].icon)
        holder.cateTv.text = dataSet[position].cateName
        holder.todoRv.apply {
            adapter = HomeViewpager2TodoAdapter(dataSet[position].todoList)
            layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        //클릭 리스너
        holder.addBtn.setOnClickListener{
            //toggle 식으로 다시 누르면 사라짐..?
            if(holder.todoAdd.isGone){
                holder.todoAdd.isVisible = true
            }
            else {
                holder.todoAdd.isGone = true
            }
        }
        //edt 저장 리스너 작성하기
        holder.edtTodo.setOnFocusChangeListener { v, hasFocus ->
            edtFocusChangeListener.onFocusChange(v, hasFocus)
        }
    }

    interface OnFocusChangeListener {
        fun onFocusChange(v : View, hasFocus : Boolean)
    }

    fun setFocusChangeListener(onFocusChangeListener: OnFocusChangeListener){
        this.edtFocusChangeListener = onFocusChangeListener
    }

    private lateinit var edtFocusChangeListener: OnFocusChangeListener
}

//1. db 에서 아이콘, 이름, 리스트 받아와서 뿌리고,
//2. onclick이벤트 설정해서 edt visibility 설정하고 트리거 발생시 저장해서 리사이클러뷰 업데이트, db 업데이트, 서버 전송