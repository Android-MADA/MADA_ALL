package com.example.myapplication.HomeFunction.adapter.category

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.HomeEditCategoryListBinding
import com.example.myapplication.db.entity.CateEntity

class CateListAdapter : ListAdapter<CateEntity, CateListAdapter.ViewHolder>(DiffCallback) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(HomeEditCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding : HomeEditCategoryListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(cateEntity: CateEntity){
            binding.tvHomeAddCategory.text = cateEntity.categoryName
            binding.ivHomeAddCategory.setImageResource(findIcon(cateEntity.iconId))
            val mGradientDrawable : GradientDrawable = binding.layoutHomeCateList.background as GradientDrawable
            mGradientDrawable.setStroke(6, Color.parseColor(cateEntity.color))
            binding.layoutHomeCateList.setOnClickListener {
                itemClickListener.onClick(it, cateEntity)
            }
        }
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

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, dataSet: CateEntity)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}