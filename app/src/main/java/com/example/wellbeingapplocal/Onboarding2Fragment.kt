package com.example.wellbeingapplocal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wellbeingapplocal.databinding.FragmentOnboarding1Binding
import com.example.wellbeingapplocal.databinding.FragmentOnboarding2Binding


class Onboarding2Fragment : Fragment() {

    private lateinit var binding: FragmentOnboarding2Binding

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
    }
}