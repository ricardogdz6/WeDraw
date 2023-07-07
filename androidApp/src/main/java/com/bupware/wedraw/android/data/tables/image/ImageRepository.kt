package com.bupware.wedraw.android.data.tables.image

import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.data.tables.user.User


class ImageRepository(private val imageDao: ImageDao) {
    suspend fun insert(draw : Image) {
        imageDao.insert(draw)
    }


    suspend fun getDrawingImage(imageid: Long): LiveData<Image> {
        return imageDao.getDrawingImage(imageid)
    }


    val readAllData : LiveData<List<Image>> = imageDao.readAllData()

//    suspend fun deleteAllImages() {
//        imageDao.deleteAllImages()
//    }
}