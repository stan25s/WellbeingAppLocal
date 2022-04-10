package com.example.wellbeingapplocal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.wellbeingapplocal.databinding.FragmentOnboarding1Binding


class Onboarding1Fragment : Fragment() {

    private lateinit var binding: FragmentOnboarding1Binding


    private val sharedPrefFile = "sharedprefs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboarding1Binding.inflate(layoutInflater)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    private fun setupData() {
        binding.txtMain.text = getString(R.string.first_fragment)
        binding.imgMain.setImageResource(R.drawable.robot_hello)

    }
}