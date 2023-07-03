package com.bupware.wedraw.android.data.tables.group

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
@Dao

interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroup(group : Group)


}