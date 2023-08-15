package com.example.myapplication.HomeFunction

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.NoticeHomeBackBinding
import com.example.myapplication.databinding.NoticeHomeDeleteBinding

class HomeBackCustomDialog(context: Context, private val listener: HomeCustomDialogListener): Dialog(context) {

    private lateinit var binding : NoticeHomeBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notice_home_back, null, false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(binding.root)

        binding.apply {
            // 버튼 클릭 시 리스너 메소드 호출
            btnHomeDialogBackNo.setOnClickListener {
                listener.onNoButtonClicked(this@HomeBackCustomDialog)
            }
            btnHomeDialogBackYes.setOnClickListener {
                listener.onYesButtonClicked(this@HomeBackCustomDialog, "back")
            }
        }
    }
}

class HomeDeleteCustomDialog(context: Context, private val listener: HomeCustomDialogListener): Dialog(context) {

    private lateinit var binding : NoticeHomeDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notice_home_delete, null, false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(binding.root)

        binding.apply {
            // 버튼 클릭 시 리스너 메소드 호출
            btnHomeDialogBackNo.setOnClickListener {
                listener.onNoButtonClicked(this@HomeDeleteCustomDialog)
            }
            btnHomeDialogBackYes.setOnClickListener {
                listener.onYesButtonClicked(this@HomeDeleteCustomDialog, "delete")
            }
        }
    }
}