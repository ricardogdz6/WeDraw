package com.bupware.wedraw.android.data.tables.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey
    val id: String,
    val name: String,
    val premium:  Boolean,
    val expire_date: Date,
    )