package com.example.wellbeingapplocal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.wellbeingapplocal.databinding.ActivityOnboardingBinding

class OnBoarding : FragmentActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerA()
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
        list.add("Page1")
        list.add("Page2")
        list.add("Page3")
        list.add("Page4")

        val colorList: MutableList<String> = ArrayList()
        colorList.add("#f0f0f0")
//        colorList.add("#f0f0f0")
//        colorList.add("#f0f0f0")
//        colorList.add("#f0f0f0")

        // Set adapter to viewPager.
        //binding.viewPager.adapter = OnBoardingAdapter(this, list, colorList)

    }

    private fun setupViewPagerA() {
        //val viewPager2 = binding.viewPager
        binding.viewPager.adapter = ViewPagerAdapter(this)
    }

}