package com.mada.myapplication.MyFunction.Fragment

import android.content.Context
import android.content.Intent
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
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.MyWithdraw1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyWithdrawFragment : Fragment() {
    private lateinit var binding: MyWithdraw1Binding
    lateinit var navController: NavController
    val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    val token = Splash2Activity.prefs.getString("token", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyWithdraw1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myWithdrawFragment_to_fragMy)
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // 체크박스가 체크되었을 때
                binding.myWithdrawBtn.setBackgroundResource(R.drawable.my_withdraw_btn_ok)

                binding.myWithdrawBtn.setOnClickListener {
                    api.withdraw(token).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Log.d("회원탈퇴 성공", response.body().toString())
                                // 캐시 매니저를 가져오기
                                clearAppData(requireContext())
                                // SharedPreferences 초기화
                                val intent = Intent(requireContext(), Splash2Activity::class.java)
                                if (intent != null) {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    requireContext().startActivity(intent)
                                    Runtime.getRuntime().exit(0)
                                }
                                navController.navigate(R.id.action_myWithdrawFragment_to_splash2Activity)
                                Toast.makeText(requireContext(), "회원탈퇴 되었습니다", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Log.d("회원탈퇴 실패", response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("회원탈퇴 실패 ", "서버 오류")
                        }
                    })
                }

            } else {
                // 체크박스가 체크 해제되었을 때
                binding.myWithdrawBtn.setBackgroundResource(R.drawable.my_withdraw_btn_nomal)
                binding.myWithdrawBtn.setOnClickListener {
                    Toast.makeText(requireContext(), "회원탈퇴를 진행하려면 체크박스에 체크해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
