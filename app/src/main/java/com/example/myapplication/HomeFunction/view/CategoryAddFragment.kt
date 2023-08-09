package com.example.myapplication.HomeFunction.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.HomeDeleteCustomDialog
import com.example.myapplication.HomeFunction.category.HomeCateColorAdapter
import com.example.myapplication.HomeFunction.category.HomeCateIconAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeFragmentCategoryAddBinding
import com.example.myapplication.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryAddFragment : Fragment(), HomeCustomDialogListener {

    lateinit var binding : HomeFragmentCategoryAddBinding

    val cateIconArray = ArrayList<Drawable>()
    val cateColorArray = ArrayList<Int>()
    val iconAdapter = HomeCateIconAdapter(cateIconArray)
    val colorAdapter = HomeCateColorAdapter(cateColorArray)
    private var bottomFlag = true
    private lateinit var backDialog : HomeBackCustomDialog
    private lateinit var deleteDialog : HomeDeleteCustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArrayList()
        initColorArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_category_add, container, false)
        hideBottomNavigation(bottomFlag, activity)

        if(arguments != null){
            //데이터 뿌리기
            val argsArray = requireArguments().getStringArrayList("key")
            binding.ivHomeCateIcon.setImageResource(argsArray!![1].toInt())
            binding.edtHomeCategoryName.setText(argsArray!![0])
            binding.ivHomeCateColor.imageTintList = ColorStateList.valueOf(argsArray!![2].toInt())
            // 수정버튼 활성화
            binding.btnHomeCateAddSave.text = "수정"
            //삭제버튼 활성화
            binding.btnHomeTimeEditDelete.isVisible = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val iconListManager = GridLayoutManager(this.activity, 6)
        val colorListManager = GridLayoutManager(this.activity, 6)


        iconAdapter.setItemClickListener(object: HomeCateIconAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                binding.ivHomeCateIcon.setImageDrawable(cateIconArray[position])
                binding.rvHomeCateIcon.isGone = true
            }
        })

        binding.ivHomeCateAddBack.setOnClickListener {
            //다이얼로그
            customBackDialog()

            Log.d("navBack", "정상 작동")
        }

        binding.btnHomeCateAddSave.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
            //서버 patch
            Log.d("navSave", "정상 작동")
        }

        binding.btnHomeTimeEditDelete.setOnClickListener {
            customDeleteDialog()
        }

        colorAdapter.setItemClickListener(object: HomeCateColorAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                binding.ivHomeCateColor.imageTintList = ColorStateList.valueOf(cateColorArray[position])
                binding.rvHomeCateColor.isGone = true
            }
        })

        var iconRecyclerList = binding.rvHomeCateIcon.apply {
            setHasFixedSize(true)
            layoutManager = iconListManager
            adapter = iconAdapter

        }
        var colorRecyclerList = binding.rvHomeCateColor.apply{
            setHasFixedSize(true)
            layoutManager = colorListManager
            adapter = colorAdapter
        }

        binding.ivHomeCateIcon.setOnClickListener {
            if(binding.rvHomeCateIcon.isVisible){
                binding.rvHomeCateIcon.isGone = true
            }
            else {
                binding.rvHomeCateIcon.isVisible = true
            }
        }

        binding.ivHomeCateColor.setOnClickListener{
            if(binding.rvHomeCateColor.isVisible){
                binding.rvHomeCateColor.isGone = true
            }
            else {
                binding.rvHomeCateColor.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.btnHomeTimeEditDelete.isGone = true
    }

    private fun initArrayList(){
        with(cateIconArray){
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_meal1))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_meal2))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_chat1))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_health))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_phone))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_rest))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_heart))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_study))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_laptop))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_music))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_lightup))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_lightout))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_pen))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_burn))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_plan))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_work))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_mic))
            cateIconArray.add(resources.getDrawable(R.drawable.ic_home_cate_sony))
        }
    }

    private fun initColorArray(){
        with(cateColorArray){
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub5))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.main))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub4))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub6))
            cateColorArray.add(Color.parseColor("#FDA4B4"))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub3))
            cateColorArray.add(Color.parseColor("#D4ECF1"))
            cateColorArray.add(Color.parseColor("#7FC7D4"))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.point_main))
            cateColorArray.add(Color.parseColor("#FDF3CF"))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub1))
            cateColorArray.add(resources.getColor(com.example.myapplication.R.color.sub2))

        }
    }

    private fun customBackDialog(){
        backDialog = HomeBackCustomDialog(requireActivity(), this)
        backDialog.show()
    }

    private fun customDeleteDialog(){
        deleteDialog = HomeDeleteCustomDialog(requireActivity(), this)
        deleteDialog.show()
    }

    // 커스텀 다이얼로그에서 버튼 클릭 시
    override fun onYesButtonClicked(dialog : Dialog) {
        Navigation.findNavController(requireView()).navigate(R.id.action_categoryAddFragment_to_homeCategoryFragment)
        dialog.dismiss()
    }

    override fun onNoButtonClicked(dialog : Dialog) {
        dialog.dismiss()
    }

}