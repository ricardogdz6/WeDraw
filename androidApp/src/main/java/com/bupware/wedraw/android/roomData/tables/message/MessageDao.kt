package com.bupware.wedraw.android.roomData.tables.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao

interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessagesList(messages: List<Message>)
    @Query("SELECT * FROM messages_table ORDER BY id ASC")
    fun readAllDataMessage(): Flow<List<Message>>

    @Query("SELECT * FROM messages_table WHERE owner_group_Id = :groupId")
    fun getMessagesByGroupId(groupId: Long): Flow<List<Message>>

    @Query("DELETE FROM messages_table")
    suspend fun deleteAll()

}

