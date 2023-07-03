package com.bupware.wedraw.android.data.tables.image

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val id: Long,
    val bitmap: ByteArray
)