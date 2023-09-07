package com.example.myapplication.HomeFunction.view

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
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.HomeDeleteCustomDialog
import com.example.myapplication.HomeFunction.Model.CategoryList1
import com.example.myapplication.HomeFunction.Model.PactchResponseCategory
import com.example.myapplication.HomeFunction.Model.PatchRequestTodo
import com.example.myapplication.HomeFunction.Model.PostRequestCategory
import com.example.myapplication.HomeFunction.adapter.category.HomeCateColorAdapter
import com.example.myapplication.HomeFunction.adapter.category.HomeCateIconAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentCategoryAddBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.hideBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryAddFragment : Fragment(), HomeCustomDialogListener {

    lateinit var binding: HomeFragmentCategoryAddBinding
    private val viewModel: HomeViewModel by activityViewModels()

    val cateIconArray = ArrayList<String>()
    val cateColorArray = ArrayList<String>()
    val iconAdapter = HomeCateIconAdapter(cateIconArray)
    val colorAdapter = HomeCateColorAdapter(cateColorArray)

    private var bottomFlag = true
    private lateinit var backDialog: HomeBackCustomDialog
    private lateinit var deleteDialog: HomeDeleteCustomDialog
    private lateinit var argsArray: java.util.ArrayList<String>

    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

    //    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                //cateogry 수정 상황 일 때
//                if(binding.btnHomeCateAddSave.text == "삭제"){
//                    //수정 사항 저장
//                    val data = PostRequestCategory(
//                        binding.edtHomeCategoryName.text.toString(),
//                        colorAdapter.selecetedColor,
//                        findIconId(iconAdapter.selectedIcon)
//                    )
//                    Log.d("cateAdd 뒤로가기", data.toString())
//                  //viewModel.patchCategory(viewModel.userToken, argsArray!![0].toInt(), data, null)
//
//                }
//                //카테고리 추가 상황 일 때
//                else {
//                    customBackDialog()
//                }
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
//    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //뒤로가기 버튼 클릭 리스너 -> 등록 : 다이얼로그, 수정 : 수정후 페이지 전환
                if (binding.btnHomeCateAddSaveMenu.isVisible) {
                    CoroutineScope(Dispatchers.IO).launch {

                        val catePostData = PostRequestCategory(binding.edtHomeCategoryName.text.toString(), colorAdapter.selecetedColor, findIconId(iconAdapter.selectedIcon), false, false)
                        val cateData = CateEntity(id = argsArray[0].toInt(), categoryName = binding.edtHomeCategoryName.text.toString(), color = colorAdapter.selecetedColor, iconId = findIconId(iconAdapter.selectedIcon), isInActive = false)
                        //서버에 patch 전송
                        api.editHCategory(viewModel.userToken, categoryId = argsArray[0].toInt(), catePostData).enqueue(object : Callback<PactchResponseCategory>{
                            override fun onResponse(
                                call: Call<PactchResponseCategory>,
                                response: Response<PactchResponseCategory>
                            ) {
                                if(response.isSuccessful){
                                    Log.d("cateupdate", "성공")
                                } else {
                                    Log.d("cateupdate", "안드 잘못 실패")
                                }
                            }

                            override fun onFailure(
                                call: Call<PactchResponseCategory>,
                                t: Throwable
                            ) {
                                Log.d("cateupdate", "서버 연결 실패")
                            }

                        })
                        viewModel.updateCate(cateData)

                    }
                    Navigation.findNavController(view!!)
                        .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                }
                else {
                    customBackDialog()
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



        if(arguments != null){
            argsArray =requireArguments().getStringArrayList("key")!!
            binding.edtHomeCategoryName.setText(argsArray!![1])
            binding.ivHomeCateColor.imageTintList = ColorStateList.valueOf(Color.parseColor(argsArray!![2]))
            colorAdapter.selecetedColor = argsArray!![2]
            binding.ivHomeCateIcon.setImageResource(findIcon(argsArray!![3].toInt()))
            iconAdapter.selectedIcon = findIcon(argsArray!![3].toInt()).toString()
            binding.btnHomeCateAddSaveMenu.isVisible = true
            binding.btnHomeCateAddSave.isGone = true
        }
        else {
            colorAdapter.selecetedColor = "#89A9D9"
            iconAdapter.selectedIcon = R.drawable.ic_home_cate_study.toString()
            binding.btnHomeCateAddSaveMenu.isGone = true
            binding.btnHomeCateAddSave.isVisible = true
        }

        val iconListManager = GridLayoutManager(this.activity, 6)
        val colorListManager = GridLayoutManager(this.activity, 6)

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
            //cateogry 수정 상황 일 때
            if (binding.btnHomeCateAddSaveMenu.isVisible) {
                //수정 사항 저장
                val cate = CateEntity(
                    argsArray!![0].toInt(),
                    binding.edtHomeCategoryName.text.toString(),
                    colorAdapter.selecetedColor,
                    false,
                    findIconId(iconAdapter.selectedIcon)
                )
                //서버 patch, db 저장
                CoroutineScope(Dispatchers.IO).launch {
                    val catePostData = PostRequestCategory(binding.edtHomeCategoryName.text.toString(), colorAdapter.selecetedColor, findIconId(iconAdapter.selectedIcon), false, false)
                    api.editHCategory(viewModel.userToken, categoryId = argsArray[0].toInt(), catePostData).enqueue(object : Callback<PactchResponseCategory>{
                        override fun onResponse(
                            call: Call<PactchResponseCategory>,
                            response: Response<PactchResponseCategory>
                        ) {
                            if(response.isSuccessful){
                                Log.d("cateupdate", "성공")
                            } else {
                                Log.d("cateupdate", "안드 잘못 실패")
                            }
                        }

                        override fun onFailure(
                            call: Call<PactchResponseCategory>,
                            t: Throwable
                        ) {
                            Log.d("cateupdate", "서버 연결 실패")
                        }

                    })
                    viewModel.updateCate(cate)
                    withContext(Dispatchers.Main){
                        Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                    }
                }


            }
            //카테고리 추가 상황 일 때
            else {
                customBackDialog()
            }
        }


        binding.btnHomeCateAddSave.setOnClickListener {

            if (binding.edtHomeCategoryName.text.isBlank()) {
                Toast.makeText(this.requireActivity(), "카테고리 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                    CoroutineScope(Dispatchers.IO).launch {
                        //내용 db 저장
                        val cateName = binding.edtHomeCategoryName.text.toString()
                        val cateColor = colorAdapter.selecetedColor
                        val cateIconId = findIconId(iconAdapter.selectedIcon)

                        //서버 post 후 response id 넣어서 db 저장
                        val catePostData = PostRequestCategory(cateName, cateColor, cateIconId, false, false)
                        api.postHCategory(viewModel.userToken, catePostData).enqueue(object :Callback<PactchResponseCategory>{
                            override fun onResponse(
                                call: Call<PactchResponseCategory>,
                                response: Response<PactchResponseCategory>
                            ) {
                                if(response.isSuccessful){
                                    viewModel.createCate(CateEntity(id = response.body()!!.data.Category.id, categoryName = cateName, color = cateColor, isInActive = false, iconId = cateIconId))
                                } else {
                                    Log.d("cate안드 잘못", "서버 연결 실패")

                                }
                            }

                            override fun onFailure(
                                call: Call<PactchResponseCategory>,
                                t: Throwable
                            ) {
                                Log.d("cate서버 연결 오류", "서버 연결 실패")
                            }

                        })


                        // navigaiton 이동
                        withContext(Dispatchers.Main){
                            Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                        }
                    }
            }

        }

        binding.btnHomeCateAddSaveMenu.setOnClickListener {
            //메뉴바 show
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.cate_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.cate_quit) {
                //다이얼로그
                //종료로 업데이트하고 네비게이션
                    // 해당 카테고리 내 모든 투두 삭제, 모든 반복 투두 삭제
                    val cate = CateEntity(
                        argsArray!![0].toInt(),
                        binding.edtHomeCategoryName.text.toString(),
                        colorAdapter.selecetedColor,
                        true,
                        findIconId(iconAdapter.selectedIcon)
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        //종료 patch
                        api.quitCategory(viewModel.userToken, categoryId = argsArray!![0].toInt()).enqueue(object : Callback<Void>{
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    Log.d("cateupdate", "성공")
                                } else {
                                    Log.d("cateupdate", "안드 잘못 실패")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("cateupdate", "서버 연결 실패")
                            }
                        })

                        viewModel.updateCate(cate)
                        withContext(Dispatchers.Main){
                            Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                        }
                    }

                }
                else{
                    //다이얼로그
                    //카테고리 테이블에서 삭제
                    //해당 카테고리 내 모든 반복 투두와 투두 삭제
                    val cateData = CateEntity(
                        argsArray!![0].toInt(),
                        argsArray!![1],
                        argsArray!![2],
                        true,
                        argsArray!![3].toInt())
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.deleteCate(cateData)
                        //서버 전송
                        api.deleteHCategory(viewModel.userToken, categoryId = argsArray!![0].toInt()).enqueue(object :Callback<Void>{
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    Log.d("cateupdate", "성공")
                                } else {
                                    Log.d("cateupdate", "안드 잘못 실패")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("cateupdate", "서버 연결 실패")
                            }

                        })
                        //해당 카테고리 내 보든 반복투두와 투두 삭제 코드
                        withContext(Dispatchers.Main){
                            Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                        }
                    }
                    Log.d("catedelete", "click")
                }
                true
            }
            popup.show()
        }



