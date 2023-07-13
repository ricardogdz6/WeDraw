package com.bupware.wedraw.android.roomData.tables.message

import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {
    suspend fun insert(message: Message) {
        messageDao.insertMessage(message)
    }

    suspend fun insertMessagesList(messages: List<Message>) {
        messageDao.insertMessagesList(messages)
    }

    val readAllData: Flow<List<Message>> = messageDao.readAllDataMessage()

    suspend fun deleteAll() = messageDao.deleteAll()


}