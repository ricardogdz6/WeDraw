package com.bupware.wedraw.android.data.tables.relationTables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bupware.wedraw.android.data.tables.group.Group
import com.bupware.wedraw.android.data.tables.relationTables.user.User

@Entity(
    tableName = "users_groups", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Group::class,
        parentColumns = ["id"],
        childColumns = ["group_id"],
        onDelete = ForeignKey.CASCADE
    )
    ]
)
data class Users_groups(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val user_id: String,
    val group_id: Long
)