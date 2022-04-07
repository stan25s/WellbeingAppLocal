package com.example.wellbeingapplocal

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.journal_item.view.*

class JournalAdapter (val context: Context) : RecyclerView.Adapter<JournalHolder>() {
    private var journalEntries: ArrayList<JournalEntry> = ArrayList()

    fun addJournalEntry(journalEntry: JournalEntry) {
        journalEntries.add(journalEntry)
        notifyDataSetChanged()
    }

    fun getJournalIDs(): ArrayList<String> {
        var idArrayList : ArrayList<String> = ArrayList()
        for (i in journalEntries) {
            idArrayList.add(i.id)
        }
        return idArrayList
    }

    fun updateJournalEntries(newJournalEntries: ArrayList<JournalEntry>) {
        journalEntries = newJournalEntries
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalHolder {
        return NewJournalHolder(
            LayoutInflater.from(context).inflate(R.layout.journal_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return journalEntries.size
    }

    override fun onBindViewHolder(holder: JournalHolder, position: Int) {
        val journalEntry = journalEntries[position]

        holder.bind(journalEntry)
    }

    inner class NewJournalHolder (view: View) : JournalHolder(view) {
        private var date: TextView = view.date
        private var dayRating: TextView = view.day_rating_value
        private var gratitudeQ: TextView = view.gratitude_question
        private var gratitudeA: TextView = view.gratitude_answer
        private var focusQ: TextView = view.focus_question
        private var focusA: TextView = view.focus_answer
        private var journalText: TextView = view.journal_text

        private var moodColor: View = view.mood_colour

        override fun bind(journalEntry:JournalEntry) {
            date.text = journalEntry.date
            dayRating.text = journalEntry.day_rating
            gratitudeQ.text = journalEntry.gratitude_question
            gratitudeA.text = journalEntry.gratitude_answer
            focusQ.text = journalEntry.focusQ
            focusA.text = journalEntry.focusA
            journalText.text = journalEntry.journal_entry
            when (journalEntry.mood) {
                "Happy" -> moodColor.setBackgroundColor(ContextCompat.getColor(context, R.color.happy))
                "Calm" -> moodColor.setBackgroundColor(ContextCompat.getColor(context, R.color.calm))
                "Powerful" -> moodColor.setBackgroundColor(ContextCompat.getColor(context, R.color.powerful))
                "Sad" -> moodColor.setBackgroundColor(ContextCompat.getColor(context, R.color.sad))
                "Angry" -> moodColor.setBackgroundColor(ContextCompat.getColor(context, R.color.angry))
                "Anxious" -> moodColor.setBackgroundColor(ContextCompat.getColor(context, R.color.anxious))
            }
        }
    }
}

open class JournalHolder (view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(journalEntry: JournalEntry) {}
}

