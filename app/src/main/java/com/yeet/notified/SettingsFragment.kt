package com.yeet.notified

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch

/**
 * Created by jacobsteves on 2018-01-20.
 */
private const val IGNORE_ONGOING = "ignore_ongoing"
private const val ENERGY_SAVE = "energy_save"
class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val energySaverButton: Switch = view.findViewById(R.id.switch_energy_saver)
        val ignoreOngoing: Switch = view.findViewById(R.id.switch_ignore_ongoing)
        val clearAllData: Button = view.findViewById(R.id.button_clear_all_data)

        energySaverButton.isChecked = getSetting(ENERGY_SAVE)
        ignoreOngoing.isChecked = getSetting(IGNORE_ONGOING)

        clearAllData.setOnClickListener ({
            val dbHandler = DBHandler(context!!)
            dbHandler.clearAllTables()
        })

        ignoreOngoing.setOnCheckedChangeListener({ buttonView: CompoundButton, isChecked: Boolean ->
            saveSetting(IGNORE_ONGOING, isChecked) })

        energySaverButton.setOnCheckedChangeListener({ buttonView: CompoundButton, isChecked: Boolean ->
            saveSetting(ENERGY_SAVE, isChecked) })
    }

    fun saveSetting(setting: String, state: Boolean) {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(setting, state)
            commit()
        }
    }

    fun getSetting(setting: String): Boolean {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return false
        return sharedPref.getBoolean(setting, false)
    }
}