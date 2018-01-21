package com.yeet.notified

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate






class GeneralDataFragment : Fragment() {

    // 0 = total notifications, 1 = days of week, 2 = hours of day
    // Note, hours of the day, is disabled
    private var currentDropDownChoice: Int = 0
    private lateinit var spinner: Spinner
    private lateinit var pieChart: PieChart
    private lateinit var lineChart: LineChart

    private val dayLabels = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private val hourLabels = arrayOf("00:00 - 01:00", "01:00 - 02:00", "02:00 - 03:00", "03:00 - 04:00", "04:00 - 05:00", "05:00 - 06:00",
            "06:00 - 07:00", "07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00",
            "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "19:00 - 20:00", "20:00 - 21:00",
            "21:00 - 22:00", "22:00 - 23:00", "23:00 - 00:00")

    private var totalNotificationData: PieDataSet? = null
    private var dayNotificationData: LineDataSet? = null
    private var hourNotificationData: LineDataSet? = null


    private var dayXAxisFormatter: IAxisValueFormatter = IAxisValueFormatter { value, _ -> dayLabels[value.toInt()] }
    private var hourXAxisFormatter: IAxisValueFormatter = IAxisValueFormatter { value, _ -> hourLabels[value.toInt()] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currentDropDownChoice = arguments!!.getInt(DROP_DOWN_CHOICE_ARG, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_general_data, container, false)
        spinner = view.findViewById(R.id.spinner_general_data)
        pieChart = view.findViewById(R.id.pie_chart_general_data)
        lineChart = view.findViewById(R.id.line_chart_general_data)
        configureData()
        setupSpinner()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(DROP_DOWN_CHOICE_ARG, currentDropDownChoice)
    }

    private fun configureData() {
        if (currentDropDownChoice == 0) {
            if (totalNotificationData == null) {
                val dbHandler = DBHandler(context!!)
                val entries = dbHandler.getPieGraph()
                if (entries.size < 1) return
                totalNotificationData = PieDataSet(entries, "Breakdown of Notifications")
                totalNotificationData!!.setColors(intArrayOf(R.color.lightBlue, R.color.lightGreen, R.color.lightYellow, R.color.lightPurple), context)
            }
            pieChart.setData(PieData(totalNotificationData))
            pieChart.invalidate()
        } else if (currentDropDownChoice == 1) {
            if (dayNotificationData == null) {
                val dbHandler = DBHandler(context!!)
                val entries = dbHandler.getMostPopularTime(true, true, null)
                if (entries.size < 1) return
                dayNotificationData = LineDataSet(entries, "Days of the Week")
            }

            lineChart.setData(LineData(dayNotificationData))
            lineChart.xAxis.valueFormatter = dayXAxisFormatter
            lineChart.invalidate()
        } else if (currentDropDownChoice == 2) {
            if (hourNotificationData == null) {
                val dbHandler = DBHandler(context!!)
                val entries = dbHandler.getMostPopularTime(false, true, null)
                hourNotificationData = LineDataSet(entries, "Hours of the Day")
            }
            lineChart.setData(LineData(hourNotificationData))
            lineChart.xAxis.valueFormatter = hourXAxisFormatter
            lineChart.invalidate()
        }
    }

    private fun configureViews() {
        if (currentDropDownChoice == 0) {
            lineChart.visibility = View.GONE
            pieChart.visibility = View.VISIBLE
        } else {
            pieChart.visibility = View.GONE
            lineChart.visibility = View.VISIBLE
        }
    }

    private fun setupSpinner() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentDropDownChoice = position
                configureViews()
                configureData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }



    companion object {
        private val DROP_DOWN_CHOICE_ARG = "dropDownChoiceArg"

        fun newInstance(dropDownChoice: Int = 0): GeneralDataFragment {
            val fragment = GeneralDataFragment()
            val args = Bundle()
            args.putInt(DROP_DOWN_CHOICE_ARG, dropDownChoice)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

