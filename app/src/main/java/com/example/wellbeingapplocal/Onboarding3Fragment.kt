package com.example.wellbeingapplocal

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
import androidx.appcompat.widget.PopupMenu
import com.example.wellbeingapplocal.databinding.FragmentOnboarding1Binding
import com.example.wellbeingapplocal.databinding.FragmentOnboarding3Binding


class Onboarding3Fragment : Fragment() {

    private lateinit var binding: FragmentOnboarding3Binding

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
//        binding.imgMain.setOnClickListener(View.OnClickListener {
//            var p : PopupWindow = PopupWindow(it, 150, 100)
//            p.showAtLocation(binding.root.rootView, 0, 50,50)
//        })
        //binding.imgMain.set
        //binding.imgMain.setImageDrawable(Drawable.createFromPath("C:\\Users\\stans\\AndroidStudioProjects\\WellbeingAppLocal\\app\\src\\main\\res\\drawable-v24\\avatar_01.png"))
        //binding.editTextTextPersonName.setShadowLayer(1.0F, 5.0F, 5.0F, 4210752)
    }



}