package com.example.myapplication.HomeFunction.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.HomeFunction.adapter.category.CateListAdapter
import com.example.myapplication.R
import com.example.myapplication.HomeFunction.adapter.category.HomeCategoryAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.databinding.HomeFragmentCategoryBinding
import com.example.myapplication.db.entity.CateEntity
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.readActiveCate(false)
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            Log.d("viewmodel", it.toString())

            val cateList = it as List<CateEntity>

            val cateRv = binding.rvHomeCategory
            val cateAdapter = CateListAdapter()
            cateAdapter.submitList(cateList)
            cateAdapter.setItemClickListener(object : CateListAdapter.OnItemClickListener{
                override fun onClick(v: View, dataSet: CateEntity) {

                    viewModel._cate.value = dataSet

                    val bundle = Bundle()
                    bundle.putStringArrayList("key", arrayListOf(
                        dataSet.id.toString(),
                        dataSet.categoryName,
                        dataSet.color,
                        dataSet.iconId.toString(),
                        "active"
                    ))

                    Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment, bundle)
                }

            })
            //item 클릭 시 수정으로 이동 코드 작성
            cateRv.adapter = cateAdapter
            cateRv.layoutManager = LinearLayoutManager(this.requireActivity())
        })

        viewModel.readQuitCate(true)
        viewModel.quitCateEntityList.observe(viewLifecycleOwner, Observer {
            //rv에 연결
            val quitCateList = it as List<CateEntity>
            val quitCateRv = binding.rvHomeCategoryQuit
            val quitCateAdapter = CateListAdapter()
            quitCateAdapter.submitList(quitCateList)
            quitCateAdapter.setItemClickListener(object : CateListAdapter.OnItemClickListener{
                override fun onClick(v: View, dataSet: CateEntity) {

                    viewModel._cate.value = dataSet

                    val bundle = Bundle()
                    bundle.putStringArrayList("key", arrayListOf(
                        dataSet.id.toString(),
                        dataSet.categoryName,
                        dataSet.color,
                        dataSet.iconId.toString(),
                        "inactive"
                    ))

                    Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment, bundle)
                }

            })
            quitCateRv.adapter = quitCateAdapter
            quitCateRv.layoutManager = LinearLayoutManager(this.requireActivity())
        })

        binding.ivHomeCategoryBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_fragHome)
            bottomFlag = false
        }

        binding.btnHomeCategory.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment)
        }

    }

    override fun onResume() {
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }


}