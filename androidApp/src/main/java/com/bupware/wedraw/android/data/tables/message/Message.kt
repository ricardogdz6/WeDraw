package com.bupware.wedraw.android.data.tables.message

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bupware.wedraw.android.data.tables.group.Group
import com.bupware.wedraw.android.data.tables.user.User
import java.sql.Date


@Entity(tableName = "messages_table",foreignKeys =  [
    ForeignKey(entity = Group::class, parentColumns = ["groupId"], childColumns = ["messageGroupID"]),
    ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["senderID"])
]

)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val messageGroupID: Long,
    val senderID: String,
    val text: String,
    val imageID: Long?= null,
    val date: Date?
)


