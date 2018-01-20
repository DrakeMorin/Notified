package com.yeet.notified

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry

class GeneralDataFragment : Fragment() {

    // 0 = total notifications, 1 = days of week, 2 = hours of day
    private var dropDownChoice: Int = 0
    private lateinit var spinner: Spinner
    private lateinit var pieChart: PieChart
    private lateinit var lineChart: LineChart



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            dropDownChoice = arguments!!.getInt(DROP_DOWN_CHOICE_ARG, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_general_data, container, false)
        spinner = view.findViewById(R.id.spinner_general_data)
        pieChart = view.findViewById(R.id.pie_chart_general_data)
        lineChart = view.findViewById(R.id.line_chart_general_data)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(DROP_DOWN_CHOICE_ARG, dropDownChoice)
    }

    fun cursorToDayEntries(cursor: Cursor): List<Entry> {
        val entries: ArrayList<Entry> = ArrayList()

        return entries
    }

    fun cursorToHourEntries(cursor: Cursor): List<Entry> {
        val entries: ArrayList<Entry> = ArrayList()

        return entries
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

