package com.example.myapplication.HomeFunction.adapter.repeatTodo

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeFunction.adapter.todo.HomeCateListAdapter
import com.example.myapplication.HomeFunction.adapter.todo.HomeTodoListAdapter
import com.example.myapplication.HomeFunction.viewModel.HomeViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeCatagoryListBinding
import com.example.myapplication.db.entity.CateEntity
import com.example.myapplication.db.entity.RepeatEntity
import com.example.myapplication.db.entity.TodoEntity

class RepeatCateListAdapter(private val view : View) : ListAdapter<CateEntity, RepeatCateListAdapter.ViewHolder>(DiffCallback) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeCatagoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.d("HomeCateItem", getItem(position).toString())
        val cateId = holder.bind(getItem(position))
        //todoadapter 연결하기
        val mTodoAdapter = RepeatTodoListAdapter(view)
        mTodoAdapter.viewModel = viewModel
        // 반복투두 읽어오기
        viewModel!!.readRepeatTodo(cateId, mTodoAdapter)
        holder.todoRv.adapter = mTodoAdapter
        holder.todoRv.layoutManager = LinearLayoutManager(holder.todoRv.context, LinearLayoutManager.VERTICAL, false)

        holder.btnAdd.setOnClickListener {
            if(holder.layoutAdd.isGone == true){
                holder.layoutAdd.isVisible = true
            }
            else {
                holder.edtAdd.text.clear()
                holder.layoutAdd.isGone = true
            }
        }

        //edt 저장

        holder.edtAdd.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                val data = RepeatEntity(0, 3, viewModel!!.homeDate.value.toString(), getItem(position).cateId, holder.edtAdd.text.toString(), false, "N", null, null, null, null, false, viewModel!!.startMonday, viewModel!!.completeBottom, viewModel!!.newTodoTop)
                viewModel!!.createRepeatTodo(data, holder.edtAdd)
                holder.edtAdd.text.clear()
                holder.layoutAdd.isGone = true
                true
            }

            false
        }

    }

    inner class ViewHolder(private val binding : HomeCatagoryListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(cateEntity: CateEntity) : Int {
            binding.tvRepeatCategory.text = cateEntity.categoryName
            binding.tvRepeatCategory.text = cateEntity.categoryName
            binding.icRepeatCategory.setImageResource(findIcon(cateEntity.iconId))
            val mGradientDrawable : GradientDrawable = binding.layoutHomeViewpagerCateList.background as GradientDrawable
            mGradientDrawable.setStroke(6, Color.parseColor(cateEntity.color))

            return cateEntity.cateId
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