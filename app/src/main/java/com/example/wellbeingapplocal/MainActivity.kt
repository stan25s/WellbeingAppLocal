package com.example.wellbeingapplocal

import android.content.Context
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
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    //private var sharedPrefsFile = "sharedprefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//TODO: If Session Variable in sharedPrefs is not set; instantiate with unique value for user.

        newSession()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications
            )
        )
        navView.setupWithNavController(navController)

        //Set up NotifyMe builder for notifications
        val notifyMe = NotifyMe.Builder(applicationContext)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        //Get user preference on reminders (true by default)
        val remindersEnabled = sharedPreferences.getBoolean("reminders-enabled", true)
        val reminderFrequency = sharedPreferences.getString("reminder-frequency", "daily")

        TODO("Set up builder NotifyMe builder with values from shared prefs")

//        notifyMe.title(String title);
//        notifyMe.content(String content);
//        notifyMe.color(Int red,Int green,Int blue,Int alpha);//Color of notification header
//        notifyMe.led_color(Int red,Int green,Int blue,Int alpha);//Color of LED when notification pops up
//        notifyMe.time(Calendar time);//The time to popup notification
//        notifyMe.delay(Int delay);//Delay in ms
//        notifyMe.large_icon(Int resource);//Icon resource by ID
//        notifyMe.rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2")//RRULE for frequency of notification
//        notifyMe.addAction(Intent intent,String text); //The action will call the intent when pressed


        supportActionBar?.hide()

    }

    override fun onStop() {
        super.onStop()
        clearSession()
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

    public fun newSession() {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        //Get User ID from sharedPrefs + append _ and current time to create sessionID
        val userID = sharedPreferences.getString("userID", "");
        val time = Calendar.getInstance().timeInMillis
        var sessionId: String
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