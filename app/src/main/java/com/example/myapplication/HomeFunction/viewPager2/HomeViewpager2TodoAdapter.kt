package com.example.myapplication.HomeFunction.viewPager2

import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class HomeViewpager2TodoAdapter(private var dataSet : ArrayList<SampleHomeTodoData>) : RecyclerView.Adapter<HomeViewpager2TodoAdapter.viewHolder>() {

    class viewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val todoCheckBox : CheckBox
        val tvTodo : TextView
        val todoMenu : TextView
        val editLayout : LinearLayout
        val edtTodo : EditText
        val todoLayout : ConstraintLayout

        init {
            todoCheckBox = view.findViewById(R.id.iv_repeat_todo)
            tvTodo = view.findViewById(R.id.tv_home_todo)
            todoMenu = view.findViewById(R.id.tv_home_todo_edit)
            editLayout = view.findViewById(R.id.layout_viewpager_todo_edit)
            edtTodo = view.findViewById(R.id.edt_home_viewpager2_todo_edit)
            todoLayout = view.findViewById(R.id.layout_viewpager_todo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_todo_list, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        //checkbox value change
        //menu창 누르면 메뉴창 오픈, 각 메뉴 별로 행동 설정
        holder.tvTodo.text = dataSet[position].todoName
        holder.todoCheckBox.isChecked = dataSet[position].done
        holder.todoMenu.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            popup.menuInflater.inflate(R.menu.home_todo_edit_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                if(item.itemId == R.id.home_todo_edit) {
                    Log.d("todoEdit", "수정하기")
                    holder.editLayout.isVisible = true
                    holder.todoLayout.isGone = true
                    holder.edtTodo.setText(dataSet[position].todoName)

                    itemClickListener.onClick(it, position)
                }
                else{
                    itemClickListener.onClick(it, position)
                    dataSet.removeAt(position)
                    notifyDataSetChanged()
                    Log.d("todoDelete", "삭제하기")
                }
                true
            }
            popup.show()
        }

        holder.todoCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            dataSet[position].done = buttonView.isChecked
            Log.d("ch확인", "${dataSet[position].todoName} : ${dataSet[position].done}")
            notifyDataSetChanged()
        }

        holder.edtTodo.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                //데이터 수정 반영
                dataSet[position].todoName = holder.edtTodo.text.toString()
                //edt 업애고 이전 투두 복구
                holder.todoLayout.isVisible = true
                holder.edtTodo.text.clear()
                holder.editLayout.isGone = true
                notifyDataSetChanged()

                true
            }

            false
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: HomeViewpager2TodoAdapter.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : HomeViewpager2TodoAdapter.OnItemClickListener
}

//checkbox 처리 -> value 바뀌면 db 변경
//tv dataset에서 가져오서 뿌리고
// ... 버튼 처리 -> 수정하기 :  edt 등장, db 수정
//             -> 삭제하기 : 삭제하고 db 수정