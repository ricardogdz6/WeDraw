package com.bupware.wedraw.android.roomData.tables.group

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups_table")
data class Group(
    @PrimaryKey (autoGenerate = true)
    val groupId: Long = 0,
    val name: String,
    val leader_ID: String? = null,
)