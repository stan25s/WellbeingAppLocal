package com.example.wellbeingapplocal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var spinnerValue: String = "General Feedback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val spinner: Spinner = findViewById(R.id.spinner2)
        spinner.onItemSelectedListener = this
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.feedback_spinner_values,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val sendButton: Button = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            composeEmail()
        }

        title = "Feedback"

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun composeEmail() {
        val uri = Uri.parse("mailto:" + "shs1g19@soton.ac.uk")
            .buildUpon()
            .appendQueryParameter("subject", "APP MESSAGE: $spinnerValue")
            .appendQueryParameter("body",
                findViewById<EditText>(R.id.editTextFeedback).text.toString()
            )
            .build()

        val intent = Intent(Intent.ACTION_SENDTO, uri)

        startActivity(Intent.createChooser(intent, "Send Email..."))
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        spinnerValue = parent.getItemAtPosition(pos) as String
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
        spinnerValue = "General Feedback"
    }
}
