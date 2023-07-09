package com.bupware.wedraw.android.data.tables.message

import com.bupware.wedraw.android.data.tables.image.ImageDao
import com.bupware.wedraw.android.data.tables.relationTables.groupUserMessages.GroupUserCrossRef
import kotlinx.coroutines.flow.Flow

class MessageRepository (private val messageDao: MessageDao) {
    suspend fun insert(message: Message) {
        messageDao.insertMessage(message)
    }

    val readAllData : Flow<List<Message>> = messageDao.readAllDataMessage()



}