package com.bupware.wedraw.android.data.tables.group

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_table")
data class Group(
    @PrimaryKey val id: Long,
    val name: String,
    val leader_ID: Long
)
