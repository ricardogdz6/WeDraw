package com.bupware.wedraw.android.data.tables.relationTables.messageWithImage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bupware.wedraw.android.data.tables.cacheUtils.cacheUtils
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageDao
import com.bupware.wedraw.android.data.tables.message.Message
import com.bupware.wedraw.android.data.tables.message.MessageDao
import com.bupware.wedraw.android.data.tables.relationTables.messageWithImage.MessageWithImage

@Dao
interface MessageWithImageDao : MessageDao, ImageDao {
    @Transaction
    @Query("SELECT * FROM messages_table INNER JOIN images_table ON messages_table.id = images_table.id WHERE messages_table.id = :messagesId")
    fun getMessageWithImage(messagesId: Long): MessageWithImage

    @Transaction
    suspend fun insertMessageWithImage(messageWithImage: MessageWithImage) {
        val imgId = insertImage(messageWithImage.image)
        val message = Message(owner_group_Id = messageWithImage.message.owner_group_Id, image_Id = imgId, text = messageWithImage.message.text, date = messageWithImage.message.date, ownerId = messageWithImage.message.ownerId)
        insertMessage(message)
    }

}