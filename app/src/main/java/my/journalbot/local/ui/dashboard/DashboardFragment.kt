package my.journalbot.local.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import my.journalbot.local.JournalAdapter
import my.journalbot.local.MoodColour
import my.journalbot.local.R
import my.journalbot.local.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JournalAdapter

    private lateinit var viewModel: DashboardViewModel

    private lateinit var moodColourArray: ArrayList<MoodColour>

    private var moodStringMap: MutableMap<String, String> = mutableMapOf()

    private var dateCurrentMonday: String = ""


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.journalRecycler
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        context?.let { viewModel.readFilesAndUpdate(it) }

        viewModel.journalEntries.observe(viewLifecycleOwner) { journalEntries ->
            if (adapter.itemCount > 0) {
                binding.noItemsText.visibility = View.GONE
            }
            val difference = journalEntries.size - adapter.itemCount
            if (difference < 0) {
                adapter.updateJournalEntries(journalEntries)
            } else if (difference > 0) {
                for (i in journalEntries) {
                    if (!adapter.getJournalIDs().contains(i.id)) {
                        adapter.addJournalEntry(i)
                    }
                }
            }
        }

        adapter = context?.let { JournalAdapter(it) }!!
        recyclerView.adapter = adapter
        if (adapter.itemCount > 0) {
            binding.noItemsText.visibility = View.GONE
        } else {
            binding.noItemsText.visibility = View.VISIBLE
        }

        viewModel.moodValues.observe(viewLifecycleOwner) { moodValues ->
            moodColourArray = moodValues
            getMoodsOverWeek()
        }

        moodColourArray = viewModel.moodValues.value!!
        getMoodsOverWeek()

    }

    private fun getMoodsOverWeek() {
        moodStringMap.clear()
        val currentDateInstant = Instant.now()
        val currentDate = LocalDate.now()
        val currentDayOfWeek = SimpleDateFormat("EEE", Locale.UK).format(Date.from(currentDate.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()))
        println("Current Date: $currentDate")
        println("Current Day: $currentDayOfWeek")

        var diff: Long = 0

        when (currentDayOfWeek) {
            "Mon" -> diff = 0
            "Tue" -> diff = 1
            "Wed" -> diff = 2
            "Thu" -> diff = 3
            "Fri" -> diff = 4
            "Sat" -> diff = 5
            "Sun" -> diff = 6
            else -> println("Issue Processing Date")
        }

        val mondayInstant = currentDateInstant.minus(diff, ChronoUnit.DAYS)
        val mondayDate = Date.from(mondayInstant)
        val mondayLocalDate = mondayDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        dateCurrentMonday = SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(mondayDate)

        println("This Monday Date: $mondayDate")
        println("This Monday Local Date: $mondayLocalDate")

        for (i in moodColourArray) {
            if (i.date >= mondayLocalDate) {
                //Get Date as dayCode, e.g. "Tue"
                when (SimpleDateFormat("EEE", Locale.UK).format(Date.from(i.date.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()))) {
                    "Mon" -> moodStringMap["Mon"] = i.mood
                    "Tue" -> moodStringMap["Tue"] = i.mood
                    "Wed" -> moodStringMap["Wed"] = i.mood
                    "Thu" -> moodStringMap["Thu"] = i.mood
                    "Fri" -> moodStringMap["Fri"] = i.mood
                    "Sat" -> moodStringMap["Sat"] = i.mood
                    "Sun" -> moodStringMap["Sun"] = i.mood
                    else -> println("Issue Processing Date")
                }
            }
        }

        if(moodStringMap.isEmpty()) {
            binding.noMoodItemsText.visibility = View.VISIBLE
            binding.daysView.visibility = View.GONE
        } else {
            binding.noMoodItemsText.visibility = View.GONE
            binding.daysView.visibility = View.VISIBLE
            context?.let { setMoodColours(it) }
        }
    }

    private fun setMoodColours(context : Context) {
        //Set all squares to default values first:
        binding.moodColour1.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))
        binding.moodColour2.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))
        binding.moodColour3.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))
        binding.moodColour4.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))
        binding.moodColour5.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))
        binding.moodColour6.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))
        binding.moodColour7.setBackgroundColor(ContextCompat.getColor(context, R.color.nodata))

        //Then iterate through map to get values
        for (i in moodStringMap.keys) {
            //var dayCode = ""
            var moodColour : View = when (i) {
                "Mon" -> binding.moodColour1
                "Tue" -> binding.moodColour2
                "Wed" -> binding.moodColour3
                "Thu" -> binding.moodColour4
                "Fri" -> binding.moodColour5
                "Sat" -> binding.moodColour6
                "Sun" -> binding.moodColour7
                else -> binding.moodColour1
            }
            when (moodStringMap[i]) {
                "Happy" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.happy))
                "Calm" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.calm))
                "Powerful" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.powerful))
                "Sad" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.sad))
                "Angry" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.angry))
                "Anxious" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.anxious))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}