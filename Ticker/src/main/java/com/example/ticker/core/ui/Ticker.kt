package com.example.ticker.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.namespace.R
import com.example.namespace.databinding.LayoutTickerBinding
import com.example.ticker.core.adapter.TickerTimeAdapter
import com.example.ticker.core.adapter.ZoomCenterItemLayoutManager
import kotlin.math.max

class Ticker @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val HOURS_12_FORMAT = 12
    }

    private val binding: LayoutTickerBinding =
        LayoutTickerBinding.inflate(LayoutInflater.from(context), this, true)

    private val hoursAdapter: TickerTimeAdapter by lazy {
        TickerTimeAdapter()
    }

    private val minutesAdapter: TickerTimeAdapter by lazy {
        TickerTimeAdapter()
    }
    private val ampmAdapter: TickerTimeAdapter by lazy {
        TickerTimeAdapter()
    }

    private var isAmSelected: Boolean = true
    private var currentlySelectedAmpm: String =""
    private var currentlySelectedHour: String = ""
    private var currentlySelectedMinute: String = ""

    private var minutesInterval: Int = 1

    init {
        initConfigurations()
        initHoursAndMinutesList()
    }

    private fun initConfigurations() {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Ticker,
            defStyleAttr,
            0
        ).apply {
            try {
                val hoursFormat = this.getInt(R.styleable.Ticker_hoursFormat, HOURS_12_FORMAT)
                minutesInterval = max(this.getInt(R.styleable.Ticker_minutesInterval, 5), 1)
                isAmSelected = this.getBoolean(R.styleable.Ticker_shouldAmSelected, true)
            } finally {
                recycle()
            }
        }
    }

    private fun initHoursAndMinutesList() {
        val minutesList = (1..6).flatMap {(0 until 60 step minutesInterval).map { it.toString() } }
        val hoursList = (1..6).flatMap { (1..12).map { it.toString() } }
        val ampmList = arrayOf("  ","오전", "오후","  ").toList()

        initTickerRecyclerViews(
            binding.rvHours,
            hoursAdapter,
            hoursList,
            0
        )
        initTickerRecyclerViews(
            binding.rvMinutes,
            minutesAdapter,
            minutesList,
            1
        )
        initTickerRecyclerViews(
            binding.tvAmpm,
            ampmAdapter,
            ampmList,
            2
        )
        setupTopBottomPadding()
    }

    private fun initTickerRecyclerViews(
        rv: RecyclerView,
        adapter: TickerTimeAdapter,
        unitsList: List<String>,
        isWhat: Int,
    ) {
        rv.apply {
            layoutManager = ZoomCenterItemLayoutManager(context)
            this.adapter = adapter
        }
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(rv)

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateTextColorForSelectedTimeText(recyclerView, R.color.time_unit_selected_color, isWhat)
                } else {
                    updateTextColorForSelectedTimeText(recyclerView, R.color.time_unit_unselected_color, isWhat)
                }
            }

            private fun updateTextColorForSelectedTimeText(
                recyclerView: RecyclerView,
                @ColorRes textColorRes: Int,
                isWhat: Int
            ) {
                val snappedChildView =
                    linearSnapHelper.findSnapView(recyclerView.layoutManager) ?: return
                val snappedViewHolder = rv.getChildViewHolder(snappedChildView)
                if (snappedViewHolder is TickerTimeAdapter.DefaultTickerViewHolder) {
                    snappedViewHolder.binding.root.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            textColorRes,
                            null
                        )
                    )
                    val timeString = snappedViewHolder.binding.root.text.toString()
                    updateCurrentlySelectedValues(isWhat, timeString, textColorRes)

                }
            }
        })

        adapter.submitList(unitsList)
        scrollToCurrentTime(rv, isWhat)
    }


    private fun updateCurrentlySelectedValues(isWhat: Int,timeString: String, @ColorRes appliedColorRes: Int) {
        if (appliedColorRes == R.color.time_unit_selected_color) {
            if (isWhat==0) {
                currentlySelectedHour = timeString
            } else if(isWhat==1) {
                currentlySelectedMinute = timeString
            } else if(isWhat==2){
                if(timeString=="오전")
                    isAmSelected=true
                else
                    isAmSelected=false
                currentlySelectedAmpm = timeString
            }
        }
    }


    private fun setupTopBottomPadding() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val height = binding.root.height
                val padding = height / 2 - 20
                binding.rvHours.setPadding(0, padding, 0, padding)
                binding.rvMinutes.setPadding(0, padding, 0, padding)

                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun scrollToCurrentTime(rv: RecyclerView, isWhat: Int) {
        val scrollRunnable = if (isWhat==0) {
            val currentTimeIntPosition = currentlySelectedHour.toIntOrNull()
            currentTimeIntPosition?.let { timeInInt ->
                Runnable {
                    val position = timeInInt - 1

                    rv.smoothScrollToPosition(position)
                }
            } ?: Runnable {
                rv.smoothScrollBy(0, 20)
            }
        } else if(isWhat==1){
            val currentTimeIntPosition = currentlySelectedMinute.toIntOrNull()
            currentTimeIntPosition?.let {
                Runnable {
                    rv.smoothScrollToPosition(it)
                }
            } ?: Runnable {
                rv.smoothScrollBy(0, 20)
            }
        } else {
            val currentTimeIntPosition = currentlySelectedAmpm.toIntOrNull()
            currentTimeIntPosition?.let {
                Runnable {
                    rv.smoothScrollToPosition(it)
                }
            } ?: Runnable {
                rv.smoothScrollBy(0, 20)
            }
        }

        rv.postDelayed(scrollRunnable, 100)
    }
    /**
     * Returns the currently selected time
     * @param format: Return format for time, default HH:MM FORMAT
     */
    fun getCurrentlySelectedTime(format: String = "HH:MM FORMAT") : String{
        val finalFormat = if (isAmSelected) "오전" else "오후"
        return format.replace("HH", currentlySelectedHour)
            .replace("MM", currentlySelectedMinute)
            .replace("FORMAT", finalFormat)
    }

    /**
     * Sets the currently selected time for the time picker
     * Format HH:MM FORMAT
     */
    fun setInitialSelectedTime(initialTime: String) {
        val hourAndMinuteSplit = initialTime.split(":")
        if (hourAndMinuteSplit.size < 2) return
        currentlySelectedHour = hourAndMinuteSplit[0]
        currentlySelectedMinute = hourAndMinuteSplit[1]
        scrollToCurrentTime(binding.rvHours, 0)
        scrollToCurrentTime(binding.rvMinutes, 1)
    }

}