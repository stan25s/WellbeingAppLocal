package com.example.wellbeingapplocal.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.wellbeingapplocal.R
import com.example.wellbeingapplocal.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.settings_activity)

        title = "Preferences"

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val focuses = sharedPreferences.getStringSet("focuses", setOf())
        if (focuses?.isNotEmpty() == true) {
            if (focuses.contains("anxiety")) {
                sharedPreferences.edit().putBoolean("anxiety_selected", true).apply()
            }
            if (focuses.contains("motivation")) {
                sharedPreferences.edit().putBoolean("motivation_selected", true).apply()
            }
            if (focuses.contains("energy")) {
                sharedPreferences.edit().putBoolean("energy_selected", true).apply()
            }
            if (focuses.contains("depression")) {
                sharedPreferences.edit().putBoolean("depression_selected", true).apply()
            }
        }
        //OnSharedPreferenceChangeListener used to check if too many focuses are selected in
        // multi-select and removes the most-recently selected
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key.contains("anxiety")
                    || key.contains("motivation")
                    || key.contains("energy")
                    || key.contains("depression")) {

                        if (sharedPreferences.getBoolean(key, false)) {
                            var selectedCount = 0
                            if (sharedPreferences.getBoolean("anxiety_selected", false)) {
                                selectedCount += 1
                            }
                            if (sharedPreferences.getBoolean("motivation_selected", false)) {
                                selectedCount += 1
                            }
                            if (sharedPreferences.getBoolean("depression_selected", false)) {
                                selectedCount += 1
                            }
                            if (sharedPreferences.getBoolean("anxiety", false)) {
                                selectedCount += 1
                            }
                            if (selectedCount < 3) {
                                val set = sharedPreferences.getStringSet("focuses", setOf())
                                val newSet = mutableSetOf<String>()
                                if (set != null) {
                                    for (i in set) {
                                        newSet.add(i)
                                    }
                                }
                                newSet.add(key.split("_")[0])
                                sharedPreferences.edit().putStringSet(
                                    "focuses",
                                    newSet
                                ).apply()
                            } else {
                                sharedPreferences.edit().putBoolean(key, false).apply()
                            }
                        } else {
                            val set: MutableSet<String> = mutableSetOf()
                            //val collection: MutableCollection<String> = setOf()
                            sharedPreferences.getStringSet("focuses", setOf())?.toList()
                                ?.let { set.addAll(it) }

                            set.remove(key.split("_")[0])

                            sharedPreferences.edit().putStringSet("focuses", set).apply()

                        }


                }

            }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);



    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            //PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}