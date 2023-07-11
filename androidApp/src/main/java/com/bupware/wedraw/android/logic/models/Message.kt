package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date
import java.util.TimeZone

data class Message(
    var id:Long?,
    val text: String,
    var timeZone: TimeZone?,
    var senderId: String,
    var groupId: Long,
    var date: Date?
): java.io.Serializable

