package com.example.myapplication.MyFuction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.myapplication.CalenderFuntion.Model.nickName
import com.example.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.example.myapplication.R
import com.example.myapplication.Splash2Activity
import com.example.myapplication.databinding.MySignup1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MySignup1Activity : AppCompatActivity() {

    private lateinit var binding: MySignup1Binding
    private var isButtonClicked = false

    // retrofit
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySignup1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        token = Splash2Activity.prefs.getString("token","")
        binding.signup1Btn.setOnClickListener {
            isButtonClicked = !isButtonClicked
            binding.signup1Btn.setBackgroundResource(R.drawable.my_btn_ok)
            if(binding.textId.text.toString() == "") {
                isButtonClicked = !isButtonClicked
                binding.signup1Btn.setBackgroundResource(R.drawable.my_btn_nomal)
            } else {
                signUpId(binding.textId.text.toString())

            }

        }
    }
    private fun signUpId(id : String) {
        val retrofit = Retrofit.Builder().baseUrl("http://www.madaumc.store/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(RetrofitServiceCalendar::class.java)
        token = Splash2Activity.prefs.getString("token","")
        val call = service.singup(token, nickName(id))
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val intent = Intent(this@MySignup1Activity, MySignup2Activity::class.java)
                startActivity(intent)
                finish()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

}

