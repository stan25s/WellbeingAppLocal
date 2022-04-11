package my.journalbot.local.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.journalbot.local.JournalEntry
import my.journalbot.local.MoodColour
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel : ViewModel() {

    private val _journalEntries = MutableLiveData<ArrayList<JournalEntry>>().apply {
        value = ArrayList()
    }
    val journalEntries: LiveData<ArrayList<JournalEntry>> = _journalEntries

    private val _moodValues = MutableLiveData<ArrayList<MoodColour>>().apply {
        value = ArrayList()
    }
    val moodValues: LiveData<ArrayList<MoodColour>> = _moodValues

    fun getJournalIDs(): ArrayList<String> {
        var idArrayList : ArrayList<String> = ArrayList()
        for (i in journalEntries.value!!) {
            idArrayList.add(i.id)
        }
        return idArrayList
    }

    fun readFilesAndUpdate(context: Context) {
        var files: ArrayList<File> = ArrayList()

        var journalDates: ArrayList<String> = ArrayList()
        var checkinDates: ArrayList<String> = ArrayList()

        var fullEntries: ArrayList<JournalEntry> = ArrayList()

        File(context.filesDir.toURI()).walk().forEach {
            files.add(it)
        }

        for(i in files) {
            if(i.name.contains("journal")) {
                journalDates.add(i.nameWithoutExtension.split("_")[1])
            } else if(i.name.contains("checkin")) {
                checkinDates.add(i.nameWithoutExtension.split("_")[1])
            }
        }

        //Iterate through journal files found to collect all of the journal entries
        for(i in journalDates) {
            //var journalText: String
            var fileInputStream: FileInputStream? = null
            fileInputStream = context.openFileInput("journal_$i.txt")
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null

            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }

            fullEntries.add(JournalEntry("journal_$i", i, "", "", "", "", "", "", stringBuilder.toString()))
        }

        for(i in checkinDates) {
            //Reading file from internal storage
            var fileInputStream: FileInputStream? = null
            fileInputStream = context.openFileInput("checkin_$i.txt")
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null

            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }

            //Parsing input string into individual values
            var inputString = stringBuilder.toString()

            var mood = ""
            var day_rating = ""
            var gratQ = ""
            var gratA = ""
            var focusQ = ""
            var focusA = ""

            var stringArray = inputString.split("{{")
            for (j in stringArray) {
                when (j) {
                    "mq1" -> mood = stringArray[stringArray.indexOf(j) + 1]
                    "mq2" -> day_rating = stringArray[stringArray.indexOf(j) + 1]
                    "fqQ" -> focusQ = stringArray[stringArray.indexOf(j) + 1]
                    "fqA" -> focusA = stringArray[stringArray.indexOf(j) + 1]
                    "gratQ" -> gratQ = stringArray[stringArray.indexOf(j) + 1]
                    "gratA" -> gratA = stringArray[stringArray.indexOf(j) + 1]
                }
            }
            //Check if mood value populated, and if so add to moodValues value
            if (mood != "" && mood.isNotEmpty()) {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.UK)
                val date = LocalDate.parse(i, formatter)
                val dateDate = Date.from(date.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant())
                val simpleDateFormat = SimpleDateFormat("EEE", Locale.UK).format(dateDate)

                val dayCode = simpleDateFormat
                _moodValues.value?.add(MoodColour(mood, dayCode.toString(), date))
            }

            if (journalDates.contains(i)) {
                //A journal entry has already been created with this date, so we need to add the
                //Check-in data to the existing entry
                fullEntries.find{it.date == i}?.mood = mood
                fullEntries.find{it.date == i}?.day_rating = day_rating
                fullEntries.find{it.date == i}?.focusQ = focusQ
                fullEntries.find{it.date == i}?.focusA = focusA
                fullEntries.find{it.date == i}?.gratitude_question = gratQ
                fullEntries.find{it.date == i}?.gratitude_answer = gratA
            } else {
                //Otherwise add a new entry without a journal variable
                fullEntries.add(JournalEntry("journal_$i", i, mood, day_rating, gratQ, gratA, focusQ, focusA, ""))
            }

            _journalEntries.value = fullEntries
        }
    }
}