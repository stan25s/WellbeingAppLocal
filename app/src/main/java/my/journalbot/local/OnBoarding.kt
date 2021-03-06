package my.journalbot.local

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import my.journalbot.local.databinding.ActivityOnboardingBinding

class OnBoarding : FragmentActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    var selectedFocus : ArrayList<String> = ArrayList()

    private val sharedPrefFile = "sharedprefs"

    private val debugMode = false

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

    private fun setupViewPagerA() {

        /*
          If user has already completed onboarding (check sharedpref file for user settings),
          then skip onboarding and start main activity.
         */
        val sharedPreferences: SharedPreferences? =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userExists = sharedPreferences?.contains("prefName") == true && !sharedPreferences?.getString("prefName", null).isNullOrEmpty()



        if (userExists && !debugMode) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {

            //else if prefName wasn't found, set up onboarding ViewPager
            binding.viewPager.id
            binding.viewPager.adapter = ViewPagerAdapter(this)

        }

    }

}