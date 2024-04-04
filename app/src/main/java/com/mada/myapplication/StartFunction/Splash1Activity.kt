package com.mada.myapplication.StartFunction

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mada.myapplication.R

class Splash1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash1)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        var update = true

        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.d("application versin", appUpdateInfo.toString())
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE)) {
                // 유효한 업데이트가 있을 때
                // ex. 스토어에서 앱 업데이트하기
                update = false

            }
        }
        // 3초 후 화면 자동 전환하기 위한 핸들러 생성
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if(update) {
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.calendar_add_popup_one, null)
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
                mDialogView.findViewById<TextView>(R.id.textTitle).text = title
                mDialogView.findViewById<TextView>(R.id.popone_desc).visibility = View.GONE
                mDialogView.findViewById<TextView>(R.id.yesBtnText).setOnClickListener( {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://details?id=$packageName")
                    startActivity(intent)
                })

            }
        }
        handler.postDelayed(runnable, 2500)
    }
}


