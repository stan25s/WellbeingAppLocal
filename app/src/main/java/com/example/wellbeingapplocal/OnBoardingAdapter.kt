//package com.example.wellbeingapplocal
//
//import android.content.Context
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.example.wellbeingapplocal.ui.home.HomeFragment
//import com.example.wellbeingapplocal.databinding.Onboarding1Binding
//
//
//class OnBoardingAdapter(private val context: Context,
//                        private val labelList: MutableList<String>,
//                        private val colorList: MutableList<String>) :
//    RecyclerView.Adapter<OnBoardingAdapter.ViewPagerHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
//
//        val binding = Onboarding1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewPagerHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
//        val label = labelList[position]
//        val color = colorList[position]
//        holder.bind(label, color)
//    }
//
//    override fun getItemCount(): Int {
//        return labelList.size
//    }
//
//    class ViewPagerHolder(private var onboarding1Binding: Onboarding1Binding) :
//        RecyclerView.ViewHolder(onboarding1Binding.root) {
//        fun bind(label: String, color: String) {
//            onboarding1Binding.label.text = label
//            onboarding1Binding.root.setBackgroundColor(Color.parseColor(color))
//        }
//    }
//}