package com.mada.reapp.HomeFunction.adapter.category

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mada.reapp.R
import com.mada.reapp.databinding.CategoryListLayoutBinding
import com.mada.reapp.db.entity.CateEntity

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
        val viewHolder = ViewHolder(CategoryListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

        if(holder.data!!.isInActive == false){
            holder.cateLayout.setOnClickListener {
                itemClickListener.onClick(it, holder.data!!)
            }
        }
        else {
            //도장 표시
            holder.cateLayout.setOnClickListener {
                itemClickListener.onClick(it, holder.data!!)
            }
        }
    }

    inner class ViewHolder(private val binding : CategoryListLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        var data : CateEntity? = null
        fun bind(cateEntity: CateEntity){
            data = cateEntity

            binding.rvCategoryTitleTv.text = cateEntity.categoryName
            binding.rvCategoryIconIv.setImageResource(findIcon(cateEntity.iconId))
            binding.rvCategoryColorIv.backgroundTintList = ColorStateList.valueOf(Color.parseColor(cateEntity.color))
        }
        val cateLayout = binding.categoryListLayout
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