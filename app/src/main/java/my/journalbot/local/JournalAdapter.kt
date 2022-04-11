package my.journalbot.local

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import my.journalbot.local.R
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
        private var focusView: ConstraintLayout = view.focusQ_view
        private var gratitudeView: ConstraintLayout = view.gratitude_view

        private var moodColour: View = view.mood_colour

        override fun bind(journalEntry: JournalEntry) {
            date.text = journalEntry.date
            dayRating.text = journalEntry.day_rating

            //If gratitude question does not exist, make this not visible in the dashboard page
            if(journalEntry.gratitude_question == "" || journalEntry.gratitude_question.isEmpty()) {
                gratitudeView.visibility = View.GONE
//                gratitudeQ.visibility = View.GONE
//                gratitudeA.visibility = View.GONE
            } else {
                gratitudeQ.text = journalEntry.gratitude_question
                gratitudeA.text = journalEntry.gratitude_answer
            }

            //If focus question does not exist, make this not visible in the dashboard page
            if(journalEntry.focusQ == "" || journalEntry.focusQ.isEmpty()) {
                focusView.visibility = View.GONE
            } else {
                focusQ.text = journalEntry.focusQ
                focusA.text = journalEntry.focusA
            }

            journalText.text = journalEntry.journal_entry
            when (journalEntry.mood) {
                "Happy" -> moodColour.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.happy
                ))
                "Calm" -> moodColour.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.calm
                ))
                "Powerful" -> moodColour.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.powerful
                ))
                "Sad" -> moodColour.setBackgroundColor(ContextCompat.getColor(context, R.color.sad))
                "Angry" -> moodColour.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.angry
                ))
                "Anxious" -> moodColour.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.anxious
                ))
            }
        }
    }
}

open class JournalHolder (view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(journalEntry: JournalEntry) {}
}

