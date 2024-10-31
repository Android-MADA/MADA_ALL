package com.mada.reapp.HomeFunction.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mada.reapp.HomeFunction.adapter.category.CateListAdapter
import com.mada.reapp.HomeFunction.api.HomeApi
import com.mada.reapp.HomeFunction.api.RetrofitInstance
import com.mada.reapp.R
import com.mada.reapp.HomeFunction.viewModel.HomeViewModel
import com.mada.reapp.MainActivity
import com.mada.reapp.databinding.CategoryLayoutBinding
import com.mada.reapp.db.entity.CateEntity
import com.mada.reapp.getAllCategory
import com.mada.reapp.getDescribe
import com.mada.reapp.hideBottomNavigation

class HomeCategoryFragment : Fragment() {

    lateinit var binding : CategoryLayoutBinding
    private val viewModel : HomeViewModel by activityViewModels()

    private var bottomFlag = true
    private val api = RetrofitInstance.getInstance().create(HomeApi::class.java)
    private var activeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.category_layout, container, false)
        hideBottomNavigation(bottomFlag, activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /**
         * 1. 카테고리 서버 연결
         */
        getAllCategory(api, viewModel, this.requireActivity())


        /**
         * 리스트에 카테고리 넣기
         */
        Log.d("Category", "read start")
        viewModel.readActiveCate(false)
        Log.d("Category", "read fin")

        //livedata로 감지
        val mainActivity = requireActivity() as MainActivity
        viewModel.cateEntityList.observe(viewLifecycleOwner, Observer {
            Log.d("Category", it.toString())
            val cateList = it as List<CateEntity>

            viewModel.activeNum = cateList.size
            activeCount = cateList.size

            if(false && activeCount >= 5) { //!mainActivity.getPremium() 프리미엄 여부는 무조건 프리미엄으로
                binding.categoryAddTv.isVisible = true
                binding.categoryAddIv.isGone = true
            } else {
                binding.categoryAddTv.isGone = true
                binding.categoryAddIv.isVisible = true
            }

            val cateRv = binding.categoryActiveRv
            val cateAdapter = CateListAdapter()
            cateAdapter.submitList(cateList)
            cateAdapter.setItemClickListener(object : CateListAdapter.OnItemClickListener{
                override fun onClick(v: View, dataSet: CateEntity) {

                    val buffering = viewModel.setPopupBufferingTodo(requireContext())
                    viewModel._cate.value = dataSet

                    val bundle = Bundle()
                    bundle.putStringArrayList("key", arrayListOf(
                        dataSet.id.toString(),
                        dataSet.categoryName,
                        dataSet.color,
                        dataSet.iconId.toString(),
                        "active"
                    ))
                    buffering.dismiss()
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
                    val buffering = viewModel.setPopupBufferingTodo(requireContext())
                    viewModel._cate.value = dataSet

                    val bundle = Bundle()
                    bundle.putStringArrayList("key", arrayListOf(
                        dataSet.id.toString(),
                        dataSet.categoryName,
                        dataSet.color,
                        dataSet.iconId.toString(),
                        "inactive"
                    ))
                    buffering.dismiss()
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
            val buffering = viewModel.setPopupBufferingTodo(requireContext())
            Navigation.findNavController(view).navigate(R.id.action_homeCategoryFragment_to_categoryAddFragment)
            buffering.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "running")
        getAllCategory(api, viewModel, this.requireActivity())
        viewModel.readActiveCate(false)
        viewModel.readQuitCate(true)
        getDescribe(viewModel, requireContext())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(bottomFlag, activity)
    }


}