package com.example.wellbeingapplocal

import android.app.Application

class ChatApp: Application() {
    companion object {
        lateinit var user:String
        const val botUser = "bot"
    }
}