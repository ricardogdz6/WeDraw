package com.bupware.wedraw.android.roomData.tables.message

import android.util.Log
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

    suspend fun deleteMessage(message: Message) = messageDao.deleteMessage(message.date!!)

    suspend fun getMessagesByGroupId(groupId: Long): Flow<List<Message>> = messageDao.getMessagesByGroupId(groupId)


}

class MessageFailedRepository(private val messageDao: MessageFailedDao) {
    suspend fun insert(message: MessageFailed) {
        messageDao.insertMessage(message)
    }

    suspend fun insertMessagesList(messages: List<MessageFailed>) {
        messageDao.insertMessagesList(messages)
    }

    val readAllData: Flow<List<MessageFailed>> = messageDao.readAllDataMessage()

    suspend fun deleteAll() = messageDao.deleteAll()

    suspend fun deleteMessage(message: MessageFailed) = messageDao.deleteMessage(message.date!!)

    suspend fun getMessagesByGroupId(groupId: Long): Flow<List<MessageFailed>> = messageDao.getMessagesByGroupId(groupId)


}