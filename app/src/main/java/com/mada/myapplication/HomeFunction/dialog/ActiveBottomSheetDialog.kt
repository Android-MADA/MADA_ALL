package com.mada.myapplication.HomeFunction.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mada.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActiveBottomSheetDialog(val itemClick : (Int) -> Unit) : BottomSheetDialogFragment() {

    var flag = "active"

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = if(flag == "active"){
        inflater.inflate(R.layout.active_menu_bottomsheet_dialog, container, false)
    }
    else{
        inflater.inflate(R.layout.inactive_menu_bottomsheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(flag == "active"){
            view.findViewById<LinearLayout>(R.id.active_menu_quit_layout).setOnClickListener {
                itemClick(0)
                dialog?.dismiss()
            }
            view.findViewById<LinearLayout>(R.id.active_menu_delete_layout).setOnClickListener {
                itemClick(1)
                dialog?.dismiss()
            }
        }
        else {
            view.findViewById<LinearLayout>(R.id.inactive_menu_active_layout).setOnClickListener {
                itemClick(2)
                dialog?.dismiss()
            }
            view.findViewById<LinearLayout>(R.id.inactive_menu_delete_layout).setOnClickListener {
                itemClick(1)
                dialog?.dismiss()
            }
        }

    }
}