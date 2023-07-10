package com.bupware.wedraw.android.data.tables.relationTables.messageWithImage

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.message.Message

data class MessageWithImage(
    @Embedded val image: Image,
    @Relation(
        parentColumn = "id",
        entityColumn = "image_Id"
    )
    val message: Message
)




