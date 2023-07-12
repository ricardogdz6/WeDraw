package com.bupware.wedraw.android.roomData.tables.group

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(group : Group):  Long

    @Query("SELECT * FROM groups_table ORDER BY groupId ASC")
    fun readAllData(): Flow<List<Group>>

    @Query("SELECT * FROM groups_table WHERE groupId = :groupId")
    fun getGroupByGroupId(groupId: Long): Flow<Group>

}