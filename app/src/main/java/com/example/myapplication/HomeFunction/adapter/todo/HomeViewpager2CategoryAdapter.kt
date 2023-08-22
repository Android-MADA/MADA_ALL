package com.example.myapplication.HomeFunction.adapter.todo

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.HomeFunction.adapter.repeatTodo.HomeRepeatTodoAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R

class HomeViewpager2CategoryAdapter(private var flag : String?) : RecyclerView.Adapter<HomeViewpager2CategoryAdapter.viewHolder>(){

    lateinit var dataSet : ArrayList<Category>
    var cateTodoSet : ArrayList<ArrayList<Todo>>? = null
    var repeatAdapter : HomeViewpager2TodoAdapter? = null
    var repeatAdapter2 : HomeRepeatTodoAdapter? = null
    var topFlag = false
    var viewModel : HomeViewModel? = null
    var completeFlag = false
    var api : HomeApi? = null

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

        if(cateTodoSet != null){
            if(cateTodoSet!![position].isNotEmpty()){
                if(flag == null){
                    repeatAdapter = HomeViewpager2TodoAdapter(flag)
                    repeatAdapter!!.cateIndex = position
                    repeatAdapter!!.dataSet = cateTodoSet!![position]
                    repeatAdapter!!.topFlag = topFlag
                    repeatAdapter!!.viewModel = viewModel
                    repeatAdapter!!.api = api
                    repeatAdapter!!.completeFlag = completeFlag

                    holder.todoRv.apply {
                        adapter = repeatAdapter
                        layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
                        setHasFixedSize(true)
                    }
                }
                else {
                    Log.d("my adapter", "실행중")
                    repeatAdapter2 = HomeRepeatTodoAdapter(null, "my")
                    repeatAdapter2!!.cateIndex = position
                    repeatAdapter2!!.dataSet2 = cateTodoSet!![position]
                    repeatAdapter2!!.topFlag = topFlag

                    holder.todoRv.apply {
                        adapter = repeatAdapter2
                        layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
                        setHasFixedSize(true)
                    }
                }


            }
        }

        if(flag == "my"){
            holder.addBtn.isInvisible = true
        }

        holder.cateIcon.setImageResource(findIcon(dataSet[position].iconId))
        holder.cateTv.text = dataSet[position].categoryName

        //클릭 리스너
        holder.addBtn.setOnClickListener{
            if(holder.todoAdd.isGone){
                holder.todoAdd.isVisible = true
            }
            else { holder.todoAdd.isGone = true }
        }

        holder.edtTodo.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                itemClickListener.onClick(view, position, dataSet[position].id, holder.edtTodo, holder.todoAdd)
                true
            }

            false
        }
}
    interface OnItemClickListener {
        fun onClick(v: View, position: Int, cate : Int, edt : EditText, layout : LinearLayout)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    fun findIcon(iconId : Int) : Int {
        val icon = when(iconId){
            1 -> {R.drawable.ic_home_cate_burn}
            2 -> {R.drawable.ic_home_cate_chat1}
            3 -> {R.drawable.ic_home_cate_health}
            4 -> {R.drawable.ic_home_cate_heart}
            5 -> {R.drawable.ic_home_cate_laptop}
            6 -> {R.drawable.ic_home_cate_lightout}
            7 -> {R.drawable.ic_home_cate_lightup}
            8 -> {R.drawable.ic_home_cate_meal2}
            9 -> {R.drawable.ic_home_cate_meal1}
            10 -> {R.drawable.ic_home_cate_mic}
            11 -> {R.drawable.ic_home_cate_music}
            12 -> {R.drawable.ic_home_cate_pen}
            13 -> {R.drawable.ic_home_cate_phone}
            14 -> {R.drawable.ic_home_cate_plan}
            15 -> {R.drawable.ic_home_cate_rest}
            16 -> {R.drawable.ic_home_cate_sony}
            17 -> {R.drawable.ic_home_cate_study}
            else -> {R.drawable.ic_home_cate_work}
        }
        return icon
    }
}
