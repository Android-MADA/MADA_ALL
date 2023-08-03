package com.example.myapplication.Fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyFuction.MyListAdapter
import com.example.myapplication.MyFuction.MyListItem
import com.example.myapplication.MyFuction.MyNoticeActivity
import com.example.myapplication.MyFuction.MyPremiumActivity
import com.example.myapplication.MyFuction.MyProfileActivity
import com.example.myapplication.MyFuction.MySetActivity
import com.example.myapplication.MyFuction.MyWithdraw1Activity
import com.example.myapplication.Splash2Activity
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
            MyListItem("프로필 편집",  MyProfileActivity::class.java),
            MyListItem("화면 설정", MySetActivity::class.java),
            MyListItem("알림", null),
            MyListItem("공지사항", MyNoticeActivity::class.java),
            MyListItem("Premium 구독", MyPremiumActivity::class.java),
            MyListItem("로그아웃", Splash2Activity::class.java),
            MyListItem("회원 탈퇴", MyWithdraw1Activity::class.java),
        )

        binding.rvMyitem.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
        binding.rvMyitem.setHasFixedSize(true)
        binding.rvMyitem.adapter = MyListAdapter(MyList)

        // 구분선 커스텀
        val dividerItemDecoration = object : DividerItemDecoration(binding.rvMyitem.getContext(),
            LinearLayoutManager(this.context).orientation) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = MyList.size

                // 마지막 아이템이 아닌 경우에만 구분선 추가
                if (position < itemCount - 1) {
                    super.getItemOffsets(outRect, view, parent, state)
                } else {
                    outRect.setEmpty() // 마지막 아이템인 경우 구분선 간격을 0으로 설정하여 표시하지 않음 (적용되지 않음, 일단 보류)
                }
            }
        }
        binding.rvMyitem.addItemDecoration(dividerItemDecoration)

        return binding.root

    }

}