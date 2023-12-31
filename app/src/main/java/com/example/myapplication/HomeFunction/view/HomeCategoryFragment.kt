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
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.databinding.CategoryLayoutBinding
import com.example.myapplication.databinding.HomeFragmentCategoryBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.hideBottomNavigation

class HomeCategoryFragment : Fragment() {

    lateinit var binding : CategoryLayoutBinding
    private val viewModel : HomeViewModel by activityViewModels()

    private var bottomFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_category, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.category_layout, container, false)
        hideBottomNavigation(bottomFlag, activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //리스트에 카테고리 넣기
        viewModel.readActiveCate(false)

        //livedata로 감지
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            Log.d("viewmodel", it.toString())

            val cateList = it as List<CateEntity>

            //val cateRv = binding.rvHomeCategory
            val cateRv = binding.categoryActiveRv
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
            //val quitCateRv = binding.rvHomeCategoryQuit
            val quitCateRv = binding.categoryInactiveRv
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

        //뒤로가기 버튼 클릭리스너
        binding.categoryBackTv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_fragHome)
            bottomFlag = false
        }

        //카테고리 추가 버튼 클리리스너
        binding.categoryAddIv.setOnClickListener {
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