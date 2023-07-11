package com.bupware.wedraw.android.data.tables.relationTables.messageWithImage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bupware.wedraw.android.data.tables.image.ImageDao
import com.bupware.wedraw.android.data.tables.message.Message
import com.bupware.wedraw.android.data.tables.message.MessageDao

@Dao
interface MessageWithImageDao : MessageDao, ImageDao {
    @Transaction
    @Query("SELECT * FROM messages_table INNER JOIN images_table ON messages_table.id = images_table.id WHERE messages_table.id = :messagesId")
    fun getMessageWithImage(messagesId: Long): MessageWithImage

    @Transaction
    suspend fun insertMessageWithImage(messageWithImage: MessageWithImage) {
        val imgId = insertImage(messageWithImage.image)
        val message = Message(messageGroupID = messageWithImage.message.messageGroupID, imageID = imgId, text = messageWithImage.message.text, date = messageWithImage.message.date, senderID = messageWithImage.message.senderID)
        insertMessage(message)
    }

}