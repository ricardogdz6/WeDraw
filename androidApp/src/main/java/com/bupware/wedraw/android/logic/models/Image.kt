package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date

data class Image(
    var id:Int?,
    val bitmap: ByteArray,
): java.io.Serializable