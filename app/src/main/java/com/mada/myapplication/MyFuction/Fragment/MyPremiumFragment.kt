package com.mada.myapplication.MyFuction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MyFuction.Data.MyPremiumData
import com.mada.myapplication.MyFuction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFuction.Splash2Activity
import com.mada.myapplication.databinding.MyPremiumBinding
import retrofit2.Call
import retrofit2.Response

class MyPremiumFragment : Fragment() {

    private lateinit var binding: MyPremiumBinding
    lateinit var navController: NavController
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myPremiumFragment_to_fragMy)
        }

        binding.myPremiumBtn.setOnClickListener {
            navController.navigate(R.id.action_myPremiumFragment_to_fragMy)
            Toast.makeText(context, "결제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun patchPremiumSetting(isSetting: Boolean) {
        api.mySetPremium(token, isSetting).enqueue(object : retrofit2.Callback<MyPremiumData> {
            override fun onResponse(
                call: Call<MyPremiumData>, response: Response<MyPremiumData>
            ) {
                val responseCode = response.code()

                if (response.isSuccessful) {
                    Log.d("프리미엄 변경 성공, ${response.body()?.is_subscribe}", "Response Code: $responseCode")
                } else {
                    Log.d("프리미엄 변경 실패", "Response Code: $responseCode")
                }
            }

            override fun onFailure(call: Call<MyPremiumData>, t: Throwable) {
                Log.d("서버 오류", "프리미엄 변경 실패")
            }
        })
    }
}
