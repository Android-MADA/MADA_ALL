package com.example.myapplication.HomeFunction.adapter.todo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.PostRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestTodoCateId
import com.example.myapplication.HomeFunction.Model.PostResponseTodo
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeCatagoryListBinding
import com.example.myapplication.databinding.HomeEditCategoryListBinding
import com.example.myapplication.db.MyApp
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.TodoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeCateListAdapter(private val view : View?) : ListAdapter<CateEntity, HomeCateListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CateEntity>(){
            override fun areItemsTheSame(oldItem: CateEntity, newItem: CateEntity): Boolean {
                //아이템  id 가 같은지 확인
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CateEntity, newItem: CateEntity): Boolean {
                //아이템 내용이 같은 지 확인
                return oldItem == newItem
            }

        }
    }

    //viewmodel 가져오기
    var viewModel : HomeViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeCatagoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cateId = holder.bind(getItem(position))
        Log.d("HomeCateItem", cateId.toString())
        //todoadapter 연결하기

            if(holder.data!!.isInActive == true){
//                holder.btnAdd.isGone = true
//                runBlocking {
//                    viewModel!!.readTodo(cateId, null)
//                    if(viewModel!!.inActiveTodoList!!.isNullOrEmpty() != true){
//                        holder.todoRv.isGone = true
//                        view!!.isGone = true
//
//                    }
//                    else {
//                        val mTodoAdapter = HomeTodoListAdapter()
//                        mTodoAdapter.viewModel = viewModel
//                        mTodoAdapter.category = getItem(position)
//                        viewModel!!.readTodo(cateId, mTodoAdapter)
//
//                        withContext(Dispatchers.Main){
//                            holder.todoRv.adapter = mTodoAdapter
//                            holder.todoRv.layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
//
//                        }
//                    }
//                }
                runBlocking {
                    viewModel!!.readTodo(cateId, null)
                    if(viewModel!!.inActiveTodoList!!.isNullOrEmpty() == true){
                        Log.d("inactive", cateId.toString())
                        view!!.isGone = true
                    }
                    else{
                        Log.d("inactive but have todo", cateId.toString())
                    }
                }

            }
            else {
                holder.btnAdd.isVisible = true
                holder.todoRv.isVisible = true

                val mTodoAdapter = HomeTodoListAdapter()
                mTodoAdapter.viewModel = viewModel
                mTodoAdapter.category = getItem(position)
                viewModel!!.readTodo(cateId, mTodoAdapter)


                    holder.todoRv.adapter = mTodoAdapter
                    holder.todoRv.layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)


            }



        holder.btnAdd.setOnClickListener {
            if(holder.layoutAdd.isGone == true){
                holder.layoutAdd.isVisible = true
            }
            else {
                holder.edtAdd.text.clear()
                holder.layoutAdd.isGone = true
            }
        }

        //edt 저장

        holder.edtAdd.setOnKeyListener { view, keyCode, event ->

            var handled = false
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                val context = MyApp.context()
                //키보드 내리기
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(holder.edtAdd.windowToken, 0)
                handled = true

                val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
                val todoData = PostRequestTodo(date = viewModel!!.homeDate.value.toString(), category = PostRequestTodoCateId(getItem(position).id!!), todoName = holder.edtAdd.text.toString(), complete = false, repeat = "N", repeatWeek = null, repeatMonth = null, startRepeatDate = null, endRepeatDate = null, isAlarm = false, startTodoAtMonday = viewModel!!.startMonday, endTodoBackSetting = viewModel!!.completeBottom, newTodoStartSetting = viewModel!!.newTodoTop)
                //서버 보내기ㅎㅎㅎㅎ
                CoroutineScope(Dispatchers.IO).launch {
                    api.addTodo(viewModel!!.userToken, todoData).enqueue(object : Callback<PostResponseTodo>{
                        override fun onResponse(
                            call: Call<PostResponseTodo>,
                            response: Response<PostResponseTodo>
                        ) {
                            if(response.isSuccessful){
                                //db에 저장
                                val Tresponse = response.body()!!.data.Todo
                                val data = TodoEntity(0, Tresponse.id, viewModel!!.homeDate.value.toString(), Tresponse.category.id, holder.edtAdd.text.toString(), false, "N", null, null, null, null, false, viewModel!!.startMonday, viewModel!!.completeBottom, viewModel!!.newTodoTop)
                                viewModel!!.createTodo(data, holder.edtAdd)

                            } else{
                                Log.d("todoAdd", "안드잘못")

                            }
                        }

                        override fun onFailure(call: Call<PostResponseTodo>, t: Throwable) {
                            Log.d("todoAdd", "서버 연결 실패")
                        }

                    })
                }

                holder.layoutAdd.isGone = true
                true
            }

            handled
            false
        }

    }

    inner class ViewHolder(private val binding : HomeCatagoryListBinding) : RecyclerView.ViewHolder(binding.root){
        var data : CateEntity? = null
        fun bind(cateEntity: CateEntity) : Int {
            binding.tvRepeatCategory.text = cateEntity.categoryName
            binding.tvRepeatCategory.text = cateEntity.categoryName
            binding.icRepeatCategory.setImageResource(findIcon(cateEntity.iconId))
            val mGradientDrawable : GradientDrawable = binding.layoutHomeViewpagerCateList.background as GradientDrawable
            mGradientDrawable.setStroke(6, Color.parseColor(cateEntity.color))
            data = cateEntity

            return cateEntity.id!!
        }
        val todoRv = binding.rvRepeatTodo
        val btnAdd = binding.btnRepeatAddTodo
        val layoutAdd = binding.layoutRepeatTodoAdd
        val edtAdd = binding.edtRepeatTodo
    }

    fun findIcon(iconId : Int) : Int {
        val icon = when(iconId){
            1 -> {
                R.drawable.ic_home_cate_burn}
            2 -> {
                R.drawable.ic_home_cate_chat1}
            3 -> {
                R.drawable.ic_home_cate_health}
            4 -> {
                R.drawable.ic_home_cate_heart}
            5 -> {
                R.drawable.ic_home_cate_laptop}
            6 -> {
                R.drawable.ic_home_cate_lightout}
            7 -> {
                R.drawable.ic_home_cate_lightup}
            8 -> {
                R.drawable.ic_home_cate_meal2}
            9 -> {
                R.drawable.ic_home_cate_meal1}
            10 -> {
                R.drawable.ic_home_cate_mic}
            11 -> {
                R.drawable.ic_home_cate_music}
            12 -> {
                R.drawable.ic_home_cate_pen}
            13 -> {
                R.drawable.ic_home_cate_phone}
            14 -> {
                R.drawable.ic_home_cate_plan}
            15 -> {
                R.drawable.ic_home_cate_rest}
            16 -> {
                R.drawable.ic_home_cate_sony}
            17 -> {
                R.drawable.ic_home_cate_study}
            else -> {
                R.drawable.ic_home_cate_work}
        }
        return icon
    }
}