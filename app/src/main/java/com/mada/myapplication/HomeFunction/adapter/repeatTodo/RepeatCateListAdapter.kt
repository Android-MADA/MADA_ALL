package com.mada.myapplication.HomeFunction.adapter.repeatTodo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mada.myapplication.CalenderFuntion.Model.CalendarViewModel
import com.mada.myapplication.HomeFunction.Model.PostRequestTodo
import com.mada.myapplication.HomeFunction.Model.PostRequestTodoCateId
import com.mada.myapplication.HomeFunction.Model.PostResponseTodo
import com.mada.myapplication.HomeFunction.Model.TodoList
import com.mada.myapplication.HomeFunction.api.HomeApi
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.HomeFunction.viewModel.HomeViewModel
import com.mada.myapplication.R
import com.mada.myapplication.databinding.HomeCatagoryListBinding
import com.mada.myapplication.db.MyApp
import com.mada.myapplication.db.entity.CateEntity
import com.mada.myapplication.db.entity.RepeatEntity
import com.mada.myapplication.db.entity.TodoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class RepeatCateListAdapter(private val view : View, fragmentManager : FragmentManager, context : Context) : ListAdapter<CateEntity, RepeatCateListAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CateEntity>(){
            override fun areItemsTheSame(oldItem: CateEntity, newItem: CateEntity): Boolean {
                //아이템  id 가 같은지 확인
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CateEntity, newItem: CateEntity): Boolean {
                //아이템 내용이 같은 지 확인
                return oldItem == newItem
            }

        }
    }

    //viewmodel 가져오기
    var viewModel : HomeViewModel? = null
    var calendarViewModel : CalendarViewModel? = null
    private var repeatFragmentManager = fragmentManager
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeCatagoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val api = RetrofitInstance.getInstance().create(HomeApi::class.java)


        Log.d("HomeCateItem", getItem(position).toString())
        val cateId = holder.bind(getItem(position))

        CoroutineScope(Dispatchers.IO).launch {
            //todoadapter 연결하기
            val mTodoAdapter = RepeatTodoListAdapter(view, repeatFragmentManager, mContext)
            mTodoAdapter.viewModel = viewModel
            mTodoAdapter.calendarViewModel = calendarViewModel
            // 반복투두 읽어오기
            viewModel!!.readRepeatTodo(cateId, mTodoAdapter)
            withContext(Dispatchers.Main){
                holder.todoRv.adapter = mTodoAdapter
                holder.todoRv.layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)
            }
        }

        holder.btnAdd.setOnClickListener {
            var bundle = Bundle()

            bundle.putStringArrayList("keyAdd", arrayListOf(
                holder.data!!.id.toString(),
            ))
            Navigation.findNavController(view!!).navigate(R.id.action_homeRepeatTodoFragment_to_repeatTodoAddFragment, bundle)
        }

        //edt 저장

    }

    inner class ViewHolder(private val binding : HomeCatagoryListBinding) : RecyclerView.ViewHolder(binding.root){
        var data : CateEntity? = null
        fun bind(cateEntity: CateEntity) : Int {
            binding.todoCategoryTitleTv.text = cateEntity.categoryName
            binding.todoCategoryIconIv.setImageResource(findIcon(cateEntity.iconId))
            data = cateEntity
            return cateEntity.id
        }
        val todoRv = binding.rvRepeatTodo
        val btnAdd = binding.btnRepeatAddTodo
        val layoutAdd = binding.layoutRepeatTodoAdd
        val edtAdd = binding.edtRepeatTodo
    }

    fun findIcon(iconId : Int) : Int {
        val icon = when(iconId){
            1 -> {
                R.drawable.ic_home_cate_burn}
            2 -> {
                R.drawable.ic_home_cate_chat1}
            3 -> {
                R.drawable.ic_home_cate_health}
            4 -> {
                R.drawable.ic_home_cate_heart}
            5 -> {
                R.drawable.ic_home_cate_laptop}
            6 -> {
                R.drawable.ic_home_cate_lightout}
            7 -> {
                R.drawable.ic_home_cate_lightup}
            8 -> {
                R.drawable.ic_home_cate_meal2}
            9 -> {
                R.drawable.ic_home_cate_meal1}
            10 -> {
                R.drawable.ic_home_cate_mic}
            11 -> {
                R.drawable.ic_home_cate_music}
            12 -> {
                R.drawable.ic_home_cate_pen}
            13 -> {
                R.drawable.ic_home_cate_phone}
            14 -> {
                R.drawable.ic_home_cate_plan}
            15 -> {
                R.drawable.ic_home_cate_rest}
            16 -> {
                R.drawable.ic_home_cate_sony}
            17 -> {
                R.drawable.ic_home_cate_study}
            else -> {
                R.drawable.ic_home_cate_work}
        }
        return icon
    }
}