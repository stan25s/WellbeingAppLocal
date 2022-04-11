package my.journalbot.local

import java.time.LocalDate

data class MoodColour(
    var mood: String,
    var dayCode: String,
    var date: LocalDate
    )