package com.mada.myapplication.StartFunction

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mada.myapplication.MainActivity
import com.mada.myapplication.PreferenceUtil
import com.mada.myapplication.R
import com.mada.myapplication.databinding.Splash2Binding

class Splash2Activity : AppCompatActivity() {
    companion object {
        lateinit var prefs: PreferenceUtil

    }

    private lateinit var binding: Splash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Splash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceUtil(this)


        val pref = getSharedPreferences("login", Activity.MODE_PRIVATE)
        val login = pref.getBoolean("login", false)
        binding.kakaoBtn.setOnClickListener{
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            //finish()
        }

        binding.naverBtn.setOnClickListener{
           /* val mDialogView = LayoutInflater.from(this).inflate(R.layout.update_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .create()

            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            mBuilder.show()

            //팝업 사이즈 조절
            DisplayMetrics()
            this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            val display = (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            display.getSize(size)
            val screenWidth = size.x
            val popupWidth = (screenWidth * 0.8).toInt()
            mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)


            //팝업 타이틀 설정, 버튼 작용 시스템
            mDialogView.findViewById<TextView>(R.id.textTitle).text = "출시 준비 중입니다";
            mDialogView.findViewById<TextView>(R.id.yesBtnText).setOnClickListener( {
                finish();
            })*/
            val intent = Intent(this, MyWebviewActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.googleBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        if(login) binding.naverBtn.callOnClick()



    }

}