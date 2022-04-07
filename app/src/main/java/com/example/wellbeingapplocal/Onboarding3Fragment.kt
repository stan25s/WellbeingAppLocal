package com.example.wellbeingapplocal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.example.wellbeingapplocal.databinding.FragmentOnboarding1Binding
import com.example.wellbeingapplocal.databinding.FragmentOnboarding3Binding


class Onboarding3Fragment : Fragment() {

    private lateinit var binding: FragmentOnboarding3Binding

    //private val sharedPrefFile = "sharedprefs"

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private var chosenAvatar: String = "";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboarding3Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    private fun setupData() {
        binding.txtMain.text = getString(R.string.third_fragment)
        binding.imgMain.setImageResource(R.mipmap.avatar01_round)
        binding.button.setOnClickListener {
            if (!binding.editTextName.text.isNullOrEmpty() && binding.editTextName.text.length >= 2) {
                //Toast.makeText(it.context, "Submitted!", Toast.LENGTH_SHORT).show()
                val intent = Intent(it.context, MainActivity::class.java)

                //Get user pref name from text box
                val prefName = binding.editTextName.text.toString()

                //Save to shared preferences
                val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                //Create new userID
                val randomString = (1..8)
                    .map { charPool.random() }
                    .joinToString("")

                editor.putString("prefName", prefName)
                editor.putStringSet("focuses", (activity as OnBoarding).selectedFocus.toSet())
                editor.putString("userID", randomString)
                editor.apply()

                //Start Main Activity, and pass user info as intent
                startActivity(intent)
                Toast.makeText(it.context, "Success!", Toast.LENGTH_SHORT).show()

                activity?.finish()
            } else {
                Toast.makeText(it.context, "Please Enter your Name Before Continuing", Toast.LENGTH_SHORT).show()
            }
        }
    }
}