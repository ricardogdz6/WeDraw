package com.bupware.wedraw.android.data.tables.relationTables.groupUserMessages

import com.bupware.wedraw.android.data.tables.group.Group
import kotlinx.coroutines.flow.Flow

class GroupWithUsersRepository(private val groupWithUsersDao: GroupWithUsersDao)
{
    suspend fun insert(groupUserCrossRef: GroupUserCrossRef) {
        groupWithUsersDao.insertGroupWithUser(groupUserCrossRef)
    }

    fun getAllUserCrossRefByGroupID(groupID : Long) : Flow<List<GroupUserCrossRef>> = groupWithUsersDao.getSetOfUsersGroupByGroupID(groupID)

    val readAllData : Flow<List<GroupUserCrossRef>> = groupWithUsersDao.readAllData()




}