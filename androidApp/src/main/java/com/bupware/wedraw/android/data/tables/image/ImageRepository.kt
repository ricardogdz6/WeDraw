package com.bupware.wedraw.android.data.tables.image

import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.data.tables.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class ImageRepository(private val imageDao: ImageDao) {
    suspend fun insert(draw: Image) {
        imageDao.insert(draw)
    }


    suspend fun getDrawingImage(imageid: Long): Image? {
        return withContext(Dispatchers.IO) {
            imageDao.getDrawingImage(imageid)
        }
    }


//    val readAllData: Flow<List<Image>> = imageDao.readAllData()

//    suspend fun deleteAllImages() {
//        imageDao.deleteAllImages()
//    }
}