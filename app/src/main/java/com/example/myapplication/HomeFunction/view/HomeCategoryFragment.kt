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
import com.example.myapplication.HomeFunction.Model.Category
import com.example.myapplication.R
import com.example.myapplication.HomeFunction.adapter.category.HomeCategoryAdapter
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.MyFuction.Model.MySettingData2
import com.example.myapplication.MyFuction.Model.MySettingData3
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.databinding.HomeFragmentCategoryBinding
import com.example.myapplication.hideBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val myApi = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)

        myApi.mySetPage(viewModel.userToken, MySettingData2(true, true, true)).enqueue(object : Callback<MySettingData3>{
            override fun onResponse(
                call: Call<MySettingData3>,
                response: Response<MySettingData3>
            ) {
                if(response.isSuccessful){
                    Log.d("myPost성공", response.body().toString())
                }
                else{
                    Log.d("myPost 안드 잘못", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MySettingData3>, t: Throwable) {
                Log.d("myPost실패", "서버 ㅇ녀결 실패")
            }

        })

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
            else {
                cateAdapter = null
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