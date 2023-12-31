package com.example.myapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.HomeFunction.Model.CategoryList1
import com.example.myapplication.HomeFunction.Model.RepeatData1
import com.example.myapplication.HomeFunction.Model.TodoList
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setContentView(binding.root)

        viewModel.userToken = Splash2Activity.prefs.getString("token", "")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fl_con) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
/*
        CoroutineScope(Dispatchers.IO).launch {
            //데베 클리어
            CoroutineScope(Dispatchers.IO).async {
                viewModel.deleteAllCate()
                viewModel.deleteAllTodo()
                viewModel.deleteAllRepeatTodo()
            }.await()


            CoroutineScope(Dispatchers.IO).async {
                //서버에서 카테고리 데이터 받아서 로컬 데베에 저장
                api.getHCategory(viewModel.userToken).enqueue(object : Callback<CategoryList1> {
                    override fun onResponse(
                        call: Call<CategoryList1>,
                        response: Response<CategoryList1>
                    ) {
                        if (response.isSuccessful) {
                            for (i in response.body()!!.data.CategoryList) {
                                val cateData = CateEntity(
                                    id = i.id,
                                    categoryName = i.categoryName,
                                    color = i.color,
                                    iconId = i.iconId,
                                    isInActive = i.isInActive
                                )
                                Log.d("cate 추가중", cateData.id.toString())
                                viewModel.createCate(cateData)
                            }
                        } else {
                            Log.d("cate안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<CategoryList1>, t: Throwable) {
                        Log.d("cate서버 연결 오류", "서버 연결 실패")
                    }

                })
            }.await()

            CoroutineScope(Dispatchers.IO).async {
                viewModel.readAllTodo()
            }.await()

            CoroutineScope(Dispatchers.IO).async {
                //서버에서 투두 데이터 받아서 로컬 데베에 저장
                api.getAllMyTodo(viewModel.userToken, LocalDate.now().toString()).enqueue(object : Callback<TodoList> {
                    override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                        if(response.isSuccessful){
                            for(i in response.body()!!.data.TodoList){
                                val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate, isAlarm = i.isAlarm, startTodoAtMonday = i.startTodoAtMonday,  endTodoBackSetting = i.endTodoBackSetting, newTodoStartSetting = i.newTodoStartSetting )
                                Log.d("todo server", todoData.toString())
                                viewModel.createTodo(todoData, null)
                            }
                            //닉네임 저장하기
                            viewModel._dUserName.value = response.body()!!.data.nickname
                        }
                        else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<TodoList>, t: Throwable) {
                        Log.d("todo서버 연결 오류", "서버 연결 실패")
                    }

                })

                //서버에서 반복투두 데이터 받아서 로컬 데베에 저장
                api.getHRepeatTodo(viewModel.userToken).enqueue(object : Callback<RepeatData1>{
                    override fun onResponse(call: Call<RepeatData1>, response: Response<RepeatData1>) {
                        if(response.isSuccessful){
                            Log.d("Rtodo서버 성공", response.body()!!.data.RepeatTodoList.toString())
                            //db에 저장
                            for(i in response.body()!!.data.RepeatTodoList){
                                val todoData = RepeatEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate)
                                if(todoData.id == 348) {

                                }else {
                                    Log.d("Rtodo server", todoData.toString())
                                    viewModel.createRepeatTodo(todoData, null)
                                }
                            }

                        } else {
                            Log.d("Rtodo서버 실패", "안드 오류")

                        }
                    }

                    override fun onFailure(call: Call<RepeatData1>, t: Throwable) {
                        Log.d("Rtodo서버 연결 오류", "서버 연결 실패")
                    }

                })

            }
        }*/
        //서버에서 커스텀 받아서 저장?
    }



    }



        fun hideBottomNavigation(bool: Boolean, activity: Activity?) {
            val bottomNavigation =
                activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            if (bool) {
                bottomNavigation?.isGone = true
            } else {
                bottomNavigation?.isVisible = true
            }
        }






