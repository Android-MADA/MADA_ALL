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
import com.example.myapplication.CustomFunction.ButtonDatabase
import com.example.myapplication.CustomFunction.ButtonInfo
import com.example.myapplication.CustomFunction.ButtonInfoEntity
import com.example.myapplication.CustomFunction.CustomViewModel
import com.example.myapplication.CustomFunction.DataRepo
import com.example.myapplication.CustomFunction.RetrofitServiceCustom
import com.example.myapplication.CustomFunction.customPrintDATA
import com.example.myapplication.Fragment.FragCalendar
import com.example.myapplication.Fragment.FragCustom
import com.example.myapplication.Fragment.FragDaily
import com.example.myapplication.Fragment.FragHome
import com.example.myapplication.Fragment.FragMy
import com.example.myapplication.Fragment.IdAndItemType
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

        CoroutineScope(Dispatchers.IO).launch {
            //데베 클리어
            viewModel.deleteAllCate()
            viewModel.deleteAllTodo()
            viewModel.deleteAllRepeatTodo()

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

            //서버에서 투두 데이터 받아서 로컬 데베에 저장
            api.getAllMyTodo(viewModel.userToken, LocalDate.now().toString())
                .enqueue(object : Callback<TodoList> {
                    override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                        if (response.isSuccessful) {
                            for (i in response.body()!!.data.TodoList) {
                                val todoData = TodoEntity(
                                    id = i.id,
                                    date = i.date,
                                    category = i.category.id,
                                    todoName = i.todoName,
                                    complete = i.complete,
                                    repeat = i.repeat,
                                    repeatWeek = i.repeatWeek,
                                    repeatMonth = i.repeatMonth,
                                    endRepeatDate = i.endRepeatDate,
                                    startRepeatDate = i.startRepeatDate,
                                    isAlarm = i.isAlarm,
                                    startTodoAtMonday = i.startTodoAtMonday,
                                    endTodoBackSetting = i.endTodoBackSetting,
                                    newTodoStartSetting = i.newTodoStartSetting
                                )
                                viewModel.createTodo(todoData, null)
                            }
                            //닉네임 저장하기
                            viewModel.userHomeName = response.body()!!.data.nickname
                        } else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<TodoList>, t: Throwable) {
                        Log.d("todo서버 연결 오류", "서버 연결 실패")
                    }

                })

            //서버에서 반복투두 데이터 받아서 로컬 데베에 저장
            api.getHRepeatTodo(viewModel.userToken).enqueue(object : Callback<RepeatData1> {
                override fun onResponse(call: Call<RepeatData1>, response: Response<RepeatData1>) {
                    if (response.isSuccessful) {
                        for (i in response.body()!!.data.RepeatTodoList) {
                            val RTodoData = RepeatEntity(
                                id = i.id,
                                date = i.date,
                                category = i.category.id,
                                todoName = i.todoName,
                                repeat = i.repeat,
                                repeatWeek = i.repeatWeek,
                                repeatMonth = i.repeatMonth,
                                startRepeatDate = i.startRepeatDate,
                                endRepeatDate = i.endRepeatDate
                            )
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

        val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(RetrofitServiceCustom::class.java)
        val token = Splash2Activity.prefs.getString("token", "")

        val appDatabase =
            ButtonDatabase.getInstance(applicationContext) // AppDatabase는 Room 데이터베이스 클래스
        val buttonInfoDao = appDatabase.buttonInfoDao()

        val printIds = mutableListOf<IdAndItemType>()
        var printId: Int = 0
        var itemType: String = "z"

        val call: Call<customPrintDATA> = service.customPrint(token)
        call.enqueue(object : Callback<customPrintDATA> {
            override fun onResponse(
                call: Call<customPrintDATA>,
                response: Response<customPrintDATA>
            ) {
                val printInfo = response.body()
                val responseCode = response.code()
                val datas = printInfo?.data?.wearingItems

                datas?.forEachIndexed { index, item ->
                    printId = item.id
                    itemType = item.itemType
                    Log.d(
                        "getCustomPrint",
                        "Item $index - id: ${item.id} itemType: ${item.itemType}"
                    )
                    Log.d("getCustomPrint", "Response Code: $responseCode")
                }

                datas?.forEachIndexed { index, item ->
                    val idAndItemType = IdAndItemType(item.id, item.itemType)
                    printIds.add(idAndItemType)
                }

                var colorid: Int? = null // 변수를 초기화

                var clothid: Int? = null // 변수를 초기화

                var itemid: Int? = null // 변수를 초기화

                var backgroundid: Int? = null // 변수를 초기화

                datas?.forEach { item ->
                    if (item.itemType == "color") {
                        colorid = item.id
                    } else if (item.itemType == "set") {
                        clothid = item.id
                    } else if (item.itemType == "item") {
                        itemid = item.id
                    } else if (item.itemType == "background") {
                        backgroundid = item.id
                    }

                    var buttonInfoEntity = ButtonInfoEntity(
                        id = 0,
                        colorButtonInfo = ButtonInfo(
                            buttonId = colorid ?: 0, // 기본값을 설정할 수 있음
                            serverID = colorid ?: 10, // 기본값을 설정할 수 있음
                            selectedImageResource = R.drawable.c_ramdi
                        ),
                        clothButtonInfo = ButtonInfo(
                            buttonId = clothid ?: 0, // 기본값을 설정할 수 있음
                            serverID = clothid ?: 900, // 기본값을 설정할 수 있음
                            selectedImageResource = R.drawable.custom_empty
                        ),
                        itemButtonInfo = ButtonInfo(
                            buttonId = itemid ?: 0, // 기본값을 설정할 수 있음
                            serverID = itemid ?: 800, // 기본값을 설정할 수 있음
                            selectedImageResource = R.drawable.custom_empty
                        ),
                        backgroundButtonInfo = ButtonInfo(
                            buttonId = backgroundid ?: 0, // 기본값을 설정할 수 있음
                            serverID = backgroundid ?: 700, // 기본값을 설정할 수 있음
                            selectedImageResource = R.drawable.custom_empty
                        )
                    )
                    DataRepo.buttonInfoEntity = buttonInfoEntity
                    CoroutineScope(Dispatchers.IO).launch {
                        buttonInfoDao.insertButtonInfo(buttonInfoEntity)
                    }

                    // 데이터베이스에 추가
                    Log.d(
                        "getCustomPrint",
                        "colorButtonInfo: ${buttonInfoEntity.colorButtonInfo.serverID} clothButtonInfo: ${buttonInfoEntity.clothButtonInfo.serverID} itmeButtonInfo: ${buttonInfoEntity.itemButtonInfo.serverID} backgroundButtonInfo: ${buttonInfoEntity.backgroundButtonInfo.serverID}"
                    )
                    Log.d("getCustomPrint", "Response Code: $responseCode")
                }


                datas?.forEachIndexed { index, item ->
                    val idAndItemType = IdAndItemType(item.id, item.itemType)
                    printIds.add(idAndItemType)
                }
            }

            override fun onFailure(call: Call<customPrintDATA>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
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






