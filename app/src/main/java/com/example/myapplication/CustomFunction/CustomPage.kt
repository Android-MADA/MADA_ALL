package com.example.myapplication
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
/*
class CustomPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val View = view.custom_recyclerview
    }

    class Adapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {
        private val inflater = LayoutInflater.from(context)

        override fun getItemCount(): Int {
            return ITEM_COUNT
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.custom_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            @SuppressLint("SetTextI18n")
            holder.custom_menutable = "Item ${position + 1}"
        }
    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView)

    companion object {
        private const val ITEM_COUNT = 30

        fun newInstance(): CustomPage {
            return CustomPage()
        }
    }
}*/