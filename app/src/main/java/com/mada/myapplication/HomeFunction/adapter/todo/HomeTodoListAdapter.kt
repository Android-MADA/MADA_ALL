package com.mada.myapplication.HomeFunction.adapter.todo

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.HomeFunction.Model.PatchRequestTodo
import com.mada.myapplication.HomeFunction.Model.data
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.bottomsheetdialog.TodoDateBottomSheetDialog
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.HomeTodoListBinding
import com.mada.myapplication.db.MyApp
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.db.entity.TodoEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeTodoListAdapter(fragmentManager : FragmentManager) : ListAdapter<TodoEntity, HomeTodoListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoEntity>(){
            override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
                //아이템  id 가 같은지 확인
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
                //아이템 내용이 같은 지 확인
                return oldItem == newItem
            }

        }
    }

    var viewModel : HomeViewModel? = null
    var category : CateEntity? = null
    var context : Context? = null
    private var mFragmentManager = fragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)

        holder.bind(getItem(position))

        var cbColor = R.drawable.home_checkbox1

        when(category!!.color){
            "#21C362" -> {cbColor = R.drawable.home_checkbox1}
            "#0E9746" -> {cbColor = R.drawable.home_checkbox2}
            "#7FC7D4" -> {cbColor = R.drawable.home_checkbox3}
            "#2AA1B7" -> {cbColor = R.drawable.home_checkbox4}
            "#89A9D9" -> {cbColor = R.drawable.home_checkbox5}
            "#486DA3" -> {cbColor = R.drawable.home_checkbox6}
            "#FDA4B4" -> {cbColor = R.drawable.home_checkbox7}
            "#F0768C" -> {cbColor = R.drawable.home_checkbox8}
            "#F8D141" -> {cbColor = R.drawable.home_checkbox9}
            "#F68F30" -> {cbColor = R.drawable.home_checkbox10}
            "#F33E3E" -> {cbColor = R.drawable.home_checkbox11}
            else -> {cbColor = R.drawable.home_checkbox12}

        }

        holder.checkbox.setBackgroundResource(cbColor)



        if(category!!.isInActive == true){
            holder.checkbox.isEnabled = false
            //holder.todoMenu.isInvisible = true
            holder.ivIcon.isVisible = true
            if(holder.data!!.complete == true){
                holder.ivIcon.setImageResource(findRes(category!!.color))
            }
        }
        else {
            holder.checkbox.isVisible = true
            holder.ivIcon.isGone = true
            holder.todoMenu.isVisible = true
        }



        holder.edtTodo.setOnKeyListener { v, keyCode, event ->

            var handled = false

            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ){

                val context = MyApp.context()
                //키보드 내리기
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(holder.edtTodo.windowToken, 0)
                handled = true

                //update
                val data = TodoEntity(holder.data!!.todoId, holder.data!!.id, date = holder.data!!.date, category = holder.data!!.category, todoName = holder.edtTodo.text.toString(), complete = holder.data!!.complete, repeat = holder.data!!.repeat, repeatInfo = holder.data!!.repeatInfo, startRepeatDate = holder.data!!.startRepeatDate, endRepeatDate = holder.data!!.endRepeatDate)
                viewModel!!.updateTodo(data)
                val updateData = PatchRequestTodo(todoName = holder.edtTodo.text.toString(), repeat = "N",  repeatInfo = holder.data!!.repeatInfo, startRepeatDate = holder.data!!.startRepeatDate, endRepeatDate = holder.data!!.endRepeatDate, complete = holder.data!!.complete, holder.data!!.date)
                api.editTodo(viewModel!!.userToken, holder.data!!.id!!, updateData).enqueue(object :Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.d("todo server", "성공")
                        }
                        else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("서버 문제", "서버 연결 실패")
                    }
                })
                holder.edtTodo.text.clear()
                holder.editLayout.isGone = true
                holder.layoutcb.isVisible = true
                true
            }
            handled
            false
        }

        holder.todoMenu.setOnClickListener {

            // 반복 투두 경우 삭제 다이얼로그
            if(holder.data!!.repeat == "N"){
                val todoMenuBottomSheet = viewModel?.let { it1 -> TodoDateBottomSheetDialog(it1) }
                if (todoMenuBottomSheet != null) {
                    todoMenuBottomSheet.menuFlag = "todoMenu"
                    todoMenuBottomSheet.show(mFragmentManager, todoMenuBottomSheet.tag)
                    todoMenuBottomSheet.apply {
                        setCallback(object : TodoDateBottomSheetDialog.OnSendFromBottomSheetDialog{
                            override fun sendValue(value: String) {
                                Log.d("todoMenu", value)
                                if(value == "edit"){
                                    holder.editLayout.isVisible = true
                                    holder.layoutcb.isGone = true
                                    holder.edtTodo.setText(holder.data!!.todoName)
                                }
                                else if(value == "delete"){
                                    val data = holder.data
                                    api.deleteTodo(viewModel!!.userToken, holder.data!!.id!!).enqueue(object :Callback<Void>{
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            if(response.isSuccessful){
                                                Log.d("todo server", "성공")
                                                viewModel!!.deleteTodo(data!!)
                                            }
                                            else {
                                                Log.d("todo안드 잘못", "서버 연결 실패")
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            Log.d("서버 문제", "서버 연결 실패")
                                        }
                                    })
                                }
                                else if(value == "date"){
                                    Log.d("바꿀 날짜", viewModel.selectedChangedDate)
                                    //val data = TodoEntity(holder.data!!.todoId, holder.data!!.id, holder.data!!.todoName, holder.data!!.category, holder.data!!.todoName, holder.data!!.complete, holder.data!!.repeat, holder.data!!.repeatWeek, holder.data!!.repeatMonth, holder.data!!.startRepeatDate, holder.data!!.endRepeatDate, holder.data!!.isAlarm, holder.data!!.startTodoAtMonday, holder.data!!.endTodoBackSetting, holder.data!!.newTodoStartSetting)
                                    //viewModel!!.updateTodo(data)
                                    val updateData = PatchRequestTodo(todoName = holder.data!!.todoName, repeat = "N",  repeatInfo = holder.data!!.repeatInfo, startRepeatDate = holder.data!!.startRepeatDate, endRepeatDate = holder.data!!.endRepeatDate, complete = holder.data!!.complete, date = viewModel.selectedChangedDate)
                                    Log.d("todo server", updateData.toString())
                                    api.editTodo(viewModel!!.userToken, holder.data!!.id!!, updateData).enqueue(object :Callback<Void>{
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            if(response.isSuccessful){
                                                Log.d("todo server", "성공")
                                                viewModel!!.deleteTodo(holder.data!!)
                                            }
                                            else {
                                                Log.d("todo안드 잘못", "서버 연결 실패")
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            Log.d("서버 문제", "서버 연결 실패")
                                        }
                                    })

                                }

                            }

                        })
                    }

                }
            }
            else{
                val mDeleteDialog = Dialog(context!!)
                mDeleteDialog.setContentView(R.layout.repeat_delete_dialog)

                val dialogOne = mDeleteDialog.findViewById<RadioButton>(R.id.radio_repeat_delete_one)
                val dialogAfter = mDeleteDialog.findViewById<RadioButton>(R.id.radio_repeat_delete_after)
                val dialogAll = mDeleteDialog.findViewById<RadioButton>(R.id.radio_repeat_delete_all)
                val layoutOne = mDeleteDialog.findViewById<LinearLayout>(R.id.layout_delete_one)
                val layoutAfter = mDeleteDialog.findViewById<LinearLayout>(R.id.layout_delete_after)
                val layoutAll = mDeleteDialog.findViewById<LinearLayout>(R.id.layout_delete_all)

                var selectedFlag = "one"

                dialogOne.isChecked = true
                dialogAfter.isChecked = false
                dialogAll.isChecked = false

                dialogOne.setOnClickListener {
                    dialogOne.isChecked = true
                    dialogAll.isChecked = false
                    dialogAfter.isChecked = false
                    selectedFlag = "one"
                }

                layoutOne.setOnClickListener {
                    dialogOne.isChecked = true
                    dialogAll.isChecked = false
                    dialogAfter.isChecked = false
                    selectedFlag = "one"
                }

                dialogAfter.setOnClickListener {
                    dialogAfter.isChecked = true
                    dialogAll.isChecked = false
                    dialogOne.isChecked = false
                    selectedFlag = "after"
                }

                layoutAfter.setOnClickListener {
                    dialogAfter.isChecked = true
                    dialogAll.isChecked = false
                    dialogOne.isChecked = false
                    selectedFlag = "after"
                }

                dialogAll.setOnClickListener {
                    dialogAll.isChecked = true
                    dialogOne.isChecked = false
                    dialogAfter.isChecked = false
                    selectedFlag = "all"
                }

                layoutAll.setOnClickListener {
                    dialogAll.isChecked = true
                    dialogOne.isChecked = false
                    dialogAfter.isChecked = false
                    selectedFlag = "all"
                }

                mDeleteDialog.findViewById<TextView>(R.id.btn_repeat_delete_cancel).setOnClickListener {
                    mDeleteDialog.dismiss()
                    Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
                }

                mDeleteDialog.findViewById<TextView>(R.id.btn_repeat_delete_delete).setOnClickListener {
                    mDeleteDialog.dismiss()
                    Toast.makeText(context, selectedFlag, Toast.LENGTH_SHORT).show()
                }
                mDeleteDialog.show()
            }

        }

        //체크박스 클릭 리스너
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                val checkUpdateData = TodoEntity(todoId = holder.data!!.todoId, id = holder.data!!.id, category = holder.data!!.category, date = holder.data!!.date, complete = isChecked, endRepeatDate =holder.data!!.endRepeatDate, repeat = holder.data!!.repeat, repeatInfo = holder.data!!.repeatInfo, startRepeatDate = holder.data!!.startRepeatDate, todoName = holder.data!!.todoName )
                val updateData = PatchRequestTodo(todoName = checkUpdateData.todoName, repeat = checkUpdateData.repeat, repeatInfo = checkUpdateData.repeatInfo,startRepeatDate = checkUpdateData.startRepeatDate, endRepeatDate = checkUpdateData.endRepeatDate, complete = isChecked, date = holder.data!!.date)
            Log.d("todo server", updateData.toString())
            //서버 연결
                api.editTodo(viewModel!!.userToken, holder.data!!.id!!, updateData).enqueue(object :Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.d("todo server", "성공")
                            viewModel!!.updateTodo(checkUpdateData)
                        }
                        else {
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("서버 문제", "서버 연결 실패")
                    }
                })

        }
    }

    inner class ViewHolder(private val binding : HomeTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        var data : TodoEntity? = null
        fun bind(todoEntity : TodoEntity) {
            binding.ivRepeatTodo.isChecked = todoEntity.complete
            binding.tvHomeTodo.text = todoEntity.todoName
            data = todoEntity
        }
        val editLayout = binding.layoutViewpagerTodoEdit
        val edtTodo  = binding.edtHomeViewpager2TodoEdit
        val checkbox = binding.ivRepeatTodo
        val layoutcb = binding.layoutViewpagerTodo
        val ivIcon = binding.ivMyTodo
        val todoMenu = binding.tvHomeTodoEdit

    }

    fun findRes(color : String) : Int {
        var color : Int = when(color){
            "#21C362" -> {R.drawable.ch_checked_color1}
            "#0E9746" -> {R.drawable.ch_checked_color2}
            "#7FC7D4" -> {R.drawable.ch_checked_color3}
            "#2AA1B7" -> {R.drawable.ch_checked_color4}
            "#89A9D9" -> {R.drawable.ch_checked_color5}
            "#486DA3" -> {R.drawable.ch_checked_color6}
            "#FDA4B4" -> {R.drawable.ch_checked_color7}
            "#F0768C" -> {R.drawable.ch_checked_color8}
            "#F8D141" -> {R.drawable.ch_checked_color9}
            "#F68F30" -> {R.drawable.ch_checked_color10}
            "#F33E3E" -> {R.drawable.ch_checked_color11}
            else -> {R.drawable.ch_checked_color12}
        }
        return color

    }

}