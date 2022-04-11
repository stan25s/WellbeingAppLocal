package my.journalbot.local

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val count = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Onboarding1Fragment()
            1 -> Onboarding2Fragment()
            2 -> Onboarding3Fragment()
            else -> Onboarding1Fragment()
        }
    }

    override fun getItemCount(): Int {
        return count
    }

    private fun getPageTitle(position: Int): CharSequence? {
        return "Tab " + (position + 1)
    }
}