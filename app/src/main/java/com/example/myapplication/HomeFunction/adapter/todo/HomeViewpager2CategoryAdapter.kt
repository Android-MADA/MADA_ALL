package com.example.myapplication.HomeFunction.adapter.todo

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
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.R

class HomeViewpager2CategoryAdapter(private val cateDataSet : ArrayList<Category>) : RecyclerView.Adapter<HomeViewpager2CategoryAdapter.viewHolder>(){

    var todoDataSet : ArrayList<ArrayList<Todo>>? = null
    var todoAdapter : HomeViewpager2TodoAdapter? = null
    var completeNum = 0

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
        return cateDataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        if(todoDataSet != null){
            if(todoDataSet!![position].isNotEmpty()){
                todoAdapter = HomeViewpager2TodoAdapter(todoDataSet!![position], completeNum)
                todoAdapter!!.setItemClickListener(object :
                    HomeViewpager2TodoAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        holder.todoRv.post {
                            notifyDataSetChanged()
                        }
                    }

                })
                holder.todoRv.apply {
                    adapter = todoAdapter
                    layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                }
            }
        }

        //todolist가 empty인지 확인하고
        //todolist가 각 position에 대하여 empty인지 확인하고
        //empty이면 adapter 연결 x


        holder.cateIcon.setImageResource(cateDataSet[position].icon_id.name.toInt())
        holder.cateTv.text = cateDataSet[position].categoryName

        //클릭 리스너
        holder.addBtn.setOnClickListener{
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
                itemClickListener.onClick(view, position, cateDataSet[position].categoryName, holder.edtTodo, holder.todoAdd)
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