//        if (arguments != null) {
//            //카테고리 수정 시 데이터 뿌리기
//            argsArray = requireArguments().getStringArrayList("key")!!
//            Log.d("argsArray", argsArray!!.toString())
//            binding.ivHomeCateIcon.setImageResource(findIcon(argsArray!![2].toInt()))
//            binding.edtHomeCategoryName.setText(argsArray!![1])
//            binding.ivHomeCateColor.imageTintList =
//                ColorStateList.valueOf(Color.parseColor(argsArray!![3]))
//            colorAdapter.selecetedColor = argsArray!![3]
//            iconAdapter.selectedIcon = findIcon(argsArray!![2].toInt()).toString()
//            // 삭제버튼으로 변경
//            binding.btnHomeCateAddSave.text = "삭제"
//
//        }
//        //카텍리 추가시
//        else {
//            colorAdapter.selecetedColor = "#89A9D9"
//            iconAdapter.selectedIcon = R.drawable.ic_home_cate_study.toString()
//        }
//
//        // 아이콘, 색상 선택창
//        val iconListManager = GridLayoutManager(this.activity, 6)
//        val colorListManager = GridLayoutManager(this.activity, 6)
//
//        iconAdapter.setItemClickListener(object : HomeCateIconAdapter.OnItemClickListener {
//            override fun onClick(v: View, position: Int) {
//                binding.ivHomeCateIcon.setImageResource(cateIconArray[position].toInt())
//                binding.rvHomeCateIcon.isGone = true
//                iconAdapter.selectedIcon = cateIconArray[position]
//
//            }
//        })
//
//        colorAdapter.setItemClickListener(object : HomeCateColorAdapter.OnItemClickListener {
//            override fun onClick(v: View, position: Int) {
//                binding.ivHomeCateColor.imageTintList =
//                    ColorStateList.valueOf(Color.parseColor(cateColorArray[position]))
//                binding.rvHomeCateColor.isGone = true
//                colorAdapter.selecetedColor = cateColorArray[position]
//            }
//        })
//
//        var iconRecyclerList = binding.rvHomeCateIcon.apply {
//            setHasFixedSize(true)
//            layoutManager = iconListManager
//            adapter = iconAdapter
//
//        }
//        var colorRecyclerList = binding.rvHomeCateColor.apply {
//            setHasFixedSize(true)
//            layoutManager = colorListManager
//            adapter = colorAdapter
//        }
//
//        binding.ivHomeCateIcon.setOnClickListener {
//            if (binding.rvHomeCateIcon.isVisible) {
//                binding.rvHomeCateIcon.isGone = true
//            } else {
//                binding.rvHomeCateIcon.isVisible = true
//            }
//        }
//
//        binding.ivHomeCateColor.setOnClickListener {
//            if (binding.rvHomeCateColor.isVisible) {
//                binding.rvHomeCateColor.isGone = true
//            } else {
//                binding.rvHomeCateColor.isVisible = true
//            }
//        }
//
//        val data = PostRequestCategory(
//            binding.edtHomeCategoryName.text.toString(),
//            colorAdapter.selecetedColor,
//            findIconId(iconAdapter.selectedIcon)
//        )
//
//        //좌상단 뒤로가기 버튼 클릭 시
//        binding.ivHomeCateAddBack.setOnClickListener {
//            //cateogry 수정 상황 일 때
//            if (binding.btnHomeCateAddSave.text == "삭제") {
//                //수정 사항 저장
//                viewModel.patchCategory(viewModel.userToken, argsArray!![0].toInt(), data, view)
//            }
//            //카테고리 추가 상황 일 때
//            else {
//                customBackDialog()
//            }
//        }
//
//        //등록 버튼, 삭제 버튼 클릭 시
//        binding.btnHomeCateAddSave.setOnClickListener {
//
//            //카테고리명 빈칸 확인
//            if (binding.edtHomeCategoryName.text.isBlank()) {
//                Toast.makeText(this.requireActivity(), "카테고리 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
//            } else {
//
//                //데이터 삭제
//                if (binding.btnHomeCateAddSave.text == "삭제") {
//                    customDeleteDialog()
//                }
//                //데이터 추가
//                else {
//                    Log.d("addCAte", data.toString())
//                    //viewModel.postCategory(viewModel.userToken, data, view)
//                    //db에 데이터 저장
//                    //viewModel.createCategory(DBCategory(0, null, data.categoryName, data.color, data.iconId))
//                    //Log.d("dbcategory", viewModel.getCategory().toString())
//                }
//
//            }
//        }


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
            cateIconArray.add(R.drawable.ic_home_cate_heart.toString())
            cateIconArray.add(R.drawable.ic_home_cate_study.toString())
            cateIconArray.add(R.drawable.ic_home_cate_laptop.toString())
            cateIconArray.add(R.drawable.ic_home_cate_music.toString())
            cateIconArray.add(R.drawable.ic_home_cate_lightup.toString())
            cateIconArray.add(R.drawable.ic_home_cate_lightout.toString())
            cateIconArray.add(R.drawable.ic_home_cate_pen.toString())
            cateIconArray.add(R.drawable.ic_home_cate_burn.toString())
            cateIconArray.add(R.drawable.ic_home_cate_plan.toString())
            cateIconArray.add(R.drawable.ic_home_cate_work.toString())
            cateIconArray.add(R.drawable.ic_home_cate_mic.toString())
            cateIconArray.add(R.drawable.ic_home_cate_sony.toString())
        }
    }

    private fun initColorArray() {
        with(cateColorArray) {
            cateColorArray.add("#E1E9F5")
            cateColorArray.add("#89A9D9")
            cateColorArray.add("#486DA3")
            cateColorArray.add("#FFE7EB")
            cateColorArray.add("#FDA4B4")
            cateColorArray.add("#F0768C")
            cateColorArray.add("#D4ECF1")
            cateColorArray.add("#7FC7D4")
            cateColorArray.add("#2AA1B7")
            cateColorArray.add("#FDF3CF")
            cateColorArray.add("#F8D141")
            cateColorArray.add("#405059")
        }
    }

    private fun customBackDialog() {
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }

    private fun customDeleteDialog() {
        deleteDialog = HomeDeleteCustomDialog(requireActivity(), this)
        deleteDialog.show()
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