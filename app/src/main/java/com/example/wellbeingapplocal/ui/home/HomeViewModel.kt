package com.example.wellbeingapplocal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wellbeingapplocal.Message
import com.example.wellbeingapplocal.MessageAdapter

class HomeViewModel : ViewModel() {

    //override fun

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _messages = MutableLiveData<ArrayList<Message>>().apply {
        value = ArrayList()
    }
    val messages: LiveData<ArrayList<Message>>
        get() = _messages

    fun addMessage(message: Message) {
        _messages.value?.add(message)
    }

    fun clearMessages() {
        _messages.value = ArrayList()
    }

//    private val _messageAdapter = MutableLiveData<MessageAdapter>().apply {
//        value = MessageAdapter(context = this@HomeViewModel)
//    }
//    val messageAdapter: LiveData<MessageAdapter>
//        get() = _messageAdapter
}