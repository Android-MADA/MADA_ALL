package com.example.myapplication.CalenderFuntion.Calendar

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.CalendarDdayFragment
import com.example.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.example.myapplication.HomeFunction.view.HomeViewpagerDdayFragment
import com.example.myapplication.R
import java.util.ArrayList

class DdayAdapter(val CalendarViewModel: CalendarViewModel, val parent2: CalendarDdayFragment)  : RecyclerView.Adapter<DdayAdapter.ItemViewHolder>()  {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dday1 : ImageView = itemView.findViewById(R.id.dday1)
        val ddayRemain : TextView = itemView.findViewById(R.id.dday_remain)
        val ddayText : TextView = itemView.findViewById(R.id.dday_text)
        val plus : ImageView = itemView.findViewById(R.id.plus)
        val day : ImageView = itemView.findViewById(R.id.plus)
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dday_layout,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = CalendarViewModel.ddayArrayList[position]
        if(item.what =="BLANK") {
            holder.dday1.visibility = View.GONE
            //holder.dday2.setImageResource(R.drawable.dday_blank)
            holder.plus.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("today",CalendarViewModel.todayDate())
                //Navigation.findNavController(holder.itemView).navigate(R.id.action_fragHome_to_ddayAddFragment,bundle)
            }
        } else {
            if(item.color == "#486DA3") {
                holder.dday1.setColorFilter(Color.parseColor("#486DA3"), PorterDuff.Mode.SRC_IN)
            } else if(item.color =="#F0768C") {
                holder.dday1.setColorFilter(Color.parseColor("#F0768C"), PorterDuff.Mode.SRC_IN)
            } else if(item.color == "#F8D141"){
                holder.dday1.setColorFilter(Color.parseColor("#F8D141"), PorterDuff.Mode.SRC_IN)
            }  else {
                holder.dday1.setColorFilter(Color.parseColor("#2AA1B7"), PorterDuff.Mode.SRC_IN)
            }
            holder.ddayRemain.text = "D-${CalendarViewModel.daysRemainingToDate(item.endDate)}"
            holder.ddayText.text = item.title
            holder.itemView.setOnClickListener {
                val mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.calendar_dday_popup_plus, null)
                mDialogView.findViewById<ImageView>(R.id.plus_up).setColorFilter(Color.parseColor(item.color))
                mDialogView.findViewById<TextView>(R.id.textTitle).text = item.title
                mDialogView.findViewById<TextView>(R.id.textDay).text = CalendarViewModel.convertToDate2(item.endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text = holder.ddayRemain.text.toString()
                mDialogView.findViewById<TextView>(R.id.textMemo).text = item.memo
                val mBuilder = AlertDialog.Builder(holder.itemView.context)
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("calData",item)
                    bundle.putBoolean("edit",true)
                    //Navigation.findNavController(holder.itemView).navigate(R.id.action_fragHome_to_ddayAddFragment,bundle)
                    mBuilder.dismiss()
                }
                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                    mBuilder.dismiss()
                    CalendarViewModel.deleteCalendar(item.id){ result ->
                        when (result) {
                            1 -> {
                                for(data in CalendarViewModel.ddayArrayList) {
                                    if(data.id == item.id) {
                                        CalendarViewModel.ddayArrayList.remove(data)
                                        break
                                    }
                                }
                                val formattedYear = item.endDate.substring(0,4).toInt()
                                val formattedMonth = item.endDate.substring(5,7).toInt()
                                val nextMonth: String

                                if (formattedMonth < 12) {
                                    nextMonth = "${formattedYear}-${formattedMonth + 1}"
                                } else {
                                    nextMonth = "${formattedYear + 1}-1"
                                }
                                val preMonth : String
                                if (formattedMonth >1) {
                                    preMonth = "${formattedYear}-${formattedMonth - 1}"
                                } else {
                                    preMonth = "${formattedYear - 1}-12"
                                }
                                Log.d("dsadasdsa",nextMonth)
                                var tmpArrayList = CalendarViewModel.hashMapArrayCal.get(nextMonth)
                                if(tmpArrayList!=null) {
                                    for(data in tmpArrayList) {
                                        if(data.id==item.id) {
                                            tmpArrayList.remove(data)
                                            break
                                        }
                                    }
                                }

                                tmpArrayList = CalendarViewModel.hashMapArrayCal.get(preMonth)
                                if(tmpArrayList!=null) {
                                    for(data in tmpArrayList) {
                                        if(data.id==item.id) {
                                            tmpArrayList.remove(data)
                                            break
                                        }
                                    }
                                }
                                tmpArrayList = CalendarViewModel.hashMapArrayCal.get("${formattedYear}-${formattedMonth}")
                                if(tmpArrayList!=null) {
                                    for(data in tmpArrayList) {
                                        if(data.id==item.id) {
                                            tmpArrayList.remove(data)
                                            break
                                        }
                                    }
                                }
                                parent2.updateDdayData()
                            }
                            2 -> {
                                Toast.makeText(holder.itemView.context, "삭제 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            holder.dday1.setOnClickListener {
                val mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.calendar_dday_popup_plus, null)
                mDialogView.findViewById<TextView>(R.id.textTitle).text = item.title
                mDialogView.findViewById<TextView>(R.id.textDay).text = CalendarViewModel.convertToDate2(item.endDate)
                mDialogView.findViewById<TextView>(R.id.textDday).text = holder.ddayRemain.text.toString()
                val mBuilder = AlertDialog.Builder(holder.itemView.context)
                    .setView(mDialogView)
                    .create()
                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                mDialogView.findViewById<ImageButton>(R.id.editbutton).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("calData",item)
                    bundle.putBoolean("edit",true)
                    //Navigation.findNavController(holder.itemView).navigate(R.id.action_fragHome_to_ddayAddFragment,bundle)
                    mBuilder.dismiss()
                }
                mDialogView.findViewById<ImageButton>(R.id.delbutton).setOnClickListener {
                    mBuilder.dismiss()
                    CalendarViewModel.deleteCalendar(item.id){ result ->
                        when (result) {
                            1 -> {
                                for(data in CalendarViewModel.ddayArrayList) {
                                    if(data.id == item.id) {
                                        CalendarViewModel.ddayArrayList.remove(data)
                                        break
                                    }
                                }
                                refreshAdapter()
                            }
                            2 -> {
                                Toast.makeText(holder.itemView.context, "삭제 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return CalendarViewModel.ddayArrayList.size
    }
    private fun refreshAdapter() {
        // 어댑터의 데이터 갱신 로직 등을 수행
        notifyDataSetChanged()
    }
}