package com.example.myapplication.HomeFunction.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.HomeBackCustomDialog
import com.example.myapplication.HomeFunction.HomeCustomDialogListener
import com.example.myapplication.HomeFunction.HomeDeleteCustomDialog
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.HomeFunction.Model.Todo
import com.example.myapplication.R
import com.example.myapplication.HomeFunction.adapter.category.HomeCategoryAdapter
import com.example.myapplication.HomeFunction.adapter.todo.HomeViewpager2CategoryAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.databinding.HomeFragmentCategoryBinding
import com.example.myapplication.hideBottomNavigation

class HomeCategoryFragment : Fragment() {

    lateinit var binding : HomeFragmentCategoryBinding
    private val viewModel : HomeViewModel by activityViewModels()
    var cateAdapter: HomeCategoryAdapter? = null

    private var bottomFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_category, container, false)
        hideBottomNavigation(bottomFlag, activity)

        //서버연결 - 카테고리 리스트 조회 후 리스트에 넣어서 어댑터에 연결
         viewModel.getCategory(viewModel.userToken)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = Bundle()


        viewModel.getCategory(viewModel.userToken)

        viewModel.categoryList.observe(viewLifecycleOwner, Observer {
            if (viewModel.categoryList.value?.isNotEmpty() == true) {
                //rv연결
                cateAdapter = HomeCategoryAdapter()
                cateAdapter!!.dataSet = viewModel.categoryList.value!!

                cateAdapter!!.setItemClickListener(object: HomeCategoryAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int, dataSet: Category) {
                        //페이지 이동 + 데이터 전달
                        bundle.putStringArrayList("key", arrayListOf(
                            dataSet.id.toString(),
                            dataSet.categoryName,
                            dataSet.iconId.toString(),
                            dataSet.color,
                            position.toString()
                        ))
                        Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment, bundle)

                    }
                })

                binding.rvHomeCategory.adapter = cateAdapter
                binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
            }
        })

        binding.ivHomeCategoryBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_fragHome)
            bottomFlag = false
        }

        binding.btnHomeCategory.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment)
        }



        if (viewModel.categoryList.value?.isNotEmpty() == true) {
            //rv연결
            cateAdapter = HomeCategoryAdapter()
            cateAdapter!!.dataSet = viewModel.categoryList.value!!

            cateAdapter!!.setItemClickListener(object: HomeCategoryAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int, dataSet: Category) {
                    //페이지 이동 + 데이터 전달
                    bundle.putStringArrayList("key", arrayListOf(
                        dataSet.id.toString(),
                        dataSet.categoryName,
                        dataSet.iconId.toString(),
                        dataSet.color,
                        position.toString()
                    ))
                    Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment, bundle)

                }
            })

            binding.rvHomeCategory.adapter = cateAdapter
            binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)
        }
        //

    }

    override fun onResume() {
        super.onResume()
        viewModel.getCategory(viewModel.userToken)
        cateAdapter?.notifyDataSetChanged()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }


}