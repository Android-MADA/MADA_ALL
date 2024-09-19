package com.mada.myapplication.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mada.myapplication.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.CustomFunction.ButtonInfo
import com.mada.myapplication.CustomFunction.ButtonInfoEntity
import com.mada.myapplication.CustomFunction.CustomViewModel
import com.mada.myapplication.CustomFunction.DataRepo
import com.mada.myapplication.CustomFunction.RetrofitServiceCustom
import com.mada.myapplication.CustomFunction.customPrintDATA
import com.mada.myapplication.HomeFunction.Model.Category
import com.mada.myapplication.HomeFunction.Model.CategoryList1
import com.mada.myapplication.HomeFunction.Model.Todo
import com.mada.myapplication.HomeFunction.Model.TodoList
import com.mada.myapplication.HomeFunction.adapter.todo.HomeCateListAdapter
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.bottomsheetdialog.TodoDateBottomSheetDialog
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.MainActivity
import com.mada.myapplication.MyFunction.Data.FragMyData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.clearHomeDatabase
import com.mada.myapplication.databinding.TodoLayoutBinding
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.getHomeTodo
import com.mada.myapplication.hideBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import kotlin.concurrent.thread


class FragHome : Fragment() {

    lateinit var binding : TodoLayoutBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    private val CustomviewModel: HomeViewModel by activityViewModels()
    private val CustomviewModel2: CustomViewModel by activityViewModels()

    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
    private val apiMy = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private lateinit var mAdView : AdView
    lateinit var savedData: FragCustom.selectedButtonInfo



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getCustomPrint(CustomviewModel)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodoLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        /**
         * 1. 바텀네비게이션 활성화
         */
        hideBottomNavigation(false, activity)


        //날짜 변경 시 서버에서 cateogry, todo받아오기

        //getCustomPrint(CustomviewModel)

        val buttonInfo = CustomviewModel2.getSavedSelectedButtonInfo(this@FragHome)

        binding.todoCharacterIv.setImageResource(
            buttonInfo?.selectedColorButtonInfo?.selectedImageResource ?: 0
        )
        binding.ivHomeCloth.setImageResource(
            buttonInfo?.selectedClothButtonInfo?.selectedImageResource ?: 0
        )
        binding.ivHomeItem.setImageResource(
            buttonInfo?.selectedItemButtonInfo?.selectedImageResource ?: 0
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*val buttonInfo = CustomviewModel2.getSavedSelectedButtonInfo(this@FragHome)

        binding.todoCharacterIv.setImageResource(
            buttonInfo?.selectedColorButtonInfo?.selectedImageResource ?: 0
        )
        binding.ivHomeCloth.setImageResource(
            buttonInfo?.selectedClothButtonInfo?.selectedImageResource ?: 0
        )
        binding.ivHomeItem.setImageResource(
            buttonInfo?.selectedItemButtonInfo?.selectedImageResource ?: 0
        )*/


        /**
         * 삭제 예정 코드 시작
         */

        viewModel.userToken = Splash2Activity.prefs.getString("token", "")
        Log.d("token", viewModel.userToken)

        /**
         * 삭제 예정 코드 끝
         */


        /**
         * 2. 날짜 세팅
         */

        binding.todoDateTv.text = todoDateSetting(viewModel)


        /**
         * 3. 요일별 응원멘트 설정
         */
        binding.todoMentTv.text = homeMent(viewModel.homeDay)


        /**
         * 4. 반복투두 페이지 이동
         */

        binding.todoRepeatIv.setOnClickListener {
            val buffering = CalendarViewModel.setPopupBuffering(requireContext())
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeRepeatTodoFragment)
            buffering.dismiss()
        }


