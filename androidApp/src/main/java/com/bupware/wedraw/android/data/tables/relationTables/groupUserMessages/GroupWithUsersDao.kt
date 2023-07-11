package com.bupware.wedraw.android.data.tables.relationTables.groupUserMessages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bupware.wedraw.android.data.tables.group.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupWithUsersDao  {

    @Transaction
    @Query("SELECT * FROM groups_table WHERE groupId = :groupId")
    fun getGroupWithUsersByGroupId(groupId: Long): GroupWithUsers?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroupWithUser(groupWithUser :GroupUserCrossRef)




    @Transaction
    suspend fun crossGroupWithLeader(groupId: Long, userId: String,isAdmin: Boolean) {

        insertGroupWithUser(GroupUserCrossRef(groupId = groupId, userId = userId ,isAdmin= isAdmin))

    }

    @Query("SELECT * FROM groupusercrossref order by groupId ASC")
    fun readAllData(): Flow<List<GroupUserCrossRef>>
}

@Dao
interface UsersWithGroupDao {
    @Transaction
    @Query("SELECT * FROM users_table WHERE userId = :userId")
    fun getUserWithGroupsByUserId(userId: Long): UsersWithGroup?
}