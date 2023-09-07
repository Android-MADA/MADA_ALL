package com.example.myapplication.HomeFunction.adapter.todo

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.Model.PostResponseTodo
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeTodoListBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.TodoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class HomeTodoListAdapter : ListAdapter<TodoEntity, HomeTodoListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoEntity>(){
            override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
                //아이템  id 가 같은지 확인
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
                //아이템 내용이 같은 지 확인
                return oldItem == newItem
            }

        }
    }

    var viewModel : HomeViewModel? = null
    var category : CateEntity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        holder.bind(getItem(position))

        var cbColor = R.drawable.home_checkbox1

        when(category!!.color){
            "#21C362" -> {cbColor = R.drawable.home_checkbox1}
            "#0E9746" -> {cbColor = R.drawable.home_checkbox2}
            "#7FC7D4" -> {cbColor = R.drawable.home_checkbox3}
            "#2AA1B7" -> {cbColor = R.drawable.home_checkbox4}
            "#89A9D9" -> {cbColor = R.drawable.home_checkbox5}
            "#486DA3" -> {cbColor = R.drawable.home_checkbox6}
            "#FDA4B4" -> {cbColor = R.drawable.home_checkbox7}
            "#F0768C" -> {cbColor = R.drawable.home_checkbox8}
            "#F8D141" -> {cbColor = R.drawable.home_checkbox9}
            "#F68F30" -> {cbColor = R.drawable.home_checkbox10}
            "#F33E3E" -> {cbColor = R.drawable.home_checkbox11}
            else -> {cbColor = R.drawable.home_checkbox12}

        }

        holder.checkbox.setBackgroundResource(cbColor)

        if(holder.data!!.repeat != "N"){

            holder.checkbox.isVisible = true
            holder.ivIcon.isGone = true
            holder.todoMenu.isGone = true
            holder.repeatIcon.isVisible = true
        }
        else {
            holder.checkbox.isVisible = true
            holder.ivIcon.isGone = true
            holder.todoMenu.isVisible = true
            holder.repeatIcon.isGone = true
        }



        holder.edtTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ){
                //update
                val data = TodoEntity(holder.data!!.todoId, holder.data!!.id, holder.data!!.date, holder.data!!.category, holder.edtTodo.text.toString(), holder.data!!.complete, holder.data!!.repeat, holder.data!!.repeatWeek, holder.data!!.repeatMonth, holder.data!!.startRepeatDate, holder.data!!.endRepeatDate, holder.data!!.isAlarm, holder.data!!.startTodoAtMonday, holder.data!!.endTodoBackSetting, holder.data!!.newTodoStartSetting)
                viewModel!!.updateTodo(data)
                val updateData = PatchRequestTodo(todoName = holder.edtTodo.text.toString(), repeat = "N",  repeatWeek = holder.data!!.repeatWeek, repeatMonth = holder.data!!.repeatMonth, startRepeatDate = holder.data!!.startRepeatDate, endRepeatDate = holder.data!!.endRepeatDate, complete = holder.data!!.complete)
                api.editTodo(viewModel!!.userToken, holder.data!!.id!!, updateData).enqueue(object :Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.d("todo server", "성공")
                        }
                        else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("서버 문제", "서버 연결 실패")
                    }
                })
                holder.edtTodo.text.clear()
                holder.editLayout.isGone = true
                holder.layoutcb.isVisible = true
                true
            }
            false
        }

        holder.todoMenu.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.home_todo_edit) {
//                    Log.d("todoEdit", "수정하기")
//                    holder.editLayout.isVisible = true
//                    holder.layoutcb.isGone = true
//                    holder.edtTodo.setText(holder.data!!.todoName)
                    if(holder.editLayout.isVisible == true){
                        holder.editLayout.isGone = true
                        //checkbox 부분 활성화
                        holder.layoutcb.isVisible = true
                    }
                    else {
                        holder.editLayout.isVisible = true
                        holder.layoutcb.isGone = true
                        //checkbox 부분 비활성화
                        //edt에 text 채우기
                        holder.edtTodo.setText(getItem(position).todoName)
                    }
                }
                else{
                    //데이터 삭제
                    val data = holder.data
                    api.deleteTodo(viewModel!!.userToken, holder.data!!.id!!).enqueue(object :Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                Log.d("todo server", "성공")
                                viewModel!!.deleteTodo(data!!)
                            }
                            else {
                                Log.d("todo안드 잘못", "서버 연결 실패")
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("서버 문제", "서버 연결 실패")
                        }
                    })
                }
                true
            }
            popup.show()
        }

//        holder.btnDelete.setOnClickListener {
//            //데이터 삭제
//            val data = holder.data
//            viewModel!!.deleteTodo(data!!)
//            notifyDataSetChanged()
//        }

        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == true){
                //update
                holder.data!!.complete = true
                val checkUpdateData = holder.data!!
                val updateData = PatchRequestTodo(checkUpdateData.todoName, checkUpdateData.repeat, checkUpdateData.repeatWeek, checkUpdateData.repeatMonth, checkUpdateData.startRepeatDate, checkUpdateData.endRepeatDate, complete = true)
                viewModel!!.updateTodo(checkUpdateData)
                //서버 연결
                api.editTodo(viewModel!!.userToken, holder.data!!.id!!, updateData).enqueue(object :Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.d("todo server", "성공")
                        }
                        else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("서버 문제", "서버 연결 실패")
                    }
                })
                //자리 이동
            }
            else {
                //update
                holder.data!!.complete = false
                CoroutineScope(Dispatchers.IO).launch {
                    //서버 연결, db 저장
                    val checkUpdateData = holder.data!!
                    val updateData = PatchRequestTodo(todoName = checkUpdateData.todoName, repeat = "N",  repeatWeek = checkUpdateData.repeatWeek, repeatMonth = checkUpdateData.repeatMonth, startRepeatDate = checkUpdateData.startRepeatDate, endRepeatDate = checkUpdateData.endRepeatDate, complete = false)
                    Log.d("todoedit 확인", updateData.toString())
                    api.editTodo(viewModel!!.userToken, holder.data!!.id!!, updateData).enqueue(object :Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                    Log.d("todo server", "성공")
                            }
                            else {
                                Log.d("todo안드 잘못", "서버 연결 실패")
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("서버 문제", "서버 연결 실패")
                        }
                    })
                    viewModel!!.updateTodo(checkUpdateData)
                }
                //자리 이동
            }
        }
    }

    inner class ViewHolder(private val binding : HomeTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        var data : TodoEntity? = null
        fun bind(todoEntity : TodoEntity) {
            binding.ivRepeatTodo.isChecked = todoEntity.complete
            binding.tvHomeTodo.text = todoEntity.todoName
            data = todoEntity
        }
        val editLayout = binding.layoutViewpagerTodoEdit
//        val btnEdit = binding.edtHomeViewpager2TodoEdit
//        val btnDelete = binding.tvHomeDelete
        val edtTodo  = binding.edtHomeViewpager2TodoEdit
        val checkbox = binding.ivRepeatTodo
        val layoutcb = binding.layoutViewpagerTodo
        val ivIcon = binding.ivMyTodo
        val todoMenu = binding.tvHomeTodoEdit
        val repeatIcon = binding.ivHomeRepeat

    }
}