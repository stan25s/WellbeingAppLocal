package com.example.wellbeingapplocal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.allyants.notifyme.NotifyMe
import com.example.wellbeingapplocal.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Instant
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private lateinit var alarmManager: AlarmManager
    private val alarmPendingIntent by lazy {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    private var hourToShowReminder: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newSession()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        //hide actionBar
        supportActionBar?.hide()


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        //Get alarm manager, and schedule initial
        alarmManager = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        hourToShowReminder = sharedPreferences.getString("reminder-time", "20")?.toInt()!!
        schedulePushNotifications()

        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "reminder-time") {
                //If reminder time has been changed, get the new value and schedule a new alarm for reminder notifications

                hourToShowReminder = sharedPreferences.getString(key, "20")?.toInt()!!
                //alarmManager.cancel(alarmPendingIntent)
                schedulePushNotifications()
            } else if (key == "reminders-enabled") {
                if (sharedPreferences.getBoolean("reminders-enabled", false)) {
                    schedulePushNotifications()
                }
                //alarmManager.cancel(alarmPendingIntent)
            } else if (key == "reminder-frequency") {
                schedulePushNotifications()
            }
        }
    }

    fun schedulePushNotifications() {
        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= hourToShowReminder) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, hourToShowReminder)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var interval: Long = AlarmManager.INTERVAL_DAY
        val frequency = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString("reminder-frequency", "daily")

        when (frequency) {
            "daily" -> interval = AlarmManager.INTERVAL_DAY
            "2_daily" -> interval = AlarmManager.INTERVAL_DAY * 2
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 2,
            alarmPendingIntent
        )
    }

    override fun onStop() {
        super.onStop()
        //clearSession()
        //Use this to clear session variable
        //TODO: Delete Session Variable from sharedPrefs
    }

    private fun clearSession() {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sharedPreferences.edit()
        editor.putString("sessionId", "");
        //editor.apply()
        editor.apply()
    }

    fun newSession() {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        //Get User ID from sharedPrefs + append _ and current time to create sessionID
        val userID = sharedPreferences.getString("userID", "");
        val time = Calendar.getInstance().timeInMillis
        val sessionId: String
        if(!userID.isNullOrEmpty()) {
            sessionId = userID + "_" + time.toString()
        } else {
            sessionId = "testUser_" + time.toString()
        }

        val editor = sharedPreferences.edit()
        editor.putString("sessionId", sessionId);
        //editor.apply()
        editor.apply()
    }
}