        /**
         * 5. 마이페이지 페이지 이동
         */
        binding.todoMyIv.setOnClickListener {
            val buffering = CalendarViewModel.setPopupBuffering(requireContext())
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_fragMy)
            buffering.dismiss()
        }

        /**
         * 6. 카테고리 존재 여부 확인
         */
        checkCategory(viewModel)
        viewModel.homeCateEntityList?.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            if(cateList.isNullOrEmpty()){
                binding.cateEmptyLayout.isVisible = true
                binding.todoActiveCategoryRv.isGone = true
                binding.todoInactiveCategoryRv.isGone = true
            }
            else{
                binding.cateEmptyLayout.isGone = true
                binding.todoActiveCategoryRv.isVisible = true
                binding.todoInactiveCategoryRv.isVisible = true
            }

        })

        /**
         * 7. 활성 카테고리 rv 연결
         */
        viewModel.readActiveCate(false)
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            val mAdapter = HomeCateListAdapter(binding.todoActiveCategoryRv, requireFragmentManager())
            mAdapter.viewModel = viewModel
            mAdapter.context = context
            mAdapter.submitList(cateList)
            binding.todoActiveCategoryRv.adapter = mAdapter
            binding.todoActiveCategoryRv.layoutManager = LinearLayoutManager(this.requireActivity())

        })


        /**
         * 8. 종료 카테고리 rv 연결
         */
        viewModel.readQuitCate(true)
        viewModel.quitCateEntityList.observe(viewLifecycleOwner, Observer {
            val cateList = it as List<CateEntity>
            val mAdapter = HomeCateListAdapter(binding.todoInactiveCategoryRv, requireFragmentManager())
            mAdapter.viewModel = viewModel
            mAdapter.context = context
            mAdapter.submitList(cateList)
            binding.todoInactiveCategoryRv.adapter = mAdapter
            binding.todoInactiveCategoryRv.layoutManager = LinearLayoutManager(this.requireActivity())
        })



        /**
         * 9. 카테고리 페이지 이동
         */
        binding.categoryIv.setOnClickListener {
            val buffering = CalendarViewModel.setPopupBuffering(requireContext())
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeCategoryFragment)
            buffering.dismiss()
        }
        binding.todoNoCategoryBtn.setOnClickListener {
            val buffering = CalendarViewModel.setPopupBuffering(requireContext())
            Navigation.findNavController(view).navigate(R.id.action_fragHome_to_homeCategoryFragment)
            buffering.dismiss()
        }


        /**
         * 10. progressbar 설정
         */
        viewModel.readAllTodo()
        viewModel.todoEntityList.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                var completeNum = 0
                var todoNum = it.size
                for(i in it){
                    if(i.complete == true){
                        completeNum++
                    }
                }

                binding.progressLayout.weightSum = todoNum.toFloat()
                binding.progressDoneLayout.layoutParams.apply {
                    (this as LinearLayout.LayoutParams).weight = completeNum.toFloat()
                }
                binding.todoAllNumTv.text = "${todoNum.toString()}개"
                binding.todoDoneNumTv.text = "${completeNum.toString()}개"
                var percent = if(todoNum == 0 || completeNum == 0){
                    0
                }
                else{
                    (completeNum.toFloat() / todoNum.toFloat() * 100.0).toInt()
                }
                binding.progressTv.text = "${percent.toString()}%"
                if(todoNum == completeNum){
                    binding.todoProgressMent2Tv.text = "오늘의 투두를 모두 완료했네요!"
                }
                else{
                    binding.todoProgressMent2Tv.text = "투두 달성을 위해 노력해 봐요!"
                }
            }
        })

        /**
         * 11. 날짜 변경 시 bottomsheetdialog 처리
         */
        binding.todoDateLayout.setOnClickListener{
            val buffering = CalendarViewModel.setPopupBuffering(requireContext())
            val todoMenuBottomSheet = TodoDateBottomSheetDialog(viewModel)
            if (todoMenuBottomSheet != null) {
                viewModel.isTodoMenu = false
                todoMenuBottomSheet.show(childFragmentManager, todoMenuBottomSheet.tag)
            }
            buffering.dismiss()
        }
        viewModel.homeDate.observe(viewLifecycleOwner, Observer {
            Log.d("date변경", it.toString())
            binding.todoDateTv.text = todoDateSetting(viewModel)
            binding.todoMentTv.text = homeMent(viewModel.homeDay)
            //서버에서 데이터 새로 받아오기
            clearHomeDatabase(viewModel)
            viewModel.getHomeMyCategory(requireContext()){
                result ->
                when(result){
                    0 -> {
                        viewModel.getHomeAllTodo(requireContext()){
                            result ->
                            when(result){
                                0 -> {}
                                1 -> {
                                    Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    1 -> {
                        Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })



        /**
         * 12. 시스템 뒤로가기
          */

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                CalendarViewModel.setPopupTwo(requireContext(),"종료하시겠습니까?",requireView(),0)
                return@OnKeyListener true
            }
            false
        })

        /**
         * 구글 광고
         */
        //구글 플레이스토어 광고
        val mainActivity = requireActivity() as MainActivity
        if(mainActivity.getPremium()) {
        } else {
            MobileAds.initialize(this.requireContext()) {}
            val adRequest = AdRequest.Builder().build()
            binding.adViewTodo.loadAd(adRequest)
        }


    }

    override fun onResume() {
        super.onResume()

        hideBottomNavigation(false, activity)
        //getHomeCategory(api, viewModel, this.requireActivity())
    }

    private fun findDayOfWeek(y: Int, m: Int, d: Int, selected: Calendar): String {
        var dayOfWeek =
            when (selected.get(Calendar.DAY_OF_WEEK)) {
                1 -> "일요일"
                2 -> "월요일"
                3 -> "화요일"
                4 -> "수요일"
                5 -> "목요일"
                6 -> "금요일"
                else -> "토요일"
            }
        return dayOfWeek
    }

    fun todoDateSetting(viewModel: HomeViewModel) : String{
        //달력은 현재 날짜로 세팅
        var dateCalendar = Calendar.getInstance()
        dateCalendar.set(
            viewModel.homeDate.value!!.year,
            (viewModel.homeDate.value!!.monthValue - 1),
            viewModel.homeDate.value!!.dayOfMonth
        )
        var currentDay = findDayOfWeek(
            viewModel.homeDate.value!!.year,
            (viewModel.homeDate.value!!.monthValue - 1),
            viewModel.homeDate.value!!.dayOfMonth,
            dateCalendar
        )
        viewModel.homeDay = currentDay
        var dateString = "${viewModel.homeDate.value!!.monthValue}월 ${viewModel.homeDate.value!!.dayOfMonth}일 ${currentDay}"
        return dateString
    }

    private fun homeMent(day: String): String {
        Log.d("homeMent", "running")
        var homeMent = when (day) {
            "월요일" -> "월요병 날려버리고 화이팅!\n"
            "화요일" -> "화끈한 에너지로 화요일을 불태워보세요!\n"
            "수요일" -> "수투레스받을 땐 심호흡 한 번 해보세요!!\n"
            "목요일" -> "오늘도 열심히 달려 봐요\n"
            "금요일" -> "주말을 위해 조금만 더 화이팅!\n"
            "토요일" -> "주말을 알차게!\n"
            else -> "일주일의 마지막도 파이팅!\n"
        }
        return homeMent
    }

}

fun checkCategory(viewModel: HomeViewModel) : Boolean {
    var isCate = false
    viewModel.readHomeCate()
    if(viewModel.homeCateEntityList?.value.isNullOrEmpty()){
        Log.d("checkHomeCate", "blank")
    }
    else{
        Log.d("checkHomeCate", "not blank")
    }
    return isCate
}






val retrofit = Retrofit.Builder().baseUrl(BuildConfig.MADA_BASE)
    .addConverterFactory(GsonConverterFactory.create()).build()
val service = retrofit.create(RetrofitServiceCustom::class.java)

val token = Splash2Activity.prefs.getString("token", "")


fun getCustomPrint(viewModel: HomeViewModel) {
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
                Log.d(
                    "getCustomPrint",
                    "Response Code: $responseCode Item $index - id: ${item.id}"
                )
                DataRepo.updateButtonInfoEntity(viewModel, index, item.id)
            }
        }

        override fun onFailure(call: Call<customPrintDATA>, t: Throwable) {
            Log.d("error", t.message.toString())
        }
    })
}

