package com.mada.myapplication.HomeFunction.viewModel

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.mada.myapplication.HomeFunction.Model.Category
import com.mada.myapplication.HomeFunction.Model.PatchCheckboxTodo
import com.mada.myapplication.HomeFunction.Model.PatchRequestRepeatTodo
import com.mada.myapplication.HomeFunction.Model.PatchRequestTodo
import com.mada.myapplication.HomeFunction.Model.PatchResponseCategory
import com.mada.myapplication.HomeFunction.Model.PostRequestCategory
import com.mada.myapplication.HomeFunction.Model.Todo
import com.mada.myapplication.HomeFunction.adapter.repeatTodo.RepeatTodoListAdapter
import com.mada.myapplication.HomeFunction.adapter.todo.HomeTodoListAdapter
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.R
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.db.entity.RepeatEntity
import com.mada.myapplication.db.entity.TodoEntity
import com.mada.myapplication.db.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import com.mada.myapplication.StartFuction.Splash2Activity
import com.mada.myapplication.getHomeTodo
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {

    //서버 연결
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

    var userToken = Splash2Activity.prefs.getString("token", "")

    var userHomeName = "김마다"

    var isSubscribe = false


    var selectedRepeatTodo : RepeatEntity? = null


    //날짜
    //2023-08-13
    private val _homeDate = MutableLiveData<LocalDate>(LocalDate.now())
    val homeDate: LiveData<LocalDate>
        get() = _homeDate

    var homeDay = "월요일"

    fun changeDate(year : Int, month : Int, dayOfWeek : Int) : LocalDate {
        var monthString : String = month.toString()
        var dayString = dayOfWeek.toString()

        if(month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6 || month == 7 || month == 8 || month == 9){
            monthString = "0${month}"
        }
        if(dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 8 || dayOfWeek == 9){
            dayString = "0${dayOfWeek}"
        }

        return LocalDate.parse("${year}-${monthString}-${dayString}")
    }


    var repeatStartDate : LocalDate? = changeDate(homeDate.value!!.year, homeDate.value!!.monthValue, homeDate.value!!.dayOfMonth)
    var repeatEndDate : LocalDate? = changeDate((homeDate.value!!.year+1), homeDate.value!!.monthValue, homeDate.value!!.dayOfMonth)

    fun resetRepeatDate(){
        repeatStartDate = changeDate(homeDate.value!!.year, homeDate.value!!.monthValue, homeDate.value!!.dayOfMonth)
        repeatEndDate = changeDate((homeDate.value!!.year+1), homeDate.value!!.monthValue, homeDate.value!!.dayOfMonth)
    }

    //viewPager date 넘기기 확인 코드 시작

    var viewpagerDate: LocalDate? = LocalDate.now()

    //달력 날짜 라이브 데이터
    private var _myLiveToday = MutableLiveData<String>()
    val myLiveToday : LiveData<String>
        get() = _myLiveToday

    fun updateData(newValue: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        _myLiveToday.value = newValue
        Log.d("update",newValue)
        _homeDate.value = LocalDate.parse(newValue, DateTimeFormatter.ISO_DATE)
    }

    //date 변경
    fun changeDate(year: Int, month: Int, dayOfWeek: Int, flag: String?): String {
        var date = homeDate.value!!.toString()
        var monthString: String = month.toString()
        var dayString = dayOfWeek.toString()

        if (month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6 || month == 7 || month == 8 || month == 9) {
            monthString = "0${month}"
        }
        if (dayOfWeek == 1 || dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4 || dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 8 || dayOfWeek == 9) {
            dayString = "0${dayOfWeek}"
        }
        date = "${year}-${monthString}-${dayString}"

        if (flag == "home") {
            viewpagerDate = LocalDate.parse("${year}-${monthString}-${dayString}")
            _homeDate.value = viewpagerDate
        }

        return date
    }

    //calendarview 시작 요일
    private var _startDay = MutableLiveData<Int>(2)
    val startDay: LiveData<Int>
        get() = _startDay

    //calendarView 시작 요일 변경
    fun changeStartDay(flag: Boolean) {
        if (flag) {
            _startDay.value = 2
        } else {
            _startDay.value = 1
        }
    }

    //bottomsheet dialog
    private var _todoMenuMode = MutableLiveData<String>("show")

    val todoMenuMode : LiveData<String>
        get() = _todoMenuMode

    fun updateTodoMenuMode(mode : String){
        _todoMenuMode.value = mode
    }

    var isTodoMenu = false

    var selectedChangedDate = homeDate.value.toString()


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //ROOM

    //repository 선언
    private val repository = HomeRepository()

    //카테고리 리스트
    var homeCateEntityList : LiveData<List<CateEntity>>? = null
    lateinit var cateEntityList : LiveData<List<CateEntity>>
    lateinit var quitCateEntityList : LiveData<List<CateEntity>>

    //특정 카테고리 1개 저장(수정, 삭제 시)
    var _cate = MutableLiveData<CateEntity>(null)
    val cate : LiveData<CateEntity>
        get() = _cate

    lateinit var todoEntityList : LiveData<List<TodoEntity>>

    var _dUserName = MutableLiveData<String>("김마다")

    val dUserName : LiveData<String>
        get() = _dUserName

    val isAlarm = false
    val startMonday = false
    val completeBottom = false
    val newTodoTop = false



    //CRUD 선언
    fun createCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.createCate(cateEntity)
    }

    fun readHomeCate(){
        homeCateEntityList = repository.readHomeCate().asLiveData()
        Log.d("check readHomecate", "working")
    }

    fun readActiveCate(isInActive : Boolean) {
        cateEntityList = repository.readActiveCate(isInActive).asLiveData()
        Log.d("readActivecate", "working")
    }

    fun readQuitCate(isInActive : Boolean) {
        quitCateEntityList = repository.readQuitCate(isInActive).asLiveData()
        Log.d("readQuitcate", "working")
    }

    fun updateCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCate(cateEntity)
    }

    fun deleteCate(cateEntity: CateEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCate(cateEntity)
    }

    fun deleteAllCate() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllCate()
    }

    //TODO

    fun createTodo(todoEntity: TodoEntity, edt : EditText?) = viewModelScope.launch(Dispatchers.IO) {
        var a = false
        for(i in homeCateEntityList!!.value!!){
            if(todoEntity.category == i.id ){
                a = true
            }
        }
        if(a){
            repository.createTodo(todoEntity)
            if(edt != null){
                edt!!.text.clear()
            }
            Log.d("todo 추가중", todoEntity.toString())
        }

    }

    var inActiveTodoList : List<TodoEntity>? = null

    fun readTodo(cateId : Int, adapter: HomeTodoListAdapter?) = viewModelScope.launch(Dispatchers.IO){
        repository.readTodo(cateId).collect{
            Log.d("todoList 확인", it.toString())

            inActiveTodoList = it
            if(inActiveTodoList.isNullOrEmpty()){
                Log.d("empty list", "empty todo list")
            }
            withContext(Dispatchers.Main){
                if(adapter != null){
                    adapter.submitList(it)
                }
            }
        }

    }

    fun updateTodo(todoEntity: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodo(todoEntity)
    }

    fun deleteTodo(todoEntity: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodo(todoEntity)
    }

    fun deleteTodoCate(cateId : Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoCate(cateId)
    }

    fun deleteAllTodo() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllTodo()
    }

    fun readAllTodo() {
        todoEntityList = repository.readAllTodo().asLiveData()
    }

    //repeatTodo

    fun createRepeatTodo(repeatTodoEntity: RepeatEntity, edt : EditText? = null) = viewModelScope.launch(Dispatchers.IO) {
        repository.createRepeatTodo(repeatTodoEntity)
        if(edt != null) {
            edt!!.text.clear()
        }
        //readAllTodo()
    }

    fun readRepeatTodo(cateId : Int, adapter: RepeatTodoListAdapter) = viewModelScope.launch(Dispatchers.IO){
        repository.readRepeatTodo(cateId).collect{
            Log.d("RtodoList 확인", it.toString())
            withContext(Dispatchers.Main){
                adapter.submitList(it)
            }
        }

    }

    fun updateRepeatTodo(repeatTodoEntity: RepeatEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateRepeatTodo(repeatTodoEntity)
    }

    fun deleteRepeatTodo(repeatTodoEntity: RepeatEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRepeatTodo(repeatTodoEntity)
    }

    fun deleteRepeatTodoCate(cateId : Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRepeatTodoCate(cateId)
    }

    fun deleteAllRepeatTodo() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllRepeatTodo()
    }

    fun readAllRepeatTodo() {
        //todoEntityList = repository.readAllRepeatTodo().asLiveData()
    }

    fun deleteRepeatTodoOne(repeatTodoId : Int, callback: (Int) -> Unit){
        api.deleteRepeatTodoOne(userToken, repeatTodoId = repeatTodoId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("viewmodel", "Successful response: ${response}")
                    callback(0)
                }else{
                    Log.e("viewmodel", "Unsuccessful response: ${response}")
                    callback(1)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("viewmodel", "Failed to make the request", t)
                callback(1)
            }

        })
    }

    fun deleteRepeatTodoAfter(repeatTodoId: Int, callback: (Int) -> Unit){
        api.deleteRepeatTodoAfter(userToken, repeatTodoId = repeatTodoId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("viewmodel", "Successful response: ${response}")
                    callback(0)
                }else{
                    Log.e("viewmodel", "Unsuccessful response: ${response}")
                    callback(1)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("viewmodel", "Failed to make the request", t)
                callback(1)
            }

        })
    }

    fun deleteRepeatTodoAll(repeatTodoId: Int, callback: (Int) -> Unit){
        api.deleteRepeatTodoAll(userToken, repeatTodoId = repeatTodoId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("viewmodel", "Successful response: ${response}")
                    callback(0)
                }else{
                    Log.e("viewmodel", "Unsuccessful response: ${response}")
                    callback(1)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("viewmodel", "Failed to make the request", t)
                callback(1)
            }

        })
    }

    fun addCategoryAPI(data : PostRequestCategory, cateName : String, cateColor : String, cateIconId : Int, callback: (Int) -> Unit){
        api.postHCategory(userToken, data).enqueue(object : Callback<PatchResponseCategory>{
            override fun onResponse(
                call: Call<PatchResponseCategory>,
                response: Response<PatchResponseCategory>
            ) {
                if(response.isSuccessful){
                    Log.d("viewmodel", "Successful response: ${response}")
                    createCate(CateEntity(id = response.body()!!.data.Category.id, categoryName = cateName, color = cateColor, isInActive = false, iconId = cateIconId))

                    callback(0)
                }else{
                    Log.e("viewmodel", "Unsuccessful response: ${response}")
                    callback(1)
                }
            }

            override fun onFailure(call: Call<PatchResponseCategory>, t: Throwable) {
                Log.e("viewmodel", "Failed to make the request", t)
                callback(1)
            }

        })
    }

    fun editCategoryAPI(category : PostRequestCategory, cateDB : CateEntity, categoryId: Int, callback: (Int) -> Unit){
        api.editHCategory(userToken, categoryId = categoryId, data = category).enqueue(object : Callback<PatchResponseCategory>{
            override fun onResponse(
                call: Call<PatchResponseCategory>,
                response: Response<PatchResponseCategory>
            ) {
                if(response.isSuccessful){
                    Log.d("viewmodel", "Successful response: ${response}")
                    updateCate(cateDB)
                    readActiveCate(false)
                    readQuitCate(true)
                    callback(0)
                }else{
                    Log.e("viewmodel", "Unsuccessful response: ${response}")
                    callback(1)
                }
            }

            override fun onFailure(call: Call<PatchResponseCategory>, t: Throwable) {
                Log.e("viewmodel", "Failed to make the request", t)
                callback(1)
            }

        })
    }

    /////////////////////////////////////////////////////////////////////
    // server 연결

    //투두 수정
    fun patchHTodo(todoId : Int, data : PatchRequestTodo) = viewModelScope.launch {
        api.editTodo(userToken, todoId, data)
    }

    // 서버 연결 테스트 코드

    var categoryListHome = mutableListOf<Category>()
    var todoListHome = mutableListOf<Todo>()


    //////
    /**
     * 다이얼로그 - repeat delete
     */
    private fun setPopupTwo2(theContext: Context,title : String, flag : String?, callback: (Int) -> Unit) {
        val mDialogView = LayoutInflater.from(theContext).inflate(R.layout.calendar_add_popup, null)
        val mBuilder = AlertDialog.Builder(theContext)
            .setView(mDialogView)
            .create()

        mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mBuilder.show()

        //팝업 사이즈 조절
        DisplayMetrics()
        theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        //팝업 타이틀 설정, 버튼 작용 시스템
        if(flag == "delete"){
            mDialogView.findViewById<TextView>(R.id.textDescribe).visibility = View.VISIBLE
            mDialogView.findViewById<TextView>(R.id.textDescribe).text = "과거의 카테고리 속 투두도 함께 삭제되며\n삭제된 카테고리와 투두는 복구할 수 없습니다."

        }
        else if(flag == "quit"){
            mDialogView.findViewById<TextView>(R.id.textDescribe).visibility = View.VISIBLE
            mDialogView.findViewById<TextView>(R.id.textDescribe).text = "종료된 카테고리에는 더 이상 투두를 추가할 수 없으며\n과거의 투두 기록은 삭제되지 않습니다."

        }
        else{
            mDialogView.findViewById<TextView>(R.id.textDescribe).visibility = View.GONE
        }

        mDialogView.findViewById<TextView>(R.id.textTitle).text = title

        mDialogView.findViewById<ImageButton>(R.id.nobutton).setOnClickListener {
            callback(1)
            mBuilder.dismiss()
        }
        mDialogView.findViewById<ImageButton>(R.id.yesbutton).setOnClickListener {
            callback(0)
            mBuilder.dismiss()
        }

    }

    @SuppressLint("MissingInflatedId")
    fun setPopupDelete(theContext: Context, todo : TodoEntity) {
        val mDialogView = LayoutInflater.from(theContext).inflate(R.layout.repeat_delete_dialog, null)
        val mBuilder = AlertDialog.Builder(theContext)
            .setView(mDialogView)
            .create()
        val repeatId : Int = todo.repeatId!!

        mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mBuilder.show()

        //팝업 사이즈 조절
        DisplayMetrics()
        theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (theContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        var deleteFlag = "one"

        /**
         * 레이아웃 설정
         */
        val dialogOne = mDialogView.findViewById<RadioButton>(R.id.radio_repeat_delete_one)
        val dialogAfter = mDialogView.findViewById<RadioButton>(R.id.radio_repeat_delete_after)
        val dialogAll = mDialogView.findViewById<RadioButton>(R.id.radio_repeat_delete_all)
        val layoutOne = mDialogView.findViewById<LinearLayout>(R.id.layout_delete_one)
        val layoutAfter = mDialogView.findViewById<LinearLayout>(R.id.layout_delete_after)
        val layoutAll = mDialogView.findViewById<LinearLayout>(R.id.layout_delete_all)

        var selectedFlag = "one"

        dialogOne.isChecked = true
        dialogAfter.isChecked = false
        dialogAll.isChecked = false

        dialogOne.setOnClickListener {
            dialogOne.isChecked = true
            dialogAll.isChecked = false
            dialogAfter.isChecked = false
            selectedFlag = "one"
        }

        layoutOne.setOnClickListener {
            dialogOne.isChecked = true
            dialogAll.isChecked = false
            dialogAfter.isChecked = false
            selectedFlag = "one"
        }

        dialogAfter.setOnClickListener {
            dialogAfter.isChecked = true
            dialogAll.isChecked = false
            dialogOne.isChecked = false
            selectedFlag = "after"
        }

        layoutAfter.setOnClickListener {
            dialogAfter.isChecked = true
            dialogAll.isChecked = false
            dialogOne.isChecked = false
            selectedFlag = "after"
        }

        dialogAll.setOnClickListener {
            dialogAll.isChecked = true
            dialogOne.isChecked = false
            dialogAfter.isChecked = false
            selectedFlag = "all"
        }

        layoutAll.setOnClickListener {
            dialogAll.isChecked = true
            dialogOne.isChecked = false
            dialogAfter.isChecked = false
            selectedFlag = "all"
        }


        mDialogView.findViewById<TextView>(R.id.btn_repeat_delete_cancel).setOnClickListener {
            mBuilder.dismiss()
        }

        mDialogView.findViewById<TextView>(R.id.btn_repeat_delete_delete).setOnClickListener {
            setPopupTwo2(theContext, "정말 삭제하시겠습니까?",flag = "deleteRepeat"){
                    result ->
                when(result){
                    0 -> {
                        //삭제
                        when(selectedFlag){
                            "one" -> {
                                deleteRepeatTodoOne(repeatId){
                                        result ->
                                    when(result){
                                        0 -> {
                                            deleteTodo(todo)
                                            mBuilder.dismiss()
                                            Toast.makeText(theContext, "one 삭제 성공", Toast.LENGTH_SHORT).show()
                                        }
                                        1 -> {Toast.makeText(theContext, "one 삭제 실패", Toast.LENGTH_SHORT).show()}
                                    }
                                }
                            }
                            "after" ->{
                                deleteRepeatTodoAfter(repeatId){
                                        result ->
                                    when(result){
                                        0 ->{
                                            deleteTodo(todo)
                                            mBuilder.dismiss()
                                            Toast.makeText(theContext, "after 삭제 성공", Toast.LENGTH_SHORT).show()}
                                        1 -> {Toast.makeText(theContext, "after 삭제 실패", Toast.LENGTH_SHORT).show()}
                                    }
                                }
                            }
                            "all" ->{
                                deleteRepeatTodoAll(repeatId){
                                        result ->
                                    when(result){
                                        0 -> {
                                            deleteTodo(todo)
                                            mBuilder.dismiss()
                                            Toast.makeText(theContext, "all 삭제 성공", Toast.LENGTH_SHORT).show()
                                        }
                                        1 -> {
                                            Toast.makeText(theContext, "all 삭제 실패", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        Log.d("repeatTodo delete", "fail")
                    }
                }
            }

        }




    }

    fun changeRepeatCb(todoEntity: TodoEntity, isChecked : Boolean, callback: (Int) -> Unit){
        api.changeRepeatCheckox(userToken, todoEntity.repeatId!!, PatchCheckboxTodo(isChecked)).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("repeat patch", "success")
                    updateTodo(todoEntity)
                    callback(0)
                }
                else{
                    Log.d("android fail", "fail")
                    callback(1)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("server fail", "fail")
                callback(1)
            }

        })
    }

    fun editRepeatTodo(todoName : String, repeat : String, repeatInfo : Int?, endDate : String, startDate : String, callback: (Int) -> Unit){
        var data = PatchRequestRepeatTodo(todoName = todoName, repeat = repeat, repeatInfo = repeatInfo, endRepeatDate = endDate, startRepeatDate = startDate)
        Log.d("check", data.toString() + selectedRepeatTodo!!.id!! )
        callback(1)
//        api.editRepeatTodo(userToken, selectedRepeatTodo!!.id!!, data).enqueue(object : Callback<Void>{
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if(response.isSuccessful){
//                    Log.d("repeat Edit", "success")
//                    callback(0)
//                }
//                else{
//                    Log.d("repeat Edit", "android fail")
//                    callback(1)
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Log.d("repeat Edit", "fail")
//                callback(1)
//            }
//
//        })
    }
}