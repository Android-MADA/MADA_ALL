package com.mada.reapp.HomeFunction.adapter.todo

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mada.reapp.HomeFunction.Model.PatchCheckboxTodo
import com.mada.reapp.HomeFunction.Model.PatchRequestTodo
import com.mada.reapp.HomeFunction.api.HomeApi
import com.mada.reapp.HomeFunction.api.RetrofitInstance
import com.mada.reapp.HomeFunction.bottomsheetdialog.TodoDateBottomSheetDialog
import com.mada.reapp.HomeFunction.viewModel.HomeViewModel
import com.mada.reapp.R
import com.mada.reapp.databinding.HomeTodoListBinding
import com.mada.reapp.db.MyApp
import com.mada.reapp.db.entity.CateEntity
import com.mada.reapp.db.entity.TodoEntity
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
            "#ED3024" -> {cbColor = R.drawable.home_checkbox1}
            "#F65F55" -> {cbColor = R.drawable.home_checkbox2}
            "#FD8415" -> {cbColor = R.drawable.home_checkbox3}
            "#FEBD16" -> {cbColor = R.drawable.home_checkbox4}
            "#FBA1B1" -> {cbColor = R.drawable.home_checkbox5}
            "#F46D85" -> {cbColor = R.drawable.home_checkbox6}
            "#D087F2" -> {cbColor = R.drawable.home_checkbox7}
            "#A516BC" -> {cbColor = R.drawable.home_checkbox8}
            "#89A9D9" -> {cbColor = R.drawable.home_checkbox9}
            "#269CB1" -> {cbColor = R.drawable.home_checkbox10}
            "#3C67A7" -> {cbColor = R.drawable.home_checkbox11}
            "#405059" -> {cbColor = R.drawable.home_checkbox12}
            "#C0D979" -> {cbColor = R.drawable.home_checkbox13}
            "#8FBC10" -> {cbColor = R.drawable.home_checkbox14}
            "#107E3D" -> {cbColor = R.drawable.home_checkbox15}
            else -> {cbColor = R.drawable.home_checkbox16}

        }

        holder.checkbox.setBackgroundResource(cbColor)



        if(category!!.isInActive == true){
            holder.checkbox.isEnabled = false
            holder.todoMenu.isInvisible = true
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
                val buffering = viewModel!!.setPopupBufferingTodo(context!!)
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
                            buffering.dismiss()
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
                                    val buffering = viewModel.setPopupBufferingTodo(requireContext())
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
                                    buffering.dismiss()
                                }
                                else if(value == "date"){
                                    val buffering = viewModel.setPopupBufferingTodo(requireContext())
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
                                    buffering.dismiss()

                                }

                            }

                        })
                    }

                }
            }
            else{
                viewModel!!.setPopupDelete(context!!, holder.data!!)
            }

        }

        //체크박스 클릭 리스너
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                var checkUpdateData = TodoEntity(repeatId = holder.data!!.repeatId, todoId = holder.data!!.todoId, id = holder.data!!.id, category = holder.data!!.category, date = holder.data!!.date, complete = isChecked, endRepeatDate =holder.data!!.endRepeatDate, repeat = holder.data!!.repeat, repeatInfo = holder.data!!.repeatInfo, startRepeatDate = holder.data!!.startRepeatDate, todoName = holder.data!!.todoName )
            //서버 연결
            if(holder.data!!.repeat == "N"){
                val buffering = viewModel!!.setPopupBufferingTodo(context!!)
                //일반투두 체크박스 변경
                Log.d("checkbox patch", checkUpdateData.toString())
                api.changeCheckox(viewModel!!.userToken, holder.data!!.id!!, PatchCheckboxTodo(isChecked)).enqueue(object : Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.d("todo server", "성공")
                            viewModel!!.updateTodo(checkUpdateData)
                        }
                        else{
                            Log.d("todo안드 잘못", "서버 연결 실패")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("서버 문제", "서버 연결 실패")
                    }

                })
                buffering.dismiss()
            }
            else{
                //반복 투두 체크박스 변경
                val buffering = viewModel!!.setPopupBufferingTodo(context!!)
                Log.d("check", checkUpdateData.toString())
                viewModel!!.changeRepeatCb(checkUpdateData, isChecked){
                    result ->
                    when(result){
                        0 -> {
                        }
                        1 -> {
                        }
                    }
                }
                buffering.dismiss()
            }


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
            "#ED3024" -> {R.drawable.ch_checked_color11}
            "#F65F55" -> {R.drawable.cb_checked_color13}
            "#FD8415" -> {R.drawable.ch_checked_color10}
            "#FEBD16" -> {R.drawable.cb_checked_febd}
            "#FBA1B1" -> {R.drawable.ch_checked_color7}
            "#F46D85" -> {R.drawable.cb_checked_f46d}
            "#D087F2" -> {R.drawable.cb_checked_d08f}
            "#A516BC" -> {R.drawable.cb_checked_a516}
            "#89A9D9" -> {R.drawable.ch_checked_color5}
            "#269CB1" -> {R.drawable.cb_checked_269c}
            "#3C67A7" -> {R.drawable.cb_checked_3c67}
            "#405059" -> {R.drawable.ch_checked_color12}
            "#C0D979" -> {R.drawable.cb_checked_c0d9}
            "#8FBC10" -> {R.drawable.cb_checked_8fbc}
            "#107E3D" -> {R.drawable.cb_checked_107e}
            else -> {R.drawable.cb_checked_0e41}
        }
        return color

    }

}