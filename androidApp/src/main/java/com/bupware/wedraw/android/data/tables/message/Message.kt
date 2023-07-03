package com.bupware.wedraw.android.data.tables.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bupware.wedraw.android.data.tables.relationTables.Users_groups
import com.bupware.wedraw.android.data.tables.image.Image
import java.sql.Date


@Entity(tableName = "message_table", foreignKeys = [
    androidx.room.ForeignKey(
        entity = Users_groups::class,
        parentColumns = ["id"],
        childColumns = ["user_groups_id"],
        onDelete = androidx.room.ForeignKey.CASCADE
    ),
    androidx.room.ForeignKey(
        entity = Image::class,
        parentColumns = ["id"],
        childColumns = ["bitmap_id"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )
])
data class Message(
    @PrimaryKey val id: Long,
    val user_groups_id: String,
    val text: String,
    val bitmap_id: Long,
    val date: Date
)
