package com.bupware.wedraw.android.data.tables.image

import androidx.lifecycle.LiveData


class ImageRepository(private val imageDao: ImageDao) {
    suspend fun insert(draw : Image) {
        imageDao.insert(draw)
    }


    suspend fun getDrawingImage(imageid: Long): LiveData<Image> {
        return imageDao.getDrawingImage(imageid)
    }

}