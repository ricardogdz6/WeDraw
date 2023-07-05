package com.bupware.wedraw.android.data.tables.image

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey
    val id: Long,
    val uri: String
)