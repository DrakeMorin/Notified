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
import android.widget.ArrayAdapter
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter


class PerAppDataFragment : Fragment() {

    private lateinit var appSpinner: Spinner
    private lateinit var dataTypeSpinner:Spinner
    private lateinit var pieChart: PieChart
    private lateinit var lineChart: LineChart
    private var currentDataTyoe: Int = 0
    private var currentAppSelected: String? = null
    private val dayLabels = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private var dayXAxisFormatter: IAxisValueFormatter = IAxisValueFormatter { value, _ -> dayLabels[value.toInt()] }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_per_app_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appSpinner = view.findViewById(R.id.spinner_app_list)
        dataTypeSpinner = view.findViewById(R.id.spinner_data_type)
        pieChart = view.findViewById(R.id.pie_chart_per_app)
        lineChart = view.findViewById(R.id.line_chart_per_app)

        val appListArrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        appListArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        val dbHandler = DBHandler(context!!)
        appListArrayAdapter.addAll(dbHandler.getAllAppNames())
        appSpinner.adapter = appListArrayAdapter
        setupSpinners()

    }

    private fun configureData() {
        val dbHandler = DBHandler(context!!)
        if (currentDataTyoe == 0) {

        } else {

            val entries = dbHandler.getMostPopularTime(true, true, currentAppSelected)
            if (entries.size < 1) return
            val dayNotificationData = LineDataSet(entries, "Days of the Week")

            lineChart.setData(LineData(dayNotificationData))
            lineChart.xAxis.valueFormatter = dayXAxisFormatter
            lineChart.invalidate()
        }
    }

    private fun configureViews() {
        if (currentDataTyoe == 0) {
            lineChart.visibility = View.GONE
            pieChart.visibility = View.VISIBLE
        } else {
            pieChart.visibility = View.GONE
            lineChart.visibility = View.VISIBLE
        }
    }

    private fun setupSpinners() {
        appSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentAppSelected = parent.getItemAtPosition(position).toString()
                configureData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        dataTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentDataTyoe = position
                configureViews()
                configureData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}