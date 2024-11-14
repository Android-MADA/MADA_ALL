package com.mada.reapp.HomeFunction.view

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mada.reapp.CalenderFuntion.Model.CalendarViewModel
import com.mada.reapp.HomeFunction.HomeBackCustomDialog
import com.mada.reapp.HomeFunction.HomeCustomDialogListener
import com.mada.reapp.HomeFunction.HomeDeleteCustomDialog
import com.mada.reapp.HomeFunction.Model.PostRequestCategory
import com.mada.reapp.HomeFunction.adapter.category.HomeCateColorAdapter
import com.mada.reapp.HomeFunction.adapter.category.HomeCateIconAdapter
import com.mada.reapp.HomeFunction.api.HomeApi
import com.mada.reapp.HomeFunction.api.RetrofitInstance
import com.mada.reapp.HomeFunction.dialog.ActiveBottomSheetDialog
import com.mada.reapp.HomeFunction.viewModel.HomeViewModel
import com.mada.reapp.MainActivity
import com.mada.reapp.R
import com.mada.reapp.databinding.HomeFragmentCategoryAddBinding
import com.mada.reapp.db.entity.CateEntity
import com.mada.reapp.hideBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryAddFragment : Fragment(), HomeCustomDialogListener {

    lateinit var binding: HomeFragmentCategoryAddBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val calendarViewModel : CalendarViewModel by activityViewModels()

    val cateIconArray = ArrayList<String>()
    val cateColorArray = ArrayList<String>()
    val iconAdapter = HomeCateIconAdapter(cateIconArray)
    val colorAdapter = HomeCateColorAdapter(cateColorArray)

    private var bottomFlag = true
    private lateinit var backDialog: HomeBackCustomDialog
    private lateinit var deleteDialog: HomeDeleteCustomDialog
    private lateinit var argsArray: java.util.ArrayList<String>
    private lateinit var mAdView : AdView

    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.btnHomeCateAddSave.isVisible){
                    //등록 상황
                    customBackDialog()
                }
                else if(binding.btnHomeCateAddSave.isGone && binding.edtHomeCategoryName.isVisible){
                    //active 수정 상황
                    if (binding.edtHomeCategoryName.text.isBlank()) {
                        //Toast.makeText(context, "카테고리 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                        calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", requireView())
                    }
                    else {
                            val catePostData = PostRequestCategory(binding.edtHomeCategoryName.text.toString(), colorAdapter.selecetedColor, findIconId(iconAdapter.selectedIcon), false, false)
                            val cateData = CateEntity(id = argsArray[0].toInt(), categoryName = binding.edtHomeCategoryName.text.toString(), color = colorAdapter.selecetedColor, iconId = findIconId(iconAdapter.selectedIcon), isInActive = false)
                            //서버에 patch 전송
                            viewModel.editCategoryAPI(categoryId = argsArray[0].toInt(), category = catePostData, cateDB = cateData) { result ->
                                when (result) {
                                    0 -> {
                                        //success
                                        findNavController().popBackStack()
                                    }

                                    1 -> {
                                        //fail
                                        Toast.makeText(
                                            context,
                                            "카테고리 수정에 실패했습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().popBackStack()
                                    }
                                }

                            }
                    }
                }
                else{
                    //inActive show
                    findNavController().popBackStack()
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
        initColorArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment_category_add, container, false)
        hideBottomNavigation(bottomFlag, activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * 구글 광고
         */
        //구글 플레이스토어 광고
        val mainActivity = requireActivity() as MainActivity
        if(mainActivity.getPremium()) {
        } else {
            MobileAds.initialize(this.requireContext()) {}
            val adRequest = AdRequest.Builder().build()
            binding.adViewCategory.loadAd(adRequest)
        }

        /**
         * 기존 카테고리 조회 시
         */
        if(arguments != null){
            argsArray =requireArguments().getStringArrayList("key")!!

            //등록 버튼 gone, 메뉴iv visible
            binding.btnHomeCateAddSaveMenu.isVisible = true
            binding.btnHomeCateAddSave.isGone = true


            if(argsArray[4] == "inactive"){

                //카테고리명 적용
                binding.edtHomeCategoryNameShow.isVisible = true
                binding.edtHomeCategoryNameShow.text = argsArray!![1]
                //edt gone
                binding.edtHomeCategoryName.isGone = true

                //color, icon click listener 없는 걸로 교체
                binding.ivHomeCateColorShow.isVisible = true
                binding.ivHomeCateColorShow.imageTintList = ColorStateList.valueOf(Color.parseColor(argsArray!![2]))
                binding.ivHomeCateColor.isGone = true

                binding.ivHomeCateIconShow.isVisible = true
                binding.ivHomeCateIconShow.setImageResource(findIcon(argsArray!![3].toInt()))
                binding.ivHomeCateIcon.isGone = true


                binding.btnHomeCateAddSaveMenu.setOnClickListener {
                    /**
                     * bottomsheetdialog
                     */
                    val inactiveBottomSheetDialog : ActiveBottomSheetDialog = ActiveBottomSheetDialog() {
                        when(it){
                            2 -> {
                                // 복원 동작
                                if(!viewModel.isSubscribe && viewModel.activeNum >=5){
                                    calendarViewModel.setPopupOne(requireContext(), "더 이상 카테고리를 추가할 수 없습니다.", view, "프리미엄 결제 시 카테고리를 제한 없이 추가할 수 있어요.")
                                }
                                else{

                                    calendarViewModel.setPopupTwo(requireContext(), "카테고리를 복원하시겠습니까?", view, 0, "restore", argsArray!![0].toInt())
                                }
                            }
                            1 -> {
                                // 삭제 동작
                                calendarViewModel.setPopupTwo(requireContext(), "정말 삭제하시겠습니까?", view, 0, "delete", argsArray!![0].toInt())
                            }
                        }
                    }
                    inactiveBottomSheetDialog.flag = "inactive"
                    inactiveBottomSheetDialog.show(parentFragmentManager, inactiveBottomSheetDialog.tag)

                }
            }
            else {
                /**
                 * active category 수정 시
                 */

                //color, icon clickListener 활성화
                binding.ivHomeCateColor.imageTintList = ColorStateList.valueOf(Color.parseColor(argsArray!![2]))
                colorAdapter.selecetedColor = argsArray!![2]
                binding.ivHomeCateIcon.setImageResource(findIcon(argsArray!![3].toInt()))
                iconAdapter.selectedIcon = findIcon(argsArray!![3].toInt()).toString()
                binding.edtHomeCategoryName.setText(argsArray!![1])

                //edt 꺼내고 text 없애기
                binding.edtHomeCategoryNameShow.isGone = true
                binding.edtHomeCategoryName.isVisible = true

                //iv 교체하기
                binding.ivHomeCateColorShow.isGone = true
                binding.ivHomeCateColor.isVisible = true

                binding.ivHomeCateColorShow.isGone = true
                binding.ivHomeCateIcon.isVisible = true

                //메뉴 클릭 시 active bottomSheetDialog
                binding.btnHomeCateAddSaveMenu.setOnClickListener {
                    val activeBottomSheetDialog : ActiveBottomSheetDialog = ActiveBottomSheetDialog() {
                        when(it){
                            0 -> {
                                // 종료 동작
                                calendarViewModel.setPopupTwo(requireContext(), "정말 종료하시겠습니까?", view, 0, "quit", argsArray!![0].toInt())
                            }
                            1 -> {
                                // 삭제 동작
                                calendarViewModel.setPopupTwo(requireContext(), "정말 삭제하시겠습니까?", view, 0, "delete", argsArray!![0].toInt())
                            }
                        }
                    }
                    activeBottomSheetDialog.flag = "active"
                    activeBottomSheetDialog.show(parentFragmentManager, activeBottomSheetDialog.tag)
                }
            }
        }
        else {
            //카테고리 최초 추가 상황
            colorAdapter.selecetedColor = "#89A9D9"
            iconAdapter.selectedIcon = R.drawable.ic_home_cate_study.toString()
            binding.btnHomeCateAddSaveMenu.isGone = true
            binding.btnHomeCateAddSave.isVisible = true

            //edt 꺼내고 text들 없애기
            binding.edtHomeCategoryNameShow.isGone = true
            binding.edtHomeCategoryName.isVisible = true

            //iv 교체하기
            binding.ivHomeCateColorShow.isGone = true
            binding.ivHomeCateColor.isVisible = true

            binding.ivHomeCateColorShow.isGone = true
            binding.ivHomeCateIcon.isVisible = true

            binding.btnHomeCateAddSave.text = "등록"

        }

        val iconListManager = GridLayoutManager(this.activity, 9)
        val colorListManager = GridLayoutManager(this.activity, 8)

        iconAdapter.setItemClickListener(object : HomeCateIconAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                binding.ivHomeCateIcon.setImageResource(cateIconArray[position].toInt())
                binding.rvHomeCateIcon.isGone = true
                iconAdapter.selectedIcon = cateIconArray[position]

            }
        })

        colorAdapter.setItemClickListener(object : HomeCateColorAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                binding.ivHomeCateColor.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(cateColorArray[position]))
                binding.rvHomeCateColor.isGone = true
                colorAdapter.selecetedColor = cateColorArray[position]
            }
        })

        var iconRecyclerList = binding.rvHomeCateIcon.apply {
            setHasFixedSize(true)
            layoutManager = iconListManager
            adapter = iconAdapter

        }
        var colorRecyclerList = binding.rvHomeCateColor.apply {
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }

        binding.ivHomeCateIcon.setOnClickListener {
            if (binding.rvHomeCateIcon.isVisible) {
                binding.rvHomeCateIcon.isGone = true
            } else {
                binding.rvHomeCateIcon.isVisible = true
            }
        }

        binding.ivHomeCateColor.setOnClickListener {
            if (binding.rvHomeCateColor.isVisible) {
                binding.rvHomeCateColor.isGone = true
            } else {
                binding.rvHomeCateColor.isVisible = true
            }
        }

        //좌상단 뒤로가기 버튼 클릭 시
        binding.ivHomeCateAddBack.setOnClickListener {
            if(binding.btnHomeCateAddSave.isVisible){
                //등록 상황
                customBackDialog()
            }
            else if(binding.btnHomeCateAddSave.isGone && binding.edtHomeCategoryName.isVisible){
                //active 수정 상황
                if (binding.edtHomeCategoryName.text.isBlank()) {
                    calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", view)
                }
                else {
                    val buffering = viewModel.setPopupBufferingTodo(requireContext())
                    val catePostData = PostRequestCategory(binding.edtHomeCategoryName.text.toString(), colorAdapter.selecetedColor, findIconId(iconAdapter.selectedIcon), false, false)
                        val cateData = CateEntity(id = argsArray[0].toInt(), categoryName = binding.edtHomeCategoryName.text.toString(), color = colorAdapter.selecetedColor, iconId = findIconId(iconAdapter.selectedIcon), isInActive = false)
                    //서버에 patch 전송
                    viewModel.editCategoryAPI(categoryId = argsArray[0].toInt(), category = catePostData, cateDB = cateData) { result ->
                        when (result) {
                            0 -> {
                                //success
                                findNavController().popBackStack()
                            }

                            1 -> {
                                //fail
                                Toast.makeText(
                                    context,
                                    "카테고리 수정에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()
                            }
                        }

                    }
                    buffering.dismiss()
                }

            }
            else{
                //inActive show
                findNavController().popBackStack()
            }
        }


        binding.btnHomeCateAddSave.setOnClickListener {
                //카테고리 등록 상황
                if (binding.edtHomeCategoryName.text.isBlank()) {
                    calendarViewModel.setPopupOne(requireContext(), "제목을 입력해주세요.", view)
                }
                else {
                        //내용 db 저장
                    val buffering = viewModel.setPopupBufferingTodo(requireContext())
                    val cateName = binding.edtHomeCategoryName.text.toString()
                        val cateColor = colorAdapter.selecetedColor
                        val cateIconId = findIconId(iconAdapter.selectedIcon)

                        //서버 post 후 response id 넣어서 db 저장
                        val catePostData = PostRequestCategory(cateName, cateColor, cateIconId, false, false)

                        viewModel.addCategoryAPI(catePostData, cateName, cateColor, cateIconId){
                            result ->
                            when(result){
                                0 -> {
                                    //success
                                    Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                                }
                                1 -> {
                                    //fail
                                    Toast.makeText(context, "카테고리 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                    Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)

                                }
                            }
                        }
                    buffering.dismiss()

                }
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initArrayList() {
        with(cateIconArray) {
            cateIconArray.add(R.drawable.ic_home_cate_meal1.toString())
            cateIconArray.add(R.drawable.ic_home_cate_meal2.toString())
            cateIconArray.add(R.drawable.ic_home_cate_chat1.toString())
            cateIconArray.add(R.drawable.ic_home_cate_health.toString())
            cateIconArray.add(R.drawable.ic_home_cate_phone.toString())
            cateIconArray.add(R.drawable.ic_home_cate_rest.toString())
            cateIconArray.add(R.drawable.ic_home_cate_plan.toString())
            cateIconArray.add(R.drawable.ic_home_cate_pen.toString())
            cateIconArray.add(R.drawable.ic_home_cate_burn.toString())
            cateIconArray.add(R.drawable.ic_home_cate_music.toString())
            cateIconArray.add(R.drawable.ic_home_cate_lightup.toString())
            cateIconArray.add(R.drawable.ic_home_cate_lightout.toString())
            cateIconArray.add(R.drawable.ic_home_cate_work.toString())
            cateIconArray.add(R.drawable.ic_home_cate_mic.toString())
            cateIconArray.add(R.drawable.ic_home_cate_sony.toString())
            cateIconArray.add(R.drawable.ic_home_cate_heart.toString())
            cateIconArray.add(R.drawable.ic_home_cate_study.toString())
            cateIconArray.add(R.drawable.ic_home_cate_laptop.toString())
        }
    }

    private fun initColorArray() {
        with(cateColorArray) {
            cateColorArray.add("#ED3024")
            cateColorArray.add("#F65F55")
            cateColorArray.add("#FD8415")
            cateColorArray.add("#FEBD16")
            cateColorArray.add("#FBA1B1")
            cateColorArray.add("#F46D85")
            cateColorArray.add("#D087F2")
            cateColorArray.add("#A516BC")
            cateColorArray.add("#89A9D9")
            cateColorArray.add("#269CB1")
            cateColorArray.add("#3C67A7")
            cateColorArray.add("#405059")
            cateColorArray.add("#C0D979")
            cateColorArray.add("#8FBC10")
            cateColorArray.add("#107E3D")
            cateColorArray.add("#0E4122")
        }
    }

    private fun customBackDialog() {
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }

    // 커스텀 다이얼로그에서 버튼 클릭 시
    override fun onYesButtonClicked(dialog: Dialog, flag: String) {
        if (flag == "delete") {
            //viewModel.deleteCategory(viewModel.userToken, argsArray!![0].toInt(), requireView())
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("deleteCate", viewModel.cate.value.toString())
                viewModel.deleteCate(viewModel.cate.value!!)

                withContext(Dispatchers.Main){
                    Navigation.findNavController(requireView()).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                }
            }
        } else {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
        }
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog: Dialog) {
        dialog.dismiss()
    }

    private fun findIconId(icon: String): Int {
        val iconId: Int = when (icon) {
            R.drawable.ic_home_cate_meal1.toString() -> {
                9
            }

            R.drawable.ic_home_cate_meal2.toString() -> {
                8
            }

            R.drawable.ic_home_cate_chat1.toString() -> {
                2
            }

            R.drawable.ic_home_cate_health.toString() -> {
                3
            }

            R.drawable.ic_home_cate_phone.toString() -> {
                13
            }

            R.drawable.ic_home_cate_rest.toString() -> {
                15
            }

            R.drawable.ic_home_cate_heart.toString() -> {
                4
            }

            R.drawable.ic_home_cate_study.toString() -> {
                17
            }

            R.drawable.ic_home_cate_laptop.toString() -> {
                5
            }

            R.drawable.ic_home_cate_music.toString() -> {
                11
            }

            R.drawable.ic_home_cate_lightup.toString() -> {
                7
            }

            R.drawable.ic_home_cate_lightout.toString() -> {
                6
            }

            R.drawable.ic_home_cate_pen.toString() -> {
                12
            }

            R.drawable.ic_home_cate_burn.toString() -> {
                1
            }

            R.drawable.ic_home_cate_plan.toString() -> {
                14
            }

            R.drawable.ic_home_cate_work.toString() -> {
                18
            }

            R.drawable.ic_home_cate_mic.toString() -> {
                10
            }

            else -> {
                16
            }
        }
        return iconId
    }

    fun findIcon(iconId: Int): Int {
        val icon = when (iconId) {
            1 -> {
                R.drawable.ic_home_cate_burn
            }

            2 -> {
                R.drawable.ic_home_cate_chat1
            }

            3 -> {
                R.drawable.ic_home_cate_health
            }

            4 -> {
                R.drawable.ic_home_cate_heart
            }

            5 -> {
                R.drawable.ic_home_cate_laptop
            }

            6 -> {
                R.drawable.ic_home_cate_lightout
            }

            7 -> {
                R.drawable.ic_home_cate_lightup
            }

            8 -> {
                R.drawable.ic_home_cate_meal2
            }

            9 -> {
                R.drawable.ic_home_cate_meal1
            }

            10 -> {
                R.drawable.ic_home_cate_mic
            }

            11 -> {
                R.drawable.ic_home_cate_music
            }

            12 -> {
                R.drawable.ic_home_cate_pen
            }

            13 -> {
                R.drawable.ic_home_cate_phone
            }

            14 -> {
                R.drawable.ic_home_cate_plan
            }

            15 -> {
                R.drawable.ic_home_cate_rest
            }

            16 -> {
                R.drawable.ic_home_cate_sony
            }

            17 -> {
                R.drawable.ic_home_cate_study
            }

            else -> {
                R.drawable.ic_home_cate_work
            }
        }
        return icon
    }


}