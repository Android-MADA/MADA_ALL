package com.example.myapplication.CalenderFuntion

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CalenderFuntion.Model.CalendarDATA
import com.example.myapplication.R
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class CalendarAdapter(private val dayList: ArrayList<Date>, private val myDataArray:  Array<ArrayList<CalendarDATA?>>, private val token : String)
        : RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>()  {
    var m = LocalDate.now().monthValue
    var y = LocalDate.now().year
    var d = LocalDate.now().dayOfMonth
    val weekdays = arrayOf("일" ,"월", "화", "수", "목", "금", "토")

    //임시 데이터
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay : TextView = itemView.findViewById(R.id.textDay)
        val lines = arrayOf<ImageView>(
            itemView.findViewById(R.id.calenadar_line1),
            itemView.findViewById(R.id.calenadar_line2)
        )
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell,parent,false)

        return ItemViewHolder(view)
    }
    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var monthDate = dayList[holder.adapterPosition]
        var dateCalendar = Calendar.getInstance()
        dateCalendar.time = monthDate
        var dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)
        holder.textDay.text = dayNo.toString()
        var iYear = dateCalendar.get(Calendar.YEAR)
        var iMonth = dateCalendar.get(Calendar.MONTH) + 1
        var iDay = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val today = iYear.toString() +"-"+iMonth.toString()+"-"+iDay.toString()
        val colorMain = ContextCompat.getColor(holder.itemView.context, R.color.main)
        if(CalendarUtil.selectedDate.monthValue != iMonth) {
            holder.textDay.setTextColor(colorMain)
        }
        if(iYear == y && iMonth == m && iDay == d) {
            holder.textDay.setBackgroundResource(R.drawable.calendar_cell_background)
            holder.textDay.setTextColor(Color.WHITE)
        }
        for(data in myDataArray[position]) {
            if (data != null && data.floor !=-1) {
                val cal =holder.lines[data.floor]
                cal.setColorFilter(Color.parseColor(data.color), PorterDuff.Mode.SRC_IN)
                if(data.duration) {
                    if((data.startDate==today&& position%7==6) ||(data.endDate==today&& position%7==0)) {
                        cal.setImageResource(R.drawable.calendar_line_center2)
                    } else if(data.startDate==today || data.startDate2==today ||position%7==0) {
                        cal.setImageResource(R.drawable.calendar_line_left)
                    } else if(data.endDate==today || position%7==6) {
                        cal.setImageResource(R.drawable.calendar_line_right)
                    } else {
                        cal.setImageResource(R.drawable.calendar_line_center)
                    }
                } else {
                    holder.lines[data.floor].setImageResource(R.drawable.calendar_point)
                }
            } else {

            }
        }

        holder.itemView.setOnClickListener {
            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"
            var mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.calendar_popup,null)
            var mBuilder = AlertDialog.Builder(holder.itemView.context)
                .setView(mDialogView)
                .create()

            mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            val alertDialog = mBuilder.show()
            val display = (holder.itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            mBuilder?.window?.setGravity(Gravity.CENTER)

            mDialogView.findViewById<TextView>(R.id.textDay2).text = iDay.toString() +"일"
            mDialogView.findViewById<TextView>(R.id.textPosition).text = weekdays[position%7] + "요일"
            mDialogView.findViewById<AppCompatImageButton>(R.id.addBtn).setOnClickListener( {
                val bundle = Bundle()
                bundle.putString("preSchedule", "$iYear-$iMonth-$iDay")
                bundle.putString("nextSchedule", "$iYear-$iMonth-$iDay")
                bundle.putString("Token",token)
                if(mDialogView.findViewById<TextView>(R.id.textTitle).text.toString()=="")
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_fragCalendar_to_calendarAdd,bundle)
                else {
                    // 등록
                }
                mBuilder.dismiss()
            })


            //임시 데이터
            if(myDataArray[position].isEmpty()) {

            } else {
                val adapter = CalendarScheduleAdapter(myDataArray[position],iDay,holder.itemView,mBuilder,token)
                var manager: RecyclerView.LayoutManager = GridLayoutManager(holder.itemView.context,1)
                mDialogView.findViewById<RecyclerView>(R.id.scheduleList).layoutManager = manager
                mDialogView.findViewById<RecyclerView>(R.id.scheduleList).adapter = adapter
            }

        }

    }

    override fun getItemCount(): Int {
        return 42
    }

}