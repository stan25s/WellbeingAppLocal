package com.example.wellbeingapplocal

data class JournalEntry(
    var id: String,
    var date: String,
    var mood: String,
    var day_rating: String,
    var gratitude_question: String,
    var gratitude_answer: String,
    var focusQ: String,
    var focusA: String,
    var journal_entry: String
)
