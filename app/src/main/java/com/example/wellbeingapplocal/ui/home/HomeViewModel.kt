package com.example.wellbeingapplocal.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wellbeingapplocal.Message
import java.io.FileOutputStream
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.StringBuilder

class HomeViewModel : ViewModel() {

    //Use this pending message to workaround message failure bug that occurs when closing tab during
    // message sending.
    //Update this when sending a message and then if interrupted, set flag.

    //HomeFragment is re-opened, check if interrupted flag is set, and if so retrieve the pending
    //message and re-send.
    private val _pendingMessage = MutableLiveData<Message>().apply {
        value = null
    }
    val pendingMessage: LiveData<Message> = _pendingMessage

    private val _pendingMessageInterrupted = MutableLiveData<Boolean>().apply {
        value = false
    }
    val pendingMessageInterrupted: LiveData<Boolean> = _pendingMessageInterrupted

    //pendingMessage Functions
    fun newPendingMessage(message: Message) {
        _pendingMessage.postValue(message)
        _pendingMessageInterrupted.postValue(false)
    }

    fun setInterruptedFlag() {
        _pendingMessageInterrupted.postValue(true)
    }
    fun clearInterruptedFlag() {
        _pendingMessageInterrupted.postValue(false)
    }

    private val _lastSavedSessionAnswers = MutableLiveData<String>().apply {
        value = ""
    }
    val lastSavedSessionAnswers: LiveData<String> = _lastSavedSessionAnswers

    private val _lastSavedSessionJournal = MutableLiveData<String>().apply {
        value = ""
    }
    val lastSavedSessionJournal: LiveData<String> = _lastSavedSessionJournal

    fun updateLastSavedSessionAnswers(session: String) {
        _lastSavedSessionAnswers.postValue(session)
    }

    fun updateLastSavedSessionJournal(session: String) {
        _lastSavedSessionJournal.postValue(session)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _journal = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val journal: LiveData<String> = _journal

    private val _checkinData = MutableLiveData<MutableMap<String,String>>().apply {
        value = mutableMapOf()
    }
    val checkInData: LiveData<MutableMap<String,String>> = _checkinData

    private val _messages = MutableLiveData<ArrayList<Message>>().apply {
        value = ArrayList()
    }
    val messages: LiveData<ArrayList<Message>>
        get() = _messages

    //Add message to message list
    fun addMessage(message: Message) {
        _messages.value?.add(message)
    }

    //Clear all messages
    fun clearMessages() {
        _messages.value?.clear()
    }

    //Add value to checkinData map
    fun addAnswerToMap(question: String, answer: String) {
        _checkinData.value?.put(question, answer)
    }

    fun removeAnswerFromMap(question: String) {
        _checkinData.value?.remove(question)
    }

    //Store journal entry as string
    fun setJournalString(input: String) {
        _journal.value = input
    }

    fun saveJournalToFile(context: Context) {
//        val sharedPrefFile = "sharedprefs"
//        val sharedPreferences: SharedPreferences = context
//            .getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val user = sharedPreferences.getString("userID", "null")


        //save to file
        val fileName =
            "journal_" + getDate(Calendar.getInstance().timeInMillis,
                "dd-MM-yyyy") + ".txt"
        val fileOutputStream : FileOutputStream

        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(_journal.value!!.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun clearJournal() {
        _journal.postValue("")
    }

    fun saveAnswersToFile(context: Context) {
        //generate string from map
        val sb : StringBuilder = StringBuilder()
        println("SAVING ANSWERS TO FILE:")
        for(i in _checkinData.value?.keys!!) {
            println("$i : " + _checkinData.value!![i])
            sb.append("{{")
            sb.append(i)
            sb.append("{{")
            sb.append(_checkinData.value!![i])
            sb.append("{{")
            sb.appendLine()
        }
        val stringToWrite = sb.toString()

        //save to file
        val fileName = "checkin_" + getDate(Calendar.getInstance().timeInMillis, "dd-MM-yyyy") + ".txt"
        val fileOutputStream : FileOutputStream

        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(stringToWrite.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //_checkinData.value?.clear()
    }

    fun clearAnswerMap() {
        _checkinData.value?.clear()
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.UK)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

//    private val _messageAdapter = MutableLiveData<MessageAdapter>().apply {
//        value = MessageAdapter(context = this@HomeViewModel)
//    }
//    val messageAdapter: LiveData<MessageAdapter>
//        get() = _messageAdapter
}