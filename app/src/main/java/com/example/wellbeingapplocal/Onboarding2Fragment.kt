package com.example.wellbeingapplocal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.wellbeingapplocal.databinding.FragmentOnboarding1Binding
import com.example.wellbeingapplocal.databinding.FragmentOnboarding2Binding
import com.example.wellbeingapplocal.databinding.FragmentOnboarding3Binding
import java.util.ArrayList


class Onboarding2Fragment : Fragment() {

    private lateinit var binding: FragmentOnboarding2Binding

    //var focusTopic: ArrayList<String> = ArrayList()

    private val sharedPrefFile = "sharedprefs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboarding2Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    private fun setupData() {
        binding.txtMain.text = getString(R.string.second_fragment)
        binding.imgMain.setImageResource(R.mipmap.ic_launcher)

        binding.toggleButton1.setOnClickListener {
            var textVal = binding.toggleButton1.textOff.toString()
            var selected = binding.toggleButton1.isChecked
            var focusTopic = (activity as OnBoarding).selectedFocus

            //Add the selected topic to the focus list, and don't allow more than 3 selections
            if (selected) {
                if (focusTopic.size < 3) {
                    focusTopic.add(textVal)
                } else {
                    binding.toggleButton1.isChecked = false
                }
            } else {
                focusTopic.remove(textVal)
            }

            (activity as OnBoarding).selectedFocus = focusTopic
        }

        binding.toggleButton2.setOnClickListener {
            var textVal = binding.toggleButton2.textOff.toString()
            var selected = binding.toggleButton2.isChecked
            var focusTopic = (activity as OnBoarding).selectedFocus

            //Add the selected topic to the focus list, and don't allow more than 3 selections
            if (selected) {
                if (focusTopic.size < 3) {
                    focusTopic.add(textVal)
                } else {
                    binding.toggleButton2.isChecked = false
                }
            } else {
                focusTopic.remove(textVal)
            }

            (activity as OnBoarding).selectedFocus = focusTopic
        }

        binding.toggleButton3.setOnClickListener {
            var textVal = binding.toggleButton3.textOff.toString()
            var selected = binding.toggleButton3.isChecked
            var focusTopic = (activity as OnBoarding).selectedFocus

            //Add the selected topic to the focus list, and don't allow more than 3 selections
            if (selected) {
                if (focusTopic.size < 3) {
                    focusTopic.add(textVal)
                } else {
                    binding.toggleButton3.isChecked = false
                }
            } else {
                focusTopic.remove(textVal)
            }

            (activity as OnBoarding).selectedFocus = focusTopic
        }

        binding.toggleButton4.setOnClickListener {
            var textVal = binding.toggleButton4.textOff.toString()
            var selected = binding.toggleButton4.isChecked
            var focusTopic = (activity as OnBoarding).selectedFocus

            //Add the selected topic to the focus list, and don't allow more than 3 selections
            if (selected) {
                if (focusTopic.size < 3) {
                    focusTopic.add(textVal)
                } else {
                    binding.toggleButton4.isChecked = false
                }
            } else {
                focusTopic.remove(textVal)
            }

            (activity as OnBoarding).selectedFocus = focusTopic
        }

        binding.toggleButton5.setOnClickListener {
            var textVal = binding.toggleButton5.textOff.toString()
            var selected = binding.toggleButton5.isChecked
            var focusTopic = (activity as OnBoarding).selectedFocus

            //Add the selected topic to the focus list, and don't allow more than 3 selections
            if (selected) {
                if (focusTopic.size < 3) {
                    focusTopic.add(textVal)
                } else {
                    binding.toggleButton5.isChecked = false
                }
            } else {
                focusTopic.remove(textVal)
            }

            (activity as OnBoarding).selectedFocus = focusTopic
        }
    }
}