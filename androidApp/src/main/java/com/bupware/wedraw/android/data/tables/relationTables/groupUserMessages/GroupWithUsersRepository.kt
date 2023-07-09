package com.bupware.wedraw.android.data.tables.relationTables.groupUserMessages

import com.bupware.wedraw.android.data.tables.group.Group
import kotlinx.coroutines.flow.Flow

class GroupWithUsersRepository(private val groupWithUsersDao: GroupWithUsersDao)
{
    suspend fun insert(groupUserCrossRef: GroupUserCrossRef) {
        groupWithUsersDao.insertGroupWithUser(groupUserCrossRef)
    }

    suspend fun crossGroupWithLeader(group: Long, leader: String) {
        groupWithUsersDao.crossGroupWithLeader(group, leader)
    }

    val readAllData : Flow<List<GroupUserCrossRef>> = groupWithUsersDao.readAllData()




}