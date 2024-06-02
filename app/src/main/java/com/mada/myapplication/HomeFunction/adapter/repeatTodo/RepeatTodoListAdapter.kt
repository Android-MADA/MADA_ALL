package com.mada.myapplication.HomeFunction.adapter.repeatTodo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.HomeFunction.Model.Category
import com.mada.myapplication.HomeFunction.Model.TodoList
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.dialog.RepeatBottomSheetDialog
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.HomeRepeatTodoListBinding
import com.mada.myapplication.databinding.HomeTodoListBinding
import com.mada.myapplication.db.entity.RepeatEntity
import com.mada.myapplication.db.entity.TodoEntity
import com.mada.myapplication.getHomeTodo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepeatTodoListAdapter(private val view : View, fragmentManager: FragmentManager, context: Context) : ListAdapter<RepeatEntity, RepeatTodoListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RepeatEntity>(){
            override fun areItemsTheSame(oldItem: RepeatEntity, newItem: RepeatEntity): Boolean {
                //아이템  id 가 같은지 확인
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RepeatEntity, newItem: RepeatEntity): Boolean {
                //아이템 내용이 같은 지 확인
                return oldItem == newItem
            }

        }
    }

    var viewModel : HomeViewModel? = null
    var calendarViewModel : CalendarViewModel? = null
    var category : Category? = null
    private var mFragmentManager = fragmentManager
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeRepeatTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
        holder.bind(getItem(position))

        holder.todoMenu.setOnClickListener {

            val repeatMenuBottomSheetDialog = RepeatBottomSheetDialog(){
                when(it){
                    0 ->{
                        //삭제
                        Log.d("repeatMenu", "delete")
                        calendarViewModel!!.setPopupTwo2(mContext, "정말 삭제하시겠습니까?", flag = "deleteRepeat"){
                            result ->
                            when(result){
                                0 -> {
                                    val buffering = viewModel!!.setPopupBufferingTodo(mContext)
                                    val data = holder.data
                        //서버 연결 delete
                        api.deleteTodo(viewModel!!.userToken, holder.data!!.id!!).enqueue(object :Callback<Void>{
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    Log.d("Rtodo server", "성공")
                                    viewModel!!.deleteRepeatTodo(data!!)
                                    viewModel!!.deleteAllTodo()
                                    //투두 새로 서버 에서 읽어오기
                                    getHomeTodo(api, viewModel!!, mContext)
                                }
                                else {
                                    Log.d("todo안드 잘못", "서버 연결 실패")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("서버 문제", "서버 연결 실패")
                            }
                        })
                                    buffering.dismiss()
                                }
                                else -> {
                                    Log.d("repeatTodo delete", "fail")
                                }
                            }
                        }
//
                    }
                    1 -> {
                        //수정
                        val buffering = viewModel!!.setPopupBufferingTodo(mContext)
                        Log.d("repeatMenu", "edit")
                        val bundle = Bundle()

                        bundle.putStringArrayList("keyEdit", arrayListOf(
                            holder.data!!.id.toString(),
                            holder.data!!.date,
                            holder.data!!.category.toString(),
                            holder.data!!.todoName,
                            holder.data!!.repeat,
                            holder.data!!.repeatInfo.toString(),
                            holder.data!!.startRepeatDate,
                            holder.data!!.endRepeatDate,
                        ))
                        viewModel!!.selectedRepeatTodo = holder.data!!

                        Navigation.findNavController(view!!).navigate(R.id.action_homeRepeatTodoFragment_to_repeatTodoAddFragment, bundle)
                        buffering.dismiss()
                    }
                }
            }
            repeatMenuBottomSheetDialog.show(mFragmentManager, repeatMenuBottomSheetDialog.tag)
        }
    }

    inner class ViewHolder(private val binding : HomeRepeatTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        var data : RepeatEntity? = null
        fun bind(repeatTodoEntity : RepeatEntity) {
            binding.tvRepeatTodo.text = repeatTodoEntity.todoName
            binding.ivRepeatTodo.setImageResource(R.drawable.home_checkbox_symbol_unchecked)
            binding.tvRepeatTodoEdit.isVisible = true
            data = repeatTodoEntity
        }
        val todoMenu = binding.tvRepeatTodoEdit

    }
}