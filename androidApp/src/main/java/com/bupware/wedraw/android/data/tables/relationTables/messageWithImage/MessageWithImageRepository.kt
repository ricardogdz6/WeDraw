package com.bupware.wedraw.android.data.tables.relationTables.messageWithImage

import com.bupware.wedraw.android.data.tables.image.ImageDao

class MessageWithImageRepository(private val MessWithImgDao: MessageWithImageDao) {

    fun getMessageWithImage(messagesId: Long): MessageWithImage {
        return MessWithImgDao.getMessageWithImage(messagesId)
    }

    suspend fun insertMessageWithImage(messageWithImage: MessageWithImage) {
        MessWithImgDao.insertMessageWithImage(messageWithImage)
    }




}