package com.example.myapplication.HomeFunction.adapter.repeatTodo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.Model.CategoryList1
import com.example.myapplication.HomeFunction.Model.PostRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestTodoCateId
import com.example.myapplication.HomeFunction.Model.PostResponseTodo
import com.example.myapplication.HomeFunction.Model.RepeatData1
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeCatagoryListBinding
import com.example.myapplication.db.MyApp
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class RepeatCateListAdapter(private val view : View, fragmentManager : FragmentManager, context : Context) : ListAdapter<CateEntity, RepeatCateListAdapter.ViewHolder>(DiffCallback) {

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
    private var repeatFragmentManager = fragmentManager
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeCatagoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)


        Log.d("HomeCateItem", getItem(position).toString())
        val cateId = holder.bind(getItem(position))

        CoroutineScope(Dispatchers.IO).launch {
            //todoadapter 연결하기
            val mTodoAdapter = RepeatTodoListAdapter(view, repeatFragmentManager, mContext)
            mTodoAdapter.viewModel = viewModel
            // 반복투두 읽어오기
            viewModel!!.readRepeatTodo(cateId, mTodoAdapter)
            withContext(Dispatchers.Main){
                holder.todoRv.adapter = mTodoAdapter
                holder.todoRv.layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
            }
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
            // Enter Key Action
            var handled = false
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                val context = MyApp.context()
                //키보드 내리기
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(holder.edtAdd.windowToken, 0)
                handled = true

                var endDate = viewModel!!.changeDate((viewModel!!.homeDate.value!!.year + 1), viewModel!!.homeDate.value!!.monthValue, viewModel!!.homeDate.value!!.dayOfMonth, null)
                //서버 post
                //response로 id 값 넣기
                val addData = PostRequestTodo(date = null, todoName = holder.edtAdd.text.toString(), category = PostRequestTodoCateId(holder.data!!.id), complete = false, repeat = "DAY", repeatWeek = null, repeatMonth = null, startRepeatDate = viewModel!!.homeDate.value.toString(), endRepeatDate = endDate, isAlarm = viewModel!!.isAlarm, startTodoAtMonday = viewModel!!.startMonday, endTodoBackSetting = viewModel!!.completeBottom, newTodoStartSetting = viewModel!!.newTodoTop )
                CoroutineScope(Dispatchers.IO).launch {
                    api.addTodo(viewModel!!.userToken, addData).enqueue(object : Callback<PostResponseTodo>{
                        override fun onResponse(
                            call: Call<PostResponseTodo>,
                            response: Response<PostResponseTodo>
                        ) {
                            if(response.isSuccessful){
                                //반복투두 db에 추가
                                val Rresponse = response.body()!!.data.Todo
                                val data = RepeatEntity(0, id = Rresponse.id, date = null, category = holder.data!!.id, todoName = holder.edtAdd.text.toString(),  repeat = "DAY", repeatWeek = null, repeatMonth = null, startRepeatDate = viewModel!!.homeDate.value.toString(), endRepeatDate = endDate)
                                viewModel!!.createRepeatTodo(data, holder.edtAdd)
                                //투두 db 모두 삭제
                                viewModel!!.deleteAllTodo()
                                //투두 새로 서버 에서 읽어오기
                                api.getAllMyTodo(viewModel!!.userToken, LocalDate.now().toString()).enqueue(object : Callback<TodoList> {
                                    override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                                        if(response.isSuccessful){
                                            for(i in response.body()!!.data.TodoList){
                                                val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate, isAlarm = i.isAlarm, startTodoAtMonday = i.startTodoAtMonday,  endTodoBackSetting = i.endTodoBackSetting, newTodoStartSetting = i.newTodoStartSetting )
                                                Log.d("todo server", todoData.toString())
                                                viewModel!!.createTodo(todoData, null)
                                            }
                                            //닉네임 저장하기
                                        }
                                        else {
                                            Log.d("todo안드 잘못", "서버 연결 실패")
                                        }
                                    }

                                    override fun onFailure(call: Call<TodoList>, t: Throwable) {
                                        Log.d("todo서버 연결 오류", "서버 연결 실패")
                                    }

                                })


                            }
                            else {
                                Log.d("addRTodo", "And fail")

                            }
                        }

                        override fun onFailure(call: Call<PostResponseTodo>, t: Throwable) {
                            Log.d("addRTodo", "server fail")
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
            binding.todoCategoryTitleTv.text = cateEntity.categoryName
            binding.todoCategoryIconIv.setImageResource(findIcon(cateEntity.iconId))
            data = cateEntity
            return cateEntity.id
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