package my.journalbot.local.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.journalbot.local.R

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        title = "Help"

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}