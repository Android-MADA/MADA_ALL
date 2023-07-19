package com.example.myapplication

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

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
}