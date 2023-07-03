package com.bupware.wedraw.android.data.tables.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
@Dao

interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: Message)
}