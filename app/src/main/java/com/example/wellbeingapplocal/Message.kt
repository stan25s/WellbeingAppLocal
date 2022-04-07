package com.example.wellbeingapplocal

import org.json.JSONObject

data class Message(
    var user: String,
    var message: String,
    var time: Long,
    var session:String,
    var focus: String,
    var mq1: String,
    var mq2: String,
    var gratQ: String,
    var gratA: String,
    var fq1: String
    )
