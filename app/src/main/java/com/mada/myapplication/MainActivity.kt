package com.mada.myapplication

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import com.mada.myapplication.HomeFunction.Model.CategoryList1
import com.mada.myapplication.HomeFunction.Model.RepeatData1
import com.mada.myapplication.HomeFunction.Model.TodoList
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.ActivityMainBinding
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.db.entity.RepeatEntity
import com.mada.myapplication.db.entity.TodoEntity
import kotlinx.coroutines.launch
import com.mada.myapplication.MyFunction.Data.FragMyData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: HomeViewModel

    private lateinit var billingClient : BillingClient
    private lateinit var sharedPreferences: SharedPreferences
    private var premium = false

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

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item->
            item.onNavDestinationSelected(navController)
            true
        }

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)


        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {

                    val queryProductDetailsParams =
                        QueryProductDetailsParams.newBuilder()
                            .setProductList(
                                ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("premium_1200")
                                        .setProductType(BillingClient.ProductType.SUBS)
                                        .build()))
                            .build()
                    billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                            billingResult,
                            productDetailsList ->
                        val productDetailsParamsList = productDetailsList.map { productDetails ->
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(
                                    productDetails.subscriptionOfferDetails?.get(0)?.offerToken
                                        ?: ""
                                )
                                .build()
                        }
                    }
                    lifecycleScope.launch {
                        queryPurchaseAsync()
                    }

                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to

                // Google Play by calling the startConnection() method.
            }
            suspend fun queryPurchaseAsync() {

                val params = QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()

                // uses queryPurchasesAsync Kotlin extension function
                billingClient.queryPurchasesAsync(params) { purchasesResult, purchasesList ->
                    if (purchasesResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // Process the list of purchase
                        if(purchasesList.isEmpty()) {
                            // 구독 안한 상태
                            premium = false
                        }
                        for (purchase in purchasesList) {
                            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                // 구독중
                                premium = true
                            }
                        }
                    }
                }
            }
        })

    }
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    Log.d("안녕하세요","구매")
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Log.d("안녕하세요","상품 주문 취소")
            } else {
                // Handle any other error codes.
                Log.d("안녕하세요","구매요청 실패")
            }
        }

    fun getPremium() : Boolean {
        return premium
    }
    fun setPremium() {
        premium = true
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
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
                    Log.d("MainActivity repeat server", repeatData.toString())
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
                    Log.d("GET repeatTodo", repeatData.toString())
                    viewModel.createRepeatTodo(repeatData, null)
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

fun getDescribe(viewModel : HomeViewModel, context : Context){
    RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java).selectfragMy(viewModel.userToken).enqueue(object : Callback<FragMyData>{
        override fun onResponse(call: Call<FragMyData>, response: Response<FragMyData>) {
            if(response.isSuccessful){
                viewModel.isSubscribe = response.body()!!.data.subscribe
            }
            else{
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<FragMyData>, t: Throwable) {
            Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    })
}


