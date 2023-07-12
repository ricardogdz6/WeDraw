package com.bupware.wedraw.android.roomData.tables.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao

interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages_table ORDER BY id ASC")
    fun readAllDataMessage(): Flow<List<Message>>

}

