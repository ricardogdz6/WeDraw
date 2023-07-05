package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date

data class Group(
    var id:Int?,
    val name: String,
    var code: String
): java.io.Serializable