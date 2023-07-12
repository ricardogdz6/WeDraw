package com.bupware.wedraw.android.roomData.tables.message

import kotlinx.coroutines.flow.Flow

class MessageRepository (private val messageDao: MessageDao) {
    suspend fun insert(message: Message) {
        messageDao.insertMessage(message)
    }

    val readAllData : Flow<List<Message>> = messageDao.readAllDataMessage()



}