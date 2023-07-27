package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MyFuction.MyListAdapter
import com.example.myapplication.MyFuction.MyListItem
import com.example.myapplication.databinding.FragMyBinding

class FragMy : Fragment() {

    private lateinit var binding: FragMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMyBinding.inflate(inflater, container, false)

        binding.myRecordBtn.setOnClickListener{
            Toast.makeText(this.context, "[내 기록 확인하기]", Toast.LENGTH_SHORT).show()
        }

        // 리스트
        val MyList = arrayListOf(
            MyListItem("프로필 편집"),
            MyListItem("화면 설정"),
            MyListItem("알림"),
            MyListItem("공지사항"),
            MyListItem("Premium 구독"),
            MyListItem("로그아웃"),
            MyListItem("회원 탈퇴"),
        )

        binding.rvMyitem.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
        binding.rvMyitem.setHasFixedSize(true)
        binding.rvMyitem.adapter = MyListAdapter(MyList)

        return binding.root

    }

}