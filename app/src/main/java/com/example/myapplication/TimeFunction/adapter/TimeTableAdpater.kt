package com.example.myapplication.TimeFunction.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.tiles.LayoutElementBuilders.Image
import com.example.myapplication.R
import com.example.myapplication.TimeFunction.TimeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimeTableAdpater(private val data: ArrayList<TimeViewModel.PieChartData>, private val today : String, private val viewModel: TimeViewModel) : RecyclerView.Adapter<TimeTableAdpater.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.timetable_title_tv)
        val memo: TextView = itemView.findViewById(R.id.timetable_memo_tv)
        val image : ImageView = itemView.findViewById(R.id.timetable_bg)
        // Add other views for your data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_timetable_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        // Populate the views with data from the item
        val start = item.startHour* 60 + item.startMin
        val end = item.endHour * 60 + item.endMin
        holder.title.text = "${item.title}"
        holder.memo.text = "${item.memo}"
        holder.image.setColorFilter(Color.parseColor(item.colorCode))
        val layoutParams = holder.image.layoutParams
        layoutParams.height = dpToPx(holder.image.context, end-start)
        holder.image.layoutParams = layoutParams
        if(item.id!=0) {
            holder.image.setOnClickListener {
                val mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.time_timetable_popup_item, null)
                val mBuilder = AlertDialog.Builder(holder.itemView.context)
                    .setView(mDialogView)
                    .create()

                mBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mBuilder?.window?.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.show()

                //팝업 사이즈 조절
                DisplayMetrics()
                holder.itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val size = Point()
                val display = (holder.itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                display.getSize(size)
                val screenWidth = size.x
                val popupWidth = (screenWidth * 0.8).toInt()
                mBuilder?.window?.setLayout(popupWidth, WindowManager.LayoutParams.WRAP_CONTENT)
                mDialogView.findViewById<TextView>(R.id.time_title_tv).text =  item.title
                mDialogView.findViewById<TextView>(R.id.time_memo_tv).text =  item.memo
                mDialogView.findViewById<ImageView>(R.id.time_info1_iv).setColorFilter(Color.parseColor(item.colorCode))
                mDialogView.findViewById<ImageView>(R.id.time_info2_iv).setColorFilter(Color.parseColor(item.colorCode))
                val startTime = timeChangeReverse("${String.format("%02d", item.startHour)}:${String.format("%02d", item.startMin)}:00")
                val endTime = timeChangeReverse("${String.format("%02d", item.endHour)}:${String.format("%02d",item.endMin)}:00")

                mDialogView.findViewById<TextView>(R.id.time_time_tv).text =  "$startTime ~ $endTime"
                //팝업 타이틀 설정, 버튼 작용 시스템
                mDialogView.findViewById<TextView>(R.id.time_edit_btn).setOnClickListener( {
                    val bundle = Bundle()
                    bundle.putString("today",today)
                    bundle.putSerializable("pieChartData", item)
                    bundle.putSerializable("pieChartDataArray", data)
                    bundle.putInt("frag",R.id.action_fragTimeAdd_to_fragTimeTable)
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_fragTimeTable_to_fragTimeAdd,bundle)
                    mBuilder.dismiss()
                })
                mDialogView.findViewById<TextView>(R.id.time_remove_btn).setOnClickListener( {
                    val id = item.id
                    viewModel.delTimeDatas(id) { result ->
                        when (result) {
                            1 -> {
                                val tmpList = viewModel.hashMapArraySchedule.get(today)!!
                                for(data in tmpList) {
                                    if(data.id==id) {
                                        tmpList.remove(data)
                                        break
                                    }
                                }
                                Navigation.findNavController(holder.itemView).navigate(R.id.action_fragTime_to_fragTime)
                            }
                            2 -> {
                                Toast.makeText(holder.itemView.context, "서버 와의 통신 불안정", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    mBuilder.dismiss()
                })
            }
        }

        // Set other data to other views
    }

    override fun getItemCount(): Int {
        return data.size
    }
    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun timeChangeReverse(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("ko", "KR"))
        val outputFormat = SimpleDateFormat(" a h:mm ", Locale("ko", "KR"))
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)

        return outputFormat.format(calendar.time)//.replace("AM","오전").replace("PM","오후")
    }
}
