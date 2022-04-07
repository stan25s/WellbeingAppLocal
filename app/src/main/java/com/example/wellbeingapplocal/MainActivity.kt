package com.example.wellbeingapplocal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wellbeingapplocal.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private var sharedPrefsFile = "sharedprefs"

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
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



    }

    override fun onStop() {
        super.onStop()
        clearSession()
        //Use this to clear session variable
        //TODO: Delete Session Variable from sharedPrefs
    }

    private fun clearSession() {
        val sharedPreferences: SharedPreferences = this.applicationContext
            .getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("sessionId", "");
        //editor.apply()
        editor.apply()
    }

    public fun newSession() {
        val sharedPreferences: SharedPreferences = this.applicationContext
            .getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)

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