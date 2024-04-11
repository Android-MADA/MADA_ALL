package com.mada.myapplication.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.CustomFunction.ButtonInfo
import com.mada.myapplication.CustomFunction.DataRepo
import com.mada.myapplication.HomeFunction.Model.Category
import com.mada.myapplication.HomeFunction.Model.CategoryList1
import com.mada.myapplication.HomeFunction.Model.Todo
import com.mada.myapplication.HomeFunction.Model.TodoList
import com.mada.myapplication.HomeFunction.adapter.todo.HomeCateListAdapter
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.bottomsheetdialog.TodoDateBottomSheetDialog
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
import java.util.Calendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class FragHome : Fragment() {

    lateinit var binding : TodoLayoutBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
    private val apiMy = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


        val colorbuttonInfo = when (DataRepo.buttonInfoEntity?.colorButtonInfo?.serverID) {
            10 -> ButtonInfo(R.id.btn_color_basic, 10, R.drawable.c_ramdi)
            11 -> ButtonInfo(R.id.btn_color_bluepurple, 11, R.drawable.c_ramdybp)
            12 -> ButtonInfo(R.id.btn_color_green, 12, R.drawable.c_ramdyg)
            13 -> ButtonInfo(R.id.btn_color_mint, 13, R.drawable.c_ramdymint)
            14 -> ButtonInfo(R.id.btn_color_orange, 14, R.drawable.c_ramdyo)
            15 -> ButtonInfo(R.id.btn_color_orange2, 15, R.drawable.c_ramdyoy)
            16 -> ButtonInfo(R.id.btn_color_pink, 16, R.drawable.c_ramdypn)
            17 -> ButtonInfo(R.id.btn_color_pink2, 17, R.drawable.c_ramdypink)
            18 -> ButtonInfo(R.id.btn_color_pink3, 18, R.drawable.c_ramdydp)
            26 -> ButtonInfo(R.id.btn_color_purple, 26, R.drawable.c_ramdyp)
            27 -> ButtonInfo(R.id.btn_color_Rblue, 27, R.drawable.c_ramdyrb)
            28 -> ButtonInfo(R.id.btn_color_yellow, 28, R.drawable.c_ramdyy)
            51 -> ButtonInfo(R.id.btn_color_yellow2, 51, R.drawable.c_ramdyyellow)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val clothbuttonInfo = when (DataRepo.buttonInfoEntity?.clothButtonInfo?.serverID) {
            49 -> ButtonInfo(0, 49, R.drawable.custom_empty)
            41 -> ButtonInfo(R.id.btn_cloth_dev, 41, R.drawable.set_dev)
            44 -> ButtonInfo(R.id.btn_cloth_movie, 44, R.drawable.set_movie)
            40 -> ButtonInfo(R.id.btn_cloth_caffK, 40, R.drawable.set_caffk)
            46 -> ButtonInfo(R.id.btn_cloth_v, 46, R.drawable.set_v)
            39 -> ButtonInfo(R.id.btn_cloth_astronauts, 39, R.drawable.set_astronauts,)
            47 -> ButtonInfo(R.id.btn_cloth_zzim, 47, R.drawable.set_zzim)
            42 -> ButtonInfo(R.id.btn_cloth_hanbokF, 42, R.drawable.set_hanbokf)
            43 -> ButtonInfo(R.id.btn_cloth_hanbokM, 43, R.drawable.set_hanbokm)
            45 -> ButtonInfo(R.id.btn_cloth_snowman, 45, R.drawable.set_snowman)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        val itembuttonInfo = when (DataRepo.buttonInfoEntity?.itemButtonInfo?.serverID) {
            50 -> ButtonInfo(0, 50, R.drawable.custom_empty)
            22 -> ButtonInfo(R.id.btn_item_glass_normal, 22,R.drawable.g_nomal)
            30 -> ButtonInfo(R.id.btn_item_hat_ber, 30, R.drawable.hat_ber)
            33 -> ButtonInfo(R.id.btn_item_hat_grad, 33, R.drawable.hat_grad)
            21 -> ButtonInfo(R.id.btn_item_glass_8bit, 21,R.drawable.g_8bit)
            25 -> ButtonInfo(R.id.btn_item_glass_woig, 25, R.drawable.g_woig)
            35 -> ButtonInfo(R.id.btn_item_hat_ipod , 35, R.drawable.hat_ipod)
            24 -> ButtonInfo(R.id.btn_item_glass_sunR , 24,R.drawable.g_sunr)
            23 -> ButtonInfo(R.id.btn_item_glass_sunB,23, R.drawable.g_sunb)
            32 -> ButtonInfo(R.id.btn_item_hat_flower, 32, R.drawable.hat_flower)
            37 -> ButtonInfo(R.id.btn_item_hat_v, 37, R.drawable.hat_v)
            31 -> ButtonInfo(R.id.btn_item_hat_dinof, 31,R.drawable.hat_dinof)
            36 -> ButtonInfo(R.id.btn_item_hat_sheep, 36, R.drawable.hat_sheep)
            19 -> ButtonInfo(R.id.btn_item_bag_e,19, R.drawable.bag_e)
            20 -> ButtonInfo(R.id.btn_item_bag_luck,20, R.drawable.bag_luck)
            34 -> ButtonInfo(R.id.btn_item_hat_heart,34, R.drawable.hat_heart)
            29 -> ButtonInfo(R.id.btn_item_hat_bee, 29, R.drawable.hat_bee)
            38 -> ButtonInfo(R.id.btn_item_hat_heads, 38, R.drawable.heads)
            else -> throw IllegalArgumentException("Unknown button ID")
        }

        binding.todoCharacterIv.setImageResource(
            colorbuttonInfo.selectedImageResource ?: 0
        )
        binding.ivHomeCloth.setImageResource(
            clothbuttonInfo.selectedImageResource ?: 0
        )
        binding.ivHomeItem.setImageResource(
            itembuttonInfo.selectedImageResource ?: 0
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




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
