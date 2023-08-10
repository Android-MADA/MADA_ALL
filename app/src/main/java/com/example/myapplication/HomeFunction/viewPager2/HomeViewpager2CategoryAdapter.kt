package com.example.myapplication.HomeFunction.viewPager2

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeViewpager2CategoryAdapter(private val dataSet : ArrayList<SampleHomeCateData> ) : RecyclerView.Adapter<HomeViewpager2CategoryAdapter.viewHolder>(){


    lateinit var todoAdapter : HomeViewpager2TodoAdapter
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_catagory_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        todoAdapter = HomeViewpager2TodoAdapter(dataSet[position].todoList)
        todoAdapter.setItemClickListener(object : HomeViewpager2TodoAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //edt, btn 노출 with text
                //cb, tx, menu visibility gone
            }

        })
        holder.cateIcon.setImageResource(dataSet[position].icon)
        holder.cateTv.text = dataSet[position].cateName
        holder.todoRv.apply {
            adapter = todoAdapter
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

        holder.edtTodo.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                itemClickListener.onClick(view, position, dataSet[position].cateName, holder.edtTodo, holder.todoAdd)
                true
            }

            false
        }
}
    interface OnItemClickListener {
        fun onClick(v: View, position: Int, cate : String, edt : EditText, layout : LinearLayout)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: HomeViewpager2CategoryAdapter.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : HomeViewpager2CategoryAdapter.OnItemClickListener
}
