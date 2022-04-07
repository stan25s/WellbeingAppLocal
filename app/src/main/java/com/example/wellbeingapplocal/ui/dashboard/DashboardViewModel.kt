package com.example.wellbeingapplocal.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wellbeingapplocal.JournalEntry
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _journalEntries = MutableLiveData<ArrayList<JournalEntry>>().apply {
        value = ArrayList()
    }
    val journalEntries: LiveData<ArrayList<JournalEntry>> = _journalEntries

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
            for (i in stringArray) {
                when (i) {
                    "mq1" -> mood = stringArray[stringArray.indexOf(i) + 1]
                    "mq2" -> day_rating = stringArray[stringArray.indexOf(i) + 1]
                    "fqQ" -> focusQ = stringArray[stringArray.indexOf(i) + 1]
                    "fqA" -> focusA = stringArray[stringArray.indexOf(i) + 1]
                    "gratQ" -> gratQ = stringArray[stringArray.indexOf(i) + 1]
                    "gratA" -> gratA = stringArray[stringArray.indexOf(i) + 1]
                }
            }
            if (journalDates.contains(i)) {
                //A journal entry has already been created with this date, so we need to add the
                //Check-in data to the existing entry
                fullEntries.find{it.date == i}?.mood = mood
                fullEntries.find{it.date == i}?.day_rating = day_rating
                fullEntries.find{it.date == i}?.focusQ = focusQ
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