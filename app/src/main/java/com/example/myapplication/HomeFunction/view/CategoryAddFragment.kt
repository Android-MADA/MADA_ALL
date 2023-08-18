package com.example.myapplication.HomeFunction.view

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.HomeDeleteCustomDialog
import com.example.myapplication.HomeFunction.Model.PactchResponseCategory
import com.example.myapplication.HomeFunction.Model.PatchRequestCategory
import com.example.myapplication.HomeFunction.adapter.category.HomeCateColorAdapter
import com.example.myapplication.HomeFunction.adapter.category.HomeCateIconAdapter
import com.example.myapplication.HomeFunction.api.HomeApi
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentCategoryAddBinding
import com.example.myapplication.hideBottomNavigation
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


        if (arguments != null) {
            //데이터 뿌리기
            argsArray = requireArguments().getStringArrayList("key")!!
//            binding.ivHomeCateIcon.setImageResource(argsArray!![2].toInt())
//            binding.edtHomeCategoryName.setText(argsArray!![1])
//            binding.ivHomeCateColor.imageTintList = ColorStateList.valueOf(Color.parseColor(argsArray!![3]))
            binding.edtHomeCategoryName.setText(argsArray!![0])
            binding.ivHomeCateColor.imageTintList =
                ColorStateList.valueOf(Color.parseColor(argsArray!![1]))
            // 수정버튼 활성화
            binding.btnHomeCateAddSave.text = "수정"
            //삭제버튼 활성화
            binding.btnHomeTimeEditDelete.isVisible = true

        } else {
            colorAdapter.selecetedColor = "#89A9D9"
            iconAdapter.selectedIcon = R.drawable.ic_home_cate_study.toString()
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

        binding.ivHomeCateAddBack.setOnClickListener {
            //다이얼로그
            customBackDialog()
        }

        binding.btnHomeCateAddSave.setOnClickListener {


            if (binding.edtHomeCategoryName.text.isBlank()) {
                Toast.makeText(this.requireActivity(), "카테고리 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                //데이터 변경
                if (binding.btnHomeCateAddSave.text == "수정") {
                    //서버 전송(PATCH)
                    Navigation.findNavController(view)
                        .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
                    Log.d("cateEdit", "확인")
                } else {
                    val data = PatchRequestCategory(
                        binding.edtHomeCategoryName.text.toString(),
                        colorAdapter.selecetedColor,
                        1
                    )
                    //서버 전송(POST)
//                    val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
//                    api.postCategory(viewModel.userToken, data).enqueue(object : Callback<PactchResponseCategory>{
//                        override fun onResponse(
//                            call: Call<PactchResponseCategory>,
//                            response: Response<PactchResponseCategory>
//                        ) {
//                           if(response.isSuccessful){
//                               Log.d("addCate", response.body().toString())
//                           }
//                            else {
//                                Log.d("addCate", "onresponse 실패")
//                           }
//                        }
//
//                        override fun onFailure(call: Call<PactchResponseCategory>, t: Throwable) {
//                            Log.d("addCate", "onFailure")
//                        }
//
//                    })
                    viewModel.postCategory(viewModel.userToken, data, view)

//                    Navigation.findNavController(view)
//                        .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)

                }

            }
        }

        binding.btnHomeTimeEditDelete.setOnClickListener {
            customDeleteDialog()
        }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.btnHomeTimeEditDelete.isGone = true
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
            viewModel.removeCate(argsArray!![4].toInt())
            //카테고리, 카테고리에 해당하는 todo 삭제 서버 전송(DELETE, id)
            //viewModel.deleteCategory(viewModel.userToken, (argsArray!![0].toInt()))
        }
        Navigation.findNavController(requireView())
            .navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog: Dialog) {
        dialog.dismiss()
    }

}