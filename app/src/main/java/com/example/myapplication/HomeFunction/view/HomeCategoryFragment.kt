package com.example.myapplication.HomeFunction.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.R
import com.example.myapplication.HomeFunction.adapter.category.HomeCategoryAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.databinding.HomeFragmentCategoryBinding
import com.example.myapplication.hideBottomNavigation

class HomeCategoryFragment : Fragment() {

    lateinit var binding : HomeFragmentCategoryBinding
    private val viewModel : HomeViewModel by activityViewModels()

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

        val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyUldNdDc0LVN2aUljMnh6SE5pQXJQNzZwRnB5clNaXzgybWJNMTJPR000IiwiYXV0aG9yaXR5IjoiVVNFUiIsImlhdCI6MTY5MjE2MDU5NCwiZXhwIjoxNjkyMTk2NTk0fQ.MSGHGd8vZ19dYAvtHqt37nq6nGVbdaD9poiAa000-PTvlDvH1b9oii9oX1rY8vGQFQ5zYYN1eocgOXyWUEOGPA"
        //viewModel.getCategory(token)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = viewModel.categoryList.value?.let { HomeCategoryAdapter(it) }

        binding.ivHomeCategoryBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_fragHome)
            bottomFlag = false
        }

        binding.btnHomeCategory.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment)
        }

        val bundle = Bundle()

        categoryAdapter?.setItemClickListener(object: HomeCategoryAdapter.OnItemClickListener{
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

        binding.rvHomeCategory.adapter = categoryAdapter
        binding.rvHomeCategory.layoutManager = LinearLayoutManager(this.activity)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }
}