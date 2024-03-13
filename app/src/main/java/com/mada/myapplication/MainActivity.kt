package com.mada.myapplication

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mada.myapplication.HomeFunction.Model.CategoryList1
import com.mada.myapplication.HomeFunction.Model.RepeatData1
import com.mada.myapplication.HomeFunction.Model.TodoList
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.StartFuction.Splash2Activity
import com.mada.myapplication.databinding.ActivityMainBinding
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.db.entity.RepeatEntity
import com.mada.myapplication.db.entity.TodoEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        /**
         * 1-1 database clear
         */
        //clearHomeDatabase(viewModel)

        /**
         * 1-2 캐릭터 서버에서 받아오기
         */


        /**
         * 2. GET home Category
         */
        //getHomeCategory(api, viewModel, this)


        /**
         * 3. GET home Todo
         */
        //getHomeTodo(api, viewModel, this)


    }



}



fun hideBottomNavigation(bool: Boolean, activity: Activity?) {
    val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    if (bool) {
        bottomNavigation?.isGone = true
    } else {
        bottomNavigation?.isVisible = true
    }
}

fun clearHomeDatabase(viewModel: HomeViewModel){
    viewModel.deleteAllCate()
    Log.d("MainActivity", "1-1-1 deleteAllCate")
    viewModel.deleteAllTodo()
    Log.d("MainActivity", "1-1-2 deleteAllTodo")
    viewModel.deleteAllRepeatTodo()
    Log.d("MainActivity", "1-1-3 deleteAllRepeatTodo")
}

fun getHomeCategory(api: HomeApi, viewModel : HomeViewModel, context: Context){
    Log.d("MainActivity", "2. getHomeCategoryStart")
    viewModel.deleteAllCate()
    api.getHCategory(viewModel.userToken, viewModel.homeDate.value.toString()).enqueue(object : Callback<CategoryList1> {
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
                    Log.d("MainActivity cate 추가중", cateData.categoryName.toString())
                    viewModel.createCate(cateData)
                }
                getHomeTodo(api, viewModel, context)
            } else {
                Log.d("MainActivity cate안드 잘못", "서버 연결 실패")
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<CategoryList1>, t: Throwable) {
            Log.d("MainActivity cate서버 연결 오류", "서버 연결 실패")
            Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    })
    Log.d("MainActivity", "2. getHomeCategoryFin")
}

fun getHomeTodo(api : HomeApi, viewModel: HomeViewModel, context: Context){
    Log.d("MainActivity", "3. GET homeTodoStart")
    api.getAllMyTodo(viewModel.userToken, viewModel.homeDate.value.toString()).enqueue(object : Callback<TodoList> {
        override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
            if(response.isSuccessful){
                for(i in response.body()!!.data.TodoList){
                    val todoData = TodoEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, complete = i.complete, repeat = i.repeat, repeatInfo = i.repeatInfo, endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate)
                    Log.d("MainActivity todo server", todoData.toString())
                    viewModel.createTodo(todoData, null)
                }
                for(i in response.body()!!.data.RepeatTodoList){
                    val repeatData = TodoEntity(id = i.todoId, repeatId = i.id, date = i.date, category = i.categoryId, todoName = i.repeatTodoName, complete = i.complete, repeat = "Y")
                    Log.d("MainActivity todo server", repeatData.toString())
                    viewModel.createTodo(repeatData, null)
                }
                //닉네임 저장하기
                viewModel._dUserName.value = response.body()!!.data.nickname
            }
            else {
                Log.d("todo안드 잘못", "서버 연결 실패")
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<TodoList>, t: Throwable) {
            Log.d("todo서버 연결 오류", "서버 연결 실패")
            Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    })
    Log.d("MainActivity", "3. GET homeTodoFin")
}

fun getAllCategory(api: HomeApi, viewModel : HomeViewModel, context: Context){
    Log.d("Category", "AllCategoryStart")
    viewModel.deleteAllCate()
    api.getCategory(viewModel.userToken).enqueue(object : Callback<CategoryList1> {
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
                    Log.d("Category cate 추가중", cateData.id.toString())
                    viewModel.createCate(cateData)
                }
            } else {
                Log.d("MainActivity cate안드 잘못", "서버 연결 실패")
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<CategoryList1>, t: Throwable) {
            Log.d("MainActivity cate서버 연결 오류", "서버 연결 실패")
            Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    })
    Log.d("Category", " AllCategoryFin")
}

fun getRepeatTodo(api : HomeApi, viewModel: HomeViewModel, context: Context){
    api.getHRepeatTodo(viewModel.userToken).enqueue(object :Callback<RepeatData1>{
        override fun onResponse(call: Call<RepeatData1>, response: Response<RepeatData1>) {
            if(response.isSuccessful){
                for(i in response.body()!!.data.RepeatTodoList){
                    var repeatData = RepeatEntity(id = i.id, date = i.date, category = i.category.id, todoName = i.todoName, repeat = i.repeat, repeatInfo = i.repeatInfo?.toInt(), endRepeatDate = i.endRepeatDate, startRepeatDate = i.startRepeatDate)
                    viewModel.createRepeatTodo(repeatData, null)
                    Log.d("GET repeatTodo", "success")
                }
            }
            else{
                Log.d("RepeatTodo GET", "android error")
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<RepeatData1>, t: Throwable) {
            Log.d("RepeatTodo GET", "server error")
            Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    })

}


