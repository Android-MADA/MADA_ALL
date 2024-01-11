package com.mada.myapplication.HomeFunction.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mada.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RepeatBottomSheetDialog(val itemClick : (Int) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.repeat_menu_bottomsheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.findViewById<LinearLayout>(R.id.repeat_menu_delete_layout)?.setOnClickListener {
            //삭제
            itemClick(0)
            dialog?.dismiss()
        }

        view?.findViewById<LinearLayout>(R.id.repeat_menu_edit_layout)?.setOnClickListener {
            //수정
            itemClick(1)
            dialog?.dismiss()
        }

    }
}