package com.bupware.wedraw.android.data.tables.image

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bupware.wedraw.android.data.tables.user.User

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: Image)

    @Query("SELECT id, uri FROM image_table WHERE id = :imageid")
    fun getDrawingImage(imageid: Long): LiveData<Image>
}