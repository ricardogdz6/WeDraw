package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date
import java.util.TimeZone

data class Message(
    var id:Int?,
    val text: String,
    var timeZone: TimeZone?,
    var senderId: String,
    var groupId: Int,
    var date: Date?
): java.io.Serializable

