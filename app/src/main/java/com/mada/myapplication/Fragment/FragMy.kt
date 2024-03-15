package com.mada.myapplication.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
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
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.myapplication.CalenderFunction.Model.CalendarViewModel
import com.example.myapplication.HomeFunction.api.RetrofitInstance
import com.example.myapplication.MainActivity
import com.example.myapplication.MyFunction.Data.FragMyData
import com.example.myapplication.MyFunction.RetrofitServiceMy
import com.example.myapplication.R
import com.example.myapplication.StartFunction.Splash2Activity
import com.example.myapplication.databinding.FragMyBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class FragMy : Fragment() {

    private lateinit var binding: FragMyBinding
    lateinit var navController: NavController
    private lateinit var alertDialog: AlertDialog
    private val CalendarViewModel : CalendarViewModel by activityViewModels()
    lateinit var mAdView : AdView

    //서버연결 시작
    val retrofit = Retrofit.Builder().baseUrl("http://15.165.210.13:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    val mainActivity = requireActivity() as MainActivity

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMyBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isGone = true

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

                    if (response.body()!!.data.subscribe == true) {binding.userType.text = "프리미엄 유저"}
                    else { binding.userType.text = "일반 유저" }

                    binding.myNickname.text = "안녕하세요, "+"${response.body()!!.data.nickname}"+"님!"
                } else {
                    Log.d("selectfragMy 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<FragMyData>, t: Throwable) {
                Log.d("서버 오류", "selectfragMy 실패")
            }
        })

        //구글 플레이스토어 광고
        MobileAds.initialize(this.requireContext()) {}
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        // 프리미엄 시 마이 화면 설정 (마스터 합치면 활성화)
//        if(mainActivity.getPremium()){
//            binding.userType.text = "프리미엄 유저"
//            binding.userType.setTextColor(getResources().getColor(R.color.point_main))
//            binding.adView.removeAllViews()
//        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_fragHome)
        }

        binding.myStampBtn.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myStampFragment)
        }

        // 바텀 시트 항목 선택 리스너
        binding.myEditProfile.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myProfileFragment)
        }
        binding.mySetPage.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_mySetFragment)
        }
        binding.myNotice.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myNoticeFragment)
        }
        binding.myPremium.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myPremiumFragment)
        }
        binding.myKakao.setOnClickListener {
            try {
                val kakaoIntent = requireActivity().packageManager.getLaunchIntentForPackage("com.kakao.talk")
                kakaoIntent?.let {
                    kakaoIntent.action = Intent.ACTION_VIEW
                    kakaoIntent.data = Uri.parse("https://pf.kakao.com/_hwCiG/chat")
                    startActivity(kakaoIntent)
                } ?: run {
                    // 카카오톡 앱이 설치되어 있지 않은 경우에 대한 처리
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pf.kakao.com/_hwCiG/chat"))
                    startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.myLogout.setOnClickListener {
            showLogoutPopup()
        }
        binding.myWithdraw.setOnClickListener{
            navController.navigate(R.id.action_fragMy_to_myWithdrawFragment)
        }

        // 시스템 뒤로가기
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                navController.navigate(R.id.action_fragMy_to_fragHome)
                return@OnKeyListener true
            }
            false
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
        alertDialog.show()
        val displayMetrics = DisplayMetrics()
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(size)
        val screenWidth = size.x
        val popupWidth = (screenWidth * 0.8).toInt()
        alertDialog?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        // 버튼 클릭 리스너
        btnYes.setOnClickListener {
            // 서버 연결
            val call = api.logout(token)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("로그아웃 성공", response.body().toString())
                        alertDialog.dismiss()
                        // 캐시 매니저를 가져오기
                        clearAppData(requireContext())
                        // SharedPreferences 초기화
                        val intent = Intent(requireContext(), Splash2Activity::class.java)
                        if(intent !=null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            requireContext().startActivity(intent)
                            Runtime.getRuntime().exit(0)
                        }
                        ///navController.navigate(R.id.action_fragMy_to_splash2Activity)
                        Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("로그아웃 실패", response.body().toString())
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("로그아웃 실패 ", "서버 오류")
                }
            })

            // 뷰 설정

        }
        btnNo.setOnClickListener {
            alertDialog.dismiss()
            // Handle "No" button click if needed
        }

        // 뷰 사이즈 조절


        // 호출

    }
    fun clearAppData(context: Context) {
        val cache: File = context.cacheDir // 캐시 폴더 호출
        val appDir = File(cache.parent) // App Data 삭제를 위해 캐시 폴더의 부모 폴더까지 호출
        if (appDir.exists()) {
            val children: Array<String> = appDir.list() ?: return
            for (s in children) {
                // App Data 폴더의 리스트를 deleteDir 를 통해 하위 디렉토리 삭제
                if(!s.equals("lib")&&!s.equals("files"))
                    deleteDir(File(appDir, s))
            }
        }
    }

    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children: Array<String> = dir.list() ?: return false

            // 파일 리스트를 반복문으로 호출
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }

        // 디렉토리가 비어있거나 파일이므로 삭제 처리
        return dir.delete()
    }



}