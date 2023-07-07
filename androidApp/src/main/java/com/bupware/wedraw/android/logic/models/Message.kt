package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date

data class Message(
    var id:Int?,
    val text: String,
    var date: Date,
    var senderId: String
): java.io.Serializable

