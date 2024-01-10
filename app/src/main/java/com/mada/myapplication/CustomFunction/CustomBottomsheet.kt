package com.mada.myapplication

class CustomBottomsheet {
    /* 오류로 인해 잠시 뺴둠(버루)
    var bottomsheet = BottomSheetBehavior.from(custom_bottomsheet)
    bottomsheet.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
        override fun onStateChanged(bottomsheet: View, newState: Int) {
            // BottomSheetBehavior의 5가지 상태
            when(newState) {

                // 사용자가 BottomSheet를 위나 아래로 드래그 중인 상태
                BottomSheetBehavior.STATE_DRAGGING -> { }

                // 드래그 동작 후 BottomSheet가 특정 높이로 고정될 때의 상태
                // SETTLING 후 EXPANDED, SETTLING 후 COLLAPSED, SETTLING 후 HIDDEN
                BottomSheetBehavior.STATE_SETTLING -> { }

                // 최대 높이로 보이는 상태
                BottomSheetBehavior.STATE_EXPANDED -> { }

                // peek 높이 만큼 보이는 상태
                BottomSheetBehavior.STATE_COLLAPSED -> { }

                // 숨김 상태
                BottomSheetBehavior.STATE_HIDDEN -> { }
            }
        }

        override fun onSlide(p0: View, slideOffset: Float) {
            // slideOffset 범위: -1.0 ~ 1.0
            // -1.0 HIDDEN, 0.0 COLLAPSED, 1.0 EXPANDED
        }
    })
    */


    /* 탭레이아웃 전환 코드
    customtabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

        // 탭 버튼을 선택할 때 이벤트
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val transaction = supportFragmentManager.beginTransaction()
            when(tab?.text) {
                "tab1" -> transaction.replace(R.id.custom_colortable, custom_color() )
                "tab2" -> transaction.replace(R.id.custom_clothtable, custom_cloth() )
                "tab3" -> transaction.replace(R.id.custom_itemtable, custom_item() )
                "tab4" -> transaction.replace(R.id.custom_backgroundtable, custom_background() )
            }
            transaction.commit()
        }

        // 다른 탭 버튼을 눌러 선택된 탭 버튼이 해제될 때 이벤트
        override fun onTabUnselected(tab: TabLayout.Tab?) {
            TODO("Not yet implemented")
        }

        // 선택된 탭 버튼을 다시 선택할 때 이벤
        override fun onTabReselected(tab: TabLayout.Tab?) {
            TODO("Not yet implemented")
        }
    })
}
*/
}