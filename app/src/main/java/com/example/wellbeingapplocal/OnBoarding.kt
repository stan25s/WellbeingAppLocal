package com.example.wellbeingapplocal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.viewpager2.widget.ViewPager2
import com.example.wellbeingapplocal.databinding.ActivityOnboardingBinding

class OnBoarding : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager2()
    }

    override fun onBackPressed() {
        val viewPager = binding.viewPager
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }


    private fun setupViewPager2() {
        val list: MutableList<String> = ArrayList()
        list.add("This is your First Screen")
        list.add("This is your Second Screen")
        list.add("This is your Third Screen")
        list.add("This is your Fourth Screen")

        val colorList: MutableList<String> = ArrayList()
        colorList.add("#f0f0f0")
        colorList.add("#f0f0f0")
        colorList.add("#f0f0f0")
        colorList.add("#f0f0f0")

        // Set adapter to viewPager.
        binding.viewPager.adapter = OnBoardingAdapter(this, list, colorList)

    }
}