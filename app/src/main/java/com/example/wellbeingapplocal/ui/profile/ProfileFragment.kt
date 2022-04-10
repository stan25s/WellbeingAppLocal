package com.example.wellbeingapplocal.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.wellbeingapplocal.FeedbackActivity
import com.example.wellbeingapplocal.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }

        binding.nameValue.text = sharedPreferences?.getString("prefName", "Name Not Found")

        if (sharedPreferences?.getStringSet("focuses", null).isNullOrEmpty()) {
            binding.focusValue.text = "No Focuses Set"
        } else {
            val focusValues = StringBuilder()
            for (i in sharedPreferences?.getStringSet("focuses", setOf())!!) {
                focusValues.append(i)
                focusValues.append(", ")
            }
            val string = focusValues.removeRange(focusValues.length - 2, focusValues.length - 1)

            binding.focusValue.text = string
        }


        binding.prefTitle.setOnClickListener {
            //Start Preference Activity
            val intent = Intent(context, SettingsActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.helpButton.setOnClickListener {
            //Start Help Activity
            val intent = Intent(context, HelpActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.feedbackButton.setOnClickListener {
            val intent = Intent(context, FeedbackActivity::class.java)
            activity?.startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}