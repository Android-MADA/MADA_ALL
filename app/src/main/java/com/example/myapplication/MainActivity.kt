package com.example.myapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.Fragment.FragCustom
import com.example.myapplication.Fragment.FragDaily
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.Fragment.FragMy
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setContentView(binding.root)

        viewModel.userToken = Splash2Activity.prefs.getString("token", "")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fl_con) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //데베 클리어
            viewModel.deleteAllCate()
            viewModel.deleteAllTodo()
            viewModel.deleteAllRepeatTodo()

            //서버에서 카테고리 데이터 받아서 로컬 데베에 저장
            api.getHCategory(viewModel.userToken).enqueue(object : Callback<CategoryList1>{
                override fun onResponse(
                    call: Call<CategoryList1>,
                    response: Response<CategoryList1>
                ) {
                    if(response.isSuccessful){
                            for(i in response.body()!!.data.CategoryList) {
                                val cateData = CateEntity(
                                    id = i.id,
                                    categoryName = i.categoryName,
                                    color = i.color,
                                    iconId = i.iconId,
                                    isActive = i.isActive
                                )
                                viewModel.createCate(cateData)
                            }
                    }
                    else {
                        Log.d("cate안드 잘못", "서버 연결 실패")
                    }
                }

                override fun onFailure(call: Call<CategoryList1>, t: Throwable) {
                    Log.d("cate서버 연결 오류", "서버 연결 실패")
                }

            })

            //서버에서 투두 데이터 받아서 로컬 데베에 저장
            api.getAllMyTodo(viewModel.userToken, LocalDate.now().toString()).enqueue(object : Callback<TodoList> {
                override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                    if(response.isSuccessful){
                            for(i in response.body()!!.data.TodoList){
                                val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate, isAlarm = i.isAlarm, startTodoAtMonday = i.startTodoAtMonday,  endTodoBackSetting = i.endTodoBackSetting, newTodoStartSetting = i.newTodoStartSetting )
                                viewModel.createTodo(todoData, null)
                            }
                        //닉네임 저장하기
                        viewModel.userHomeName = response.body()!!.data.nickname
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
                        for(i in response.body()!!.data.RepeatTodoList){
                            val RTodoData = RepeatEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, repeat = i.repeat, repeatWeek = i.repeatWeek, repeatMonth = i.repeatMonth, startRepeatDate = i.startRepeatDate, endRepeatDate = i.endRepeatDate)
                            viewModel.createRepeatTodo(RTodoData, null)
                        }
                    } else {
                        Log.d("repeattodo안드 잘못", "서버 연결 실패")
                    }
                }

                override fun onFailure(call: Call<RepeatData1>, t: Throwable) {
                    Log.d("repeattodo서버 연결 오류", "서버 연결 실패")
                }

            })

        }
        //서버에서 커스텀 받아서 저장?
    }
}

fun hideBottomNavigation(bool : Boolean, activity: Activity?){
    val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    if(bool){
        bottomNavigation?.isGone = true
    }
    else {
        bottomNavigation?.isVisible = true
    }
}
