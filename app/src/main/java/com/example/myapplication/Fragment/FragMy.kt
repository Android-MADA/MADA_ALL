package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.CalenderFuntion.Model.CharacterResponse
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MyFuction.Data.FragMyData
import com.example.myapplication.MyFuction.Activity.MyRecordDayActivity
import com.example.myapplication.MyFuction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFuction.Splash2Activity
import com.example.myapplication.databinding.FragMyBinding
import com.squareup.picasso.Picasso
import retrofit2.converter.gson.GsonConverterFactory


class FragMy : Fragment() {

    private lateinit var binding: FragMyBinding
    lateinit var navController: NavController
    private lateinit var alertDialog: AlertDialog

    //서버연결 시작
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")


    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMyBinding.inflate(inflater, container, false)

        // 서버 데이터 연결
        api.selectfragMy(token).enqueue(object : retrofit2.Callback<FragMyData> {
            override fun onResponse(
                call: Call<FragMyData>,
                response: Response<FragMyData>
            ) {
                val responseCode = response.code()
                Log.d("selectfragMy", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("selectfragMy 성공", response.body().toString())
                    if (response.body()!!.data.subscribe == true) binding.userType.text = "프리미엄 유저"
                    else binding.userType.text = "일반 유저"
                    binding.myNickname.text = "안녕하세요, "+"${response.body()!!.data.nickname}"+"님!"
                    binding.sayingContent.text = response.body()!!.data.saying[0].content
                    binding.sayingSayer.text = response.body()!!.data.saying[0].sayer
                } else {
                    Log.d("selectfragMy 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<FragMyData>, t: Throwable) {
                Log.d("서버 오류", "selectfragMy 실패")
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        // 캐릭터 커스텀 불러오는 함수 호출
        getCustomChar()
        // 스위치 색 설정 함수 호출
        setupSwitchColor(binding.myGoogleCalSwitch)

        // 바텀 시트 항목 선택 리스너
        binding.myEditProfile.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myProfileFragment)
        }
        binding.mySetPage.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_mySetFragment)
        }
        binding.mySetAlarm.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myAlarmFragment)
        }
        binding.myNotice.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myNoticeFragment)
        }
        binding.myGoogleCalSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "연동 완료", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "연동 해제", Toast.LENGTH_SHORT).show()
            }
        }
        binding.myPremium.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myPremiumFragment)
        }
        binding.myLogout.setOnClickListener {
            showLogoutPopup()
        }
        binding.myWithdraw.setOnClickListener{
            navController.navigate(R.id.action_fragMy_to_myWithdrawFragment)
        }

        // 내 기록 확인하기
        binding.myRecordBtn.setOnClickListener {
            val intent = Intent(requireContext(), MyRecordDayActivity::class.java)
            startActivity(intent)
        }


    }
    // 캐릭터 커스텀 불러오기
    private fun getCustomChar() {
        val call2 = api.characterRequest(token)
        call2.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call2: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val datas = apiResponse.data.datas
                        if(datas != null) {
                            for (data in datas) {
                                //arrays.add(data)
                                //Log.d("111","datas: ${data.id} ${data.itemType} ${data.filePath}")
                                if(data.itemType=="color") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.myRamdi)
                                } else if(data.itemType=="set") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.imgMyCloth)
                                } else if(data.itemType=="item") {
                                    Picasso.get()
                                        .load(data.filePath)
                                        .into(binding.imgMyItem)
                                }
                                // ...
                            }
                        } else {
                            //Log.d("2221","${response.code()}")
                        }
                    } else {
                        //Log.d("222","Request was not successful. Message: hi")
                    }
                } else {
                    //Log.d("3331","itemType: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                //Log.d("444","itemType: ${t.message}")
            }
        })
    }

    // 스위치 색 설정 함수
    private fun setupSwitchColor(mySwitch: SwitchCompat) {
        mySwitch.setOnCheckedChangeListener { _, isChecked ->
            val trackColor = if (isChecked) {
                ContextCompat.getColor(requireContext(), R.color.main)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grey5)
            }
            val thumbColor = if (isChecked) {
                ContextCompat.getColor(requireContext(),  R.color.sub4)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grey2)
            }
            mySwitch.trackDrawable?.setColorFilter(trackColor, PorterDuff.Mode.SRC_IN)
            mySwitch.thumbDrawable?.setColorFilter(thumbColor, PorterDuff.Mode.SRC_IN)
        }
    }



    // 로그아웃 팝업창 함수
    private fun showLogoutPopup() {

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.my_logout_popup, null)
        val btnNo = dialogView.findViewById<AppCompatButton>(R.id.nobutton)
        val btnYes = dialogView.findViewById<AppCompatButton>(R.id.yesbutton)

        alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // 버튼 클릭 리스너
        btnYes.setOnClickListener {

            // 서버 연결
            val call = api.logout(token)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("로그아웃 성공", response.body().toString())
                    } else {
                        Log.d("로그아웃 실패", response.body().toString())
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("로그아웃 실패 ", "서버 오류")
                }
            })

            // 뷰 설정
            alertDialog.dismiss()
            navController.navigate(R.id.action_fragMy_to_splash2Activity)

            Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
        }
        btnNo.setOnClickListener {
            alertDialog.dismiss()
            // Handle "No" button click if needed
        }

        // 뷰 사이즈 조절
        val displayMetrics = DisplayMetrics()
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        alertDialog?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        // 호출
        alertDialog.show()
    }
}