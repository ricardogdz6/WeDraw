package com.bupware.wedraw.android.logic.dataclasses

import java.io.Serializable
import java.util.Date

data class User(
    val email: String,
    var username: String?,
    var premium: Boolean,
    var expire_date: Date?
):Serializable