package com.mada.myapplication.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
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
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MainActivity
import com.mada.myapplication.MyFunction.Data.FragMyData
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.FragMyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class FragMy : Fragment() {

    private lateinit var binding: FragMyBinding
    lateinit var navController: NavController
    private lateinit var alertDialog: AlertDialog
    lateinit var mAdView : AdView
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")
    //val mainActivity = requireActivity() as MainActivity
    //private val viewModel: HomeViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMyBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isGone = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("token:",token)

        navController = view.findNavController()

        setupAdView()
        setPage()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_fragHome)
        }
        binding.myStampBtn.setOnClickListener {
            //navController.navigate(R.id.action_fragMy_to_myStampFragment)
            // 서버 되기 전까지 기능 막아두기
            Toast.makeText(requireContext(), "아직 준비 중인 기능입니다.", Toast.LENGTH_SHORT).show()
        }

        // 바텀 시트 항목 선택 리스너
        binding.myEditProfile.setOnClickListener {
            navController.navigate(R.id.action_fragMy_to_myProfileFragment)
        }
//        binding.mySetPage.setOnClickListener {
//            navController.navigate(R.id.action_fragMy_to_mySetFragment)
//        }
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

    // 페이지 설정
    private fun setPage(){
        api.selectfragMy(token).enqueue(object : retrofit2.Callback<FragMyData> {
            override fun onResponse(
                call: Call<FragMyData>,
                response: Response<FragMyData>
            ) {
                val responseCode = response.code()
                Log.d("selectfragMy", "Response Code: $responseCode")

                if (response.isSuccessful) {
                    Log.d("selectfragMy 성공", response.body().toString())

                    // 닉네임 제공
                    binding.myNickname.text = "안녕하세요, "+ response.body()!!.data.nickname +"님!"

                    // 유저타입 제공
                    if (response.body()!!.data.subscribe == true) {
                        binding.userType.text = "프리미엄 유저"
                        binding.userType.setTextColor(getResources().getColor(R.color.point_main))
                        binding.adView.removeAllViews()
                    } else {
                        binding.userType.text = "일반 유저"
                    }
                } else {
                    Log.d("selectfragMy 실패", response.body().toString())
                }
            }
            override fun onFailure(call: Call<FragMyData>, t: Throwable) {
                Log.d("서버 오류", "selectfragMy 실패")
            }
        })
    }
    // 배너 광고
    private fun setupAdView() {
        val mainActivity = requireActivity() as MainActivity
        if(mainActivity.getPremium()) {
        } else {
            MobileAds.initialize(this.requireContext()) {}
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
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
        }
        btnNo.setOnClickListener {
            alertDialog.dismiss()
            // Handle "No" button click if needed
        }
    }
    // 앱 데이터 비우기
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
    // 디렉토리 삭제
